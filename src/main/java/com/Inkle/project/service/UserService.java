package com.Inkle.project.service;

import com.Inkle.project.dto.UserDTO;
import com.Inkle.project.entity.User;
import com.Inkle.project.enums.UserRole;
import com.Inkle.project.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ActivityService activityService;

    public UserService(UserRepository userRepository, ActivityService activityService) {
        this.userRepository = userRepository;
        this.activityService = activityService;
    }

    @Transactional
    public void followUser(String currentUsername, Long targetUserId) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        if (targetUser.getBlockedUsers().contains(currentUser)) {
            throw new RuntimeException("You cannot follow this user");
        }

        targetUser.getFollowers().add(currentUser);
        userRepository.save(targetUser);
        activityService.createActivity(currentUser, "followed " + targetUser.getUsername());
    }

    @Transactional
    public void unfollowUser(String currentUsername, Long targetUserId) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        targetUser.getFollowers().remove(currentUser);
        userRepository.save(targetUser);
    }

    @Transactional
    public void blockUser(String currentUsername, Long targetUserId) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        currentUser.getBlockedUsers().add(targetUser);
        // Remove any existing follow relationship
        currentUser.getFollowers().remove(targetUser);
        targetUser.getFollowers().remove(currentUser);
        
        userRepository.save(currentUser);
        userRepository.save(targetUser);
    }

    @Transactional
    public void unblockUser(String currentUsername, Long targetUserId) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        currentUser.getBlockedUsers().remove(targetUser);
        userRepository.save(currentUser);
    }

    @Transactional
    public void promoteToAdmin(String currentUsername, Long targetUserId) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        if (currentUser.getRole() != UserRole.OWNER) {
            throw new RuntimeException("Only owners can promote users to admin");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        targetUser.setRole(UserRole.ADMIN);
        userRepository.save(targetUser);
        activityService.createActivity(currentUser, "promoted " + targetUser.getUsername() + " to admin");
    }

    @Transactional
    public void removeAdmin(String currentUsername, Long targetUserId) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        if (currentUser.getRole() != UserRole.OWNER) {
            throw new RuntimeException("Only owners can remove admin privileges");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        if (targetUser.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Target user is not an admin");
        }

        targetUser.setRole(UserRole.USER);
        userRepository.save(targetUser);
    }

    @Transactional
    public void deleteUser(String currentUsername, Long targetUserId) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        if (currentUser.getRole() != UserRole.OWNER && currentUser.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only owners and admins can delete users");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        if (targetUser.getRole() == UserRole.OWNER) {
            throw new RuntimeException("Cannot delete owner account");
        }

        if (currentUser.getRole() == UserRole.ADMIN && targetUser.getRole() == UserRole.ADMIN) {
            throw new RuntimeException("Admins cannot delete other admins");
        }

        userRepository.delete(targetUser);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(user -> {
                UserDTO dto = new UserDTO();
                dto.setId(user.getId());
                dto.setUsername(user.getUsername());
                dto.setEmail(user.getEmail());
                return dto;
            })
            .collect(Collectors.toList());
    }
} 