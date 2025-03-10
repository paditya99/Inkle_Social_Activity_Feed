package com.Inkle.project.service;

import com.Inkle.project.dto.ActivityDTO;
import com.Inkle.project.entity.Activity;
import com.Inkle.project.entity.User;
import com.Inkle.project.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Transactional
    public void createActivity(User user, String action) {
        Activity activity = new Activity();
        activity.setUser(user);
        activity.setAction(action);
        activityRepository.save(activity);
    }

    @Transactional(readOnly = true)
    public List<ActivityDTO> getRecentActivities() {
        return activityRepository.findTop20ByOrderByCreatedAtDesc()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private ActivityDTO convertToDTO(Activity activity) {
        ActivityDTO dto = new ActivityDTO();
        dto.setId(activity.getId());
        dto.setUsername(activity.getUser().getUsername());
        dto.setAction(activity.getAction());
        dto.setCreatedAt(activity.getCreatedAt());
        return dto;
    }
} 