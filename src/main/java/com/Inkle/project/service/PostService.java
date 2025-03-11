package com.Inkle.project.service;

import com.Inkle.project.dto.PostRequest;
import com.Inkle.project.dto.PostResponse;
import com.Inkle.project.entity.Post;
import com.Inkle.project.entity.User;
import com.Inkle.project.repository.PostRepository;
import com.Inkle.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ActivityService activityService;

    @Autowired
    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            ActivityService activityService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.activityService = activityService;
    }

    @Transactional
    public PostResponse createPost(PostRequest request, String username) {
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Post content cannot be empty");
        }
        
        System.out.print("username in createPost method= "+username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Post post = new Post();
        post.setContent(request.getContent().trim());
        post.setUser(user);

        Post savedPost = postRepository.save(post);
        activityService.createActivity(user, "created a post");

        return mapToPostResponse(savedPost, user);
    }

    @Transactional
    public PostResponse likePost(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (post.getLikes().contains(user)) {
            throw new IllegalArgumentException("You have already liked this post");
        }

        post.getLikes().add(user);
        activityService.createActivity(user, "liked " + post.getUser().getUsername() + "'s post");

        return mapToPostResponse(postRepository.save(post), user);
    }

    @Transactional
    public PostResponse unlikePost(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getLikes().contains(user)) {
            throw new IllegalArgumentException("You haven't liked this post yet");
        }

        post.getLikes().remove(user);
        return mapToPostResponse(postRepository.save(post), user);
    }

    public List<PostResponse> getFeed(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<User> blockedUsers = user.getBlockedUsers().stream().collect(Collectors.toList());
        return postRepository.findAllVisiblePosts(blockedUsers).stream()
                .map(post -> mapToPostResponse(post, user))
                .collect(Collectors.toList());
    }

    private PostResponse mapToPostResponse(Post post, User currentUser) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setContent(post.getContent());
        response.setUsername(post.getUser().getUsername());
        response.setLikesCount(post.getLikes().size());
        response.setLiked(post.getLikes().contains(currentUser));
        response.setCreatedAt(post.getCreatedAt());
        return response;
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.delete(post);
    }

    @Transactional
    public void clearLikes(Long postId, String adminUsername) {
        // Verify admin exists
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        // Get the post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Clear all likes
        post.getLikes().clear();
        
        // Save the post
        postRepository.save(post);
        
        // Create activity record
        activityService.createActivity(admin, "cleared all likes from post by " + post.getUser().getUsername());
    }
} 