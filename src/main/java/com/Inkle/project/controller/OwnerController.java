package com.Inkle.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.Inkle.project.service.UserService;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {
    private final UserService userService;

    public OwnerController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admins/{userId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> promoteToAdmin(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.promoteToAdmin(userDetails.getUsername(), userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admins/{userId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> removeAdmin(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.removeAdmin(userDetails.getUsername(), userId);
        return ResponseEntity.ok().build();
    }
} 