package com.example.postspringsecurity.controllers;

import com.example.postspringsecurity.models.Post;
import com.example.postspringsecurity.repositories.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class PostController {

    @Autowired
    PostRepository postRepository;

    @GetMapping(path = "/posts")
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
            postRepository.save(post);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            log.error("Error executing POST", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
