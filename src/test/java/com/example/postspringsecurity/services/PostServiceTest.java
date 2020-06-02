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

import static org.junit.jupiter.api.Assertions.*;
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
                .id("123")
                .body("Body")
                .title("title")
                .category(Category.News)
                .date(now)
                .tags(Arrays.asList(Category.News.name(), Category.events.name()))
                .user(user)
                .build();

        List<String> newTags = Arrays.asList(Category.Entertainment.name(), Category.events.name());
        Post postToUpdate = Post.builder()
                .id("123")
                .body("NEW BODY")
                .title("NEW Title")
                .category(Category.Entertainment)
                .tags(newTags)
                .build();

        when(postRepositoryMock.findByObjectId("123")).thenReturn(Optional.of(post1));

        ArgumentCaptor captor = ArgumentCaptor.forClass(Post.class);
        ArgumentCaptor captorStr = ArgumentCaptor.forClass(String.class);

        when(postRepositoryMock.save(any(Post.class))).thenReturn(postToUpdate);

        postService.updatePost("123", postToUpdate);

        verify(postRepositoryMock).findByObjectId((String) captorStr.capture());
        verify(postRepositoryMock).save((Post) captor.capture());

        assertEquals("userName", ((Post) captor.getValue()).getUser().getName());
        assertEquals("AUTHOR", ((Post) captor.getValue()).getUser().getStatus());
        assertEquals("NEW BODY", ((Post) captor.getValue()).getBody());
        assertEquals("NEW Title", ((Post) captor.getValue()).getTitle());
        assertEquals(user, ((Post) captor.getValue()).getUser());
        assertEquals(newTags, ((Post) captor.getValue()).getTags());
        assertEquals(Category.Entertainment, ((Post) captor.getValue()).getCategory());
        assertEquals(now, ((Post) captor.getValue()).getDate());
        assertNotNull(((Post) captor.getValue()).getLastUpdate());

        assertEquals("123", captorStr.getValue());

    }

    @Test()
    public void updatePostTestNoSuchElement() {
        User user = new User("userName", "AUTHOR");

        Post post1 = Post.builder()
                .id("1235")
                .body("Body")
                .title("title")
                .category(Category.News)
                .date(now)
                .tags(Arrays.asList(Category.News.name(), Category.events.name()))
                .user(user)
                .build();

        when(postRepositoryMock.findByObjectId("123")).thenReturn(Optional.empty());

        ArgumentCaptor captor = ArgumentCaptor.forClass(Post.class);
        ArgumentCaptor captorStr = ArgumentCaptor.forClass(String.class);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> postService.updatePost("123", post1));
        assertEquals(exception.getMessage(), "No element found by ObjectID=123");

        verify(postRepositoryMock).findByObjectId((String) captorStr.capture());

        assertEquals("123", captorStr.getValue());

    }

    @Test
    public void deletePostTest() {
        Post post1 = Post.builder()
                .id("123")
                .body("Body")
                .title("title")
                .category(Category.News)
                .date(now)
                .tags(Arrays.asList(Category.News.name(), Category.events.name()))
                .build();

        when(postRepositoryMock.findByObjectId("123")).thenReturn(Optional.of(post1));

        ArgumentCaptor captor = ArgumentCaptor.forClass(Post.class);
        ArgumentCaptor captorStr = ArgumentCaptor.forClass(String.class);

        postService.deletePost("123");

        verify(postRepositoryMock).findByObjectId((String) captorStr.capture());
        verify(postRepositoryMock).delete((Post) captor.capture());

        assertEquals("123", captorStr.getValue());
        assertEquals("123", ((Post) captor.getValue()).getId());
    }

    @Test()
    public void deletePostTestNoSuchElement() {
        when(postRepositoryMock.findByObjectId("123")).thenReturn(Optional.empty());

        ArgumentCaptor captorStr = ArgumentCaptor.forClass(String.class);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> postService.deletePost("123"));
        assertEquals(exception.getMessage(), "No element found by ObjectID=123");

        verify(postRepositoryMock).findByObjectId((String) captorStr.capture());

        assertEquals("123", captorStr.getValue());
    }


    @Test
    public void addCommentPost() {
        User user = new User("userName", "AUTHOR");
        Authentication authenticationMock = mock(Authentication.class);

        when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getName()).thenReturn("userName");

        Post postToUpdate = Post.builder()
                .id("123")
                .body("NEW BODY")
                .title("NEW Title")
                .category(Category.Entertainment)
                .tags(Arrays.asList(Category.Entertainment.name(), Category.events.name()))
                .build();

        when(postRepositoryMock.findByObjectId("123")).thenReturn(Optional.of(postToUpdate));

        ArgumentCaptor captor = ArgumentCaptor.forClass(Post.class);
        ArgumentCaptor captorStr = ArgumentCaptor.forClass(String.class);

        when(postRepositoryMock.save(any(Post.class))).thenReturn(postToUpdate);

        postService.addCommentPost("123", "commentary");

        verify(postRepositoryMock).findByObjectId((String) captorStr.capture());
        verify(postRepositoryMock).save((Post) captor.capture());

        assertEquals("123", captorStr.getValue());
        assertEquals("123", ((Post) captor.getValue()).getId());
        assertNotNull(((Post) captor.getValue()).getComments());
        assertEquals("commentary", ((Post) captor.getValue()).getComments().get(0).getBody());
        assertEquals("userName", ((Post) captor.getValue()).getComments().get(0).getUser());
        assertNotNull(((Post) captor.getValue()).getComments().get(0).getDate());

    }

    @Test()
    public void addCommentPostTestNoSuchElement() {
        when(postRepositoryMock.findByObjectId("123")).thenReturn(Optional.empty());

        ArgumentCaptor captorStr = ArgumentCaptor.forClass(String.class);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> postService.addCommentPost("123", "comment"));
        assertEquals(exception.getMessage(), "No element found by ObjectID=123");

        verify(postRepositoryMock).findByObjectId((String) captorStr.capture());

        assertEquals("123", captorStr.getValue());
    }

}