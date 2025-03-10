package com.Inkle.project.repository;

import com.Inkle.project.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findTop20ByOrderByCreatedAtDesc();
} 