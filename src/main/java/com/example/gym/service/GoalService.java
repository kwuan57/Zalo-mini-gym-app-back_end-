package com.example.gym.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gym.dto.request.GoalRequest;
import com.example.gym.dto.request.Update.GoalUpdateRequest;
import com.example.gym.dto.response.GoalResponse;
import com.example.gym.entity.Goal;
import com.example.gym.entity.User;
import com.example.gym.exception.AppException;
import com.example.gym.exception.ErrorCode;
import com.example.gym.mapper.GoalMapper;
import com.example.gym.repository.GoalRepository;
import com.example.gym.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoalService {
    GoalRepository goalRepository;
    GoalMapper goalMapper;
    UserRepository userRepository;

    public GoalResponse createGole(GoalRequest goalRequest) {
        User user = userRepository.findById(goalRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (goalRequest.getName() == null || goalRequest.getName().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_GOAL_NAME);
        }

        if (goalRepository.existsByUserIdAndName(user.getId(), goalRequest.getName())) {
            throw new AppException(ErrorCode.GOAL_ALREADY_EXISTS);
        }

        Goal goal = goalMapper.maptoGoal(goalRequest);
        goal.setUser(user);
        return goalMapper.toGoalResponse(goalRepository.save(goal));
    }

    public List<GoalResponse> getAllGoals() {
        return goalRepository.findAll().stream().map(goalMapper::toGoalResponse).toList();
    }

    public List<Goal> getAllGoalsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return goalRepository.findAllByUserId(user.getId());
    }

    public GoalResponse updateGoal(Long id, GoalUpdateRequest goalRequest) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));

        User user = userRepository.findById(goalRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (goalRequest.getName() == null || goalRequest.getName().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_GOAL_NAME);
        }
        if (goalRepository.existsByUserIdAndName(user.getId(), goalRequest.getName())) {
            throw new AppException(ErrorCode.GOAL_ALREADY_EXISTS);
        }
        goalMapper.updateGoal(goalRequest, goal);
        goal.setUser(user);
        return goalMapper.toGoalResponse(goalRepository.save(goal));
    }

    public void deleteGoal(Long id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        goalRepository.delete(goal);
    }
}
