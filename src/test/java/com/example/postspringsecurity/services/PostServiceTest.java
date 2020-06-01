package com.example.postspringsecurity.services;

import com.example.postspringsecurity.models.Category;
import com.example.postspringsecurity.models.Post;
import com.example.postspringsecurity.models.User;
import com.example.postspringsecurity.repositories.PostRepository;
import com.example.postspringsecurity.security.utility.AuthenticationFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private final Date now = Date.from(Instant.now());

    @Mock
    AuthenticationFacade authenticationFacadeMock;
    @Mock
    PostRepository postRepositoryMock;

    @InjectMocks
    PostService postService;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getPostsTest() {
        Post post1 = Post.builder()
                .id("123")
                .body("Body")
                .title("title")
                .date(now)
                .build();

        when(postRepositoryMock.findAll()).thenReturn(Collections.singletonList(post1));

        List<Post> posts = postService.getPosts();
        verify(postRepositoryMock, atLeastOnce()).findAll();
        assertEquals(1, posts.size());
        assertEquals(post1, posts.get(0));

    }

    @Test
    public void addPostTest() {
        Authentication authenticationMock = mock(Authentication.class);

        when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getName()).thenReturn("userName");

        Post post1 = Post.builder()
                .id("123")
                .body("Body")
                .title("title")
                .category(Category.News)
                .build();

        ArgumentCaptor captor = ArgumentCaptor.forClass(Post.class);

        when(postRepositoryMock.save(any(Post.class))).thenReturn(post1);

        postService.addPost(post1);

        verify(postRepositoryMock).save((Post) captor.capture());
        verify(authenticationMock, atLeastOnce()).getName();

        assertEquals("userName", ((Post) captor.getValue()).getUser().getName());
        assertEquals("AUTHOR", ((Post) captor.getValue()).getUser().getStatus());
        assertEquals(Arrays.asList(Category.News.name(), Category.events.name()), ((Post) captor.getValue()).getTags());
        assertEquals(Category.News, ((Post) captor.getValue()).getCategory());
        assertNotNull(((Post) captor.getValue()).getDate());

    }

    @Test
    public void updatePostTest() {
        User user = new User("userName", "AUTHOR");

        Post post1 = Post.builder()
                .body("Body")
                .title("title")
                .category(Category.News)
                .tags(Arrays.asList(Category.News.name(), Category.events.name()))
                .user(user)
                .build();

        when(postRepositoryMock.findByObjectId("123")).thenReturn(post1);

        ArgumentCaptor captor = ArgumentCaptor.forClass(Post.class);
        ArgumentCaptor captorStr = ArgumentCaptor.forClass(String.class);

        when(postRepositoryMock.save(any(Post.class))).thenReturn(post1);

        postService.updatePost("123", post1);

        verify(postRepositoryMock).findByObjectId((String) captorStr.capture());
        verify(postRepositoryMock).save((Post) captor.capture());

        assertEquals("userName", ((Post) captor.getValue()).getUser().getName());
        assertEquals("AUTHOR", ((Post) captor.getValue()).getUser().getStatus());
        assertEquals(Category.News, ((Post) captor.getValue()).getCategory());
        assertNotNull(((Post) captor.getValue()).getDate());

        assertEquals("123", captorStr.getValue());

    }

}