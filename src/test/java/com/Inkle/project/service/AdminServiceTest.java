package com.Inkle.project.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.Inkle.project.entity.User;
import com.Inkle.project.entity.Post;
import com.Inkle.project.repository.UserRepository;
import com.Inkle.project.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private UserService adminService;
    private PostService postService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

//        adminService.deleteUser(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeletePost() {
        Post post = new Post();
        post.setId(1L);
        post.setContent("This is a test post");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.deletePost(1L);

        verify(postRepository, times(1)).delete(post);
    }
} 