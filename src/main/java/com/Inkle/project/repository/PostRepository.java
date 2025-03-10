package com.Inkle.project.repository;

import com.Inkle.project.entity.Post;
import com.Inkle.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserOrderByCreatedAtDesc(User user);
    
    @Query("SELECT p FROM Post p WHERE p.user NOT IN :blockedUsers ORDER BY p.createdAt DESC")
    List<Post> findAllVisiblePosts(List<User> blockedUsers);
} 