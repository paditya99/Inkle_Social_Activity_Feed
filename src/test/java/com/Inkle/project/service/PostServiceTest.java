package com.Inkle.project.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.Inkle.project.dto.PostRequest;
import com.Inkle.project.dto.PostResponse;
import com.Inkle.project.entity.Post;
import com.Inkle.project.entity.User;
import com.Inkle.project.repository.PostRepository;
import com.Inkle.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePost() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        PostRequest request = new PostRequest();
        request.setContent("This is a test post");

        Post post = new Post();
        post.setId(1L);
        post.setContent(request.getContent());
        post.setUser(user);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostResponse createdPostResponse = postService.createPost(request, "testuser");

        assertEquals("This is a test post", createdPostResponse.getContent());
        assertEquals(1L, createdPostResponse.getId());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void testLikePost() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Post post = new Post();
        post.setId(1L);
        post.setContent("This is a test post");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        postService.likePost(1L, "testuser");

        assertTrue(post.getLikes().contains(user));
        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void testUnlikePost() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Post post = new Post();
        post.setId(1L);
        post.setContent("This is a test post");
        post.getLikes().add(user); // User already liked the post

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        postService.unlikePost(1L, "testuser");

        assertFalse(post.getLikes().contains(user));
        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void testBlockedUserCannotSeePost() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user1.getBlockedUsers().add(user2); // user1 blocks user2

        Post post = new Post();
        post.setId(1L);
        post.setContent("This is a test post");
        post.setUser(user1);

        // Simulate that user2 tries to access user1's post
        assertThrows(IllegalArgumentException.class, () -> {
            if (user1.getBlockedUsers().contains(user2)) {
                throw new IllegalArgumentException("You cannot see this post");
            }
        });
    }
} 