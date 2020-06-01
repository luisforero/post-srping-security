package com.example.postspringsecurity.controllers;

import com.example.postspringsecurity.models.Comment;
import com.example.postspringsecurity.models.Post;
import com.example.postspringsecurity.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    final
    PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_VIEWER','ROLE_AUTHOR')")
    public ResponseEntity<List<Post>> getPosts() {
        try {
            List<Post> posts = postService.getPosts();
            if (posts.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            log.error("Error executing GET", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(path = "/addPost")
    @PreAuthorize("hasAuthority('author:write')")
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        try {
            postService.addPost(post);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            log.error("Error executing POST", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(path = "/deletePost")
    @PreAuthorize("hasAuthority('admin:remove')")
    public ResponseEntity<String> deletePost(@RequestParam("id") String postId) {
        try {
            postService.deletePost(postId);

            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            log.info(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("POST not found");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('author:write')")
    public ResponseEntity<Post> updatePost(@RequestParam("postId") String postId, @RequestBody Post post) {
        try {
            return ResponseEntity.ok(postService.updatePost(postId, post));
        } catch (NoSuchElementException e) {
            log.info(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(path = "/comment")
    @PreAuthorize("hasAnyAuthority('author:write','viewer:comment')")
    public ResponseEntity<Comment> addCommentPost(@RequestParam("id") String postId, @RequestBody Comment comment) {
        try {
            return ResponseEntity.ok(postService.addCommentPost(postId, comment.getBody()));
        } catch (NoSuchElementException e) {
            log.info(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
