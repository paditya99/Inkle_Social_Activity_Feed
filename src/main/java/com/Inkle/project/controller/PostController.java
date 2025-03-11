package com.Inkle.project.controller;

import com.Inkle.project.dto.PostRequest;
import com.Inkle.project.dto.PostResponse;
import com.Inkle.project.entity.Post;

import com.Inkle.project.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request, @AuthenticationPrincipal UserDetails userDetails) {
    	System.out.print("username in PostController= "+userDetails.getUsername());
        PostResponse createdPost = postService.createPost(request, userDetails.getUsername());
        return ResponseEntity.ok(createdPost);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostResponse> likePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(postService.likePost(postId, userDetails.getUsername()));
    }

    @PostMapping("/{postId}/unlike")
    public ResponseEntity<PostResponse> unlikePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(postService.unlikePost(postId, userDetails.getUsername()));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> getFeed(
    		@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(postService.getFeed(userDetails.getUsername()));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("Post deleted successfully");
    }
} 