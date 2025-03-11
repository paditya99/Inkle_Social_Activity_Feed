package com.Inkle.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.Inkle.project.service.PostService;
import com.Inkle.project.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final PostService postService;

    public AdminController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {
//        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{postId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{postId}/likes")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<Void> deleteLikes(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {
        postService.clearLikes(postId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
} 