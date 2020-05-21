package com.example.postspringsecurity.controllers;

import com.example.postspringsecurity.models.Post;
import com.example.postspringsecurity.models.User;
import com.example.postspringsecurity.repositories.PostRepository;
import com.example.postspringsecurity.security.utility.AuthenticationFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @GetMapping()
    public ResponseEntity<List<Post>> getPosts() {
        try {
            List<Post> posts = new ArrayList<>();
            postRepository.findAll().forEach(posts::add);
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
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        try {
            Authentication auth = authenticationFacade.getAuthentication();

            User user = new User();
            user.setName(auth.getName());
            user.setStatus("AUTHOR");
            post.setUser(user);

            post.setDate(Date.from(Instant.now()));

            postRepository.save(post);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            log.error("Error executing POST", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(path = "/deletePost")
    public ResponseEntity<String> deletePost(@RequestParam("id") String postId) {
        try {
            Post postToDelete = postRepository.findByObjectId(postId);
            if (postToDelete == null) {
                throw new NoSuchElementException(String.format("No element found by ObjectID=%s", postId));
            }
            postRepository.delete(postToDelete);

            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            log.info(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("POST not found");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
