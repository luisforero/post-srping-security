package com.example.postspringsecurity.services;

import com.example.postspringsecurity.models.Category;
import com.example.postspringsecurity.models.Comment;
import com.example.postspringsecurity.models.Post;
import com.example.postspringsecurity.models.User;
import com.example.postspringsecurity.repositories.PostRepository;
import com.example.postspringsecurity.security.utility.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.*;

@Service
public class PostService {

    final
    PostRepository postRepository;
    final
    AuthenticationFacade authenticationFacade;

    @Autowired
    public PostService(PostRepository postRepository, AuthenticationFacade authenticationFacade) {
        this.postRepository = postRepository;
        this.authenticationFacade = authenticationFacade;
    }

    public List<Post> getPosts() {
        List<Post> posts = new ArrayList<>();
        postRepository.findAll().forEach(posts::add);
        return posts;
    }

    public void addPost(Post post) {
        Authentication auth = authenticationFacade.getAuthentication();

        User user = new User();
        user.setName(auth.getName());
        user.setStatus("AUTHOR");
        post.setUser(user);
        post.setTags(Arrays.asList(post.getCategory().name(), Category.events.name()));

        post.setDate(Date.from(Instant.now()));

        postRepository.save(post);
    }

    public Post updatePost(String postId, Post post) {
        Post postUpdated = postRepository.findByObjectId(postId);
        if (postUpdated == null) {
            throw new NoSuchElementException(String.format("No element found by ObjectID=%s", postId));
        }
        postUpdated.setBody(post.getBody());
        postUpdated.setTitle(post.getTitle());
        postUpdated.setCategory(post.getCategory());
        postUpdated.setTags(post.getTags());
        postUpdated.setLastUpdate(Date.from(Instant.now()));
        postRepository.save(postUpdated);

        return postUpdated;
    }

    public void deletePost(String postId) {
        Post postToDelete = postRepository.findByObjectId(postId);
        if (postToDelete == null) {
            throw new NoSuchElementException(String.format("No element found by ObjectID=%s", postId));
        }
        postRepository.delete(postToDelete);
    }

    public Comment addCommentPost(String postId, String comment) {

        Post postToUpdate = postRepository.findByObjectId(postId);
        if (postToUpdate == null) {
            throw new NoSuchElementException(String.format("No element found by ObjectID=%s", postId));
        }

        Comment comment1 = Comment.builder()
                .body(comment)
                .user(authenticationFacade.getAuthentication().getName())
                .date(Date.from(Instant.now()))
                .build();

        if (postToUpdate.getComments() == null) {
            postToUpdate.setComments(Collections.singletonList(comment1));
        } else {
            postToUpdate.getComments().add(comment1);
        }

        postRepository.save(postToUpdate);
        return comment1;
    }

}
