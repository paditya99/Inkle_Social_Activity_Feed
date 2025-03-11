package com.Inkle.project.service;

import com.Inkle.project.entity.User;
import com.Inkle.project.enums.UserRole;
import com.Inkle.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ActivityService activityService;
    
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

//        User registeredUser = userService.register(user);

//        assertEquals("testuser", registeredUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testFollowUser() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        userService.followUser(null, null);

        assertTrue(user1.getFollowing().contains(user2));
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    public void testPreventSelfFollow() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.followUser(null, null);
        });

        assertEquals("You cannot follow yourself", exception.getMessage());
    }


    @Test
    public void testUserAlreadyExists() {
        User user = new User();
        user.setUsername("existinguser");
        user.setEmail("existing@example.com");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
//            userService.register(null, null);
        });

        assertEquals("Username already exists", exception.getMessage());
    }

    // Add more tests...
} 