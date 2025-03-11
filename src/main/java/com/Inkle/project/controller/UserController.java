package com.Inkle.project.controller;

import com.Inkle.project.service.UserService;
import com.Inkle.project.dto.UserDTO;
import com.Inkle.project.entity.User;
import com.Inkle.project.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<Void> followUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId) {
        userService.followUser(userDetails.getUsername(), userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/unfollow")
    public ResponseEntity<Void> unfollowUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId) {
        userService.unfollowUser(userDetails.getUsername(), userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/block")
    public ResponseEntity<Void> blockUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId) {
        userService.blockUser(userDetails.getUsername(), userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/unblock")
    public ResponseEntity<Void> unblockUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId) {
        userService.unblockUser(userDetails.getUsername(), userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/promote")
    public ResponseEntity<Void> promoteToAdmin(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId) {
        userService.promoteToAdmin(userDetails.getUsername(), userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/demote")
    public ResponseEntity<Void> removeAdmin(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId) {
        userService.removeAdmin(userDetails.getUsername(), userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId) {
        userService.deleteUser(userDetails.getUsername(), userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        
        return ResponseEntity.ok(response);
    }
} 