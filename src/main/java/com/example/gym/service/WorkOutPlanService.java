package com.example.gym.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gym.dto.request.Update.WorkOutPlanUpdateRequest;
import com.example.gym.dto.request.WorkOutPlanRequest;
import com.example.gym.dto.response.WorkOutPlanResponse;
import com.example.gym.entity.Exercise;
import com.example.gym.entity.User;
import com.example.gym.entity.WorkOutPlan;
import com.example.gym.exception.AppException;
import com.example.gym.exception.ErrorCode;
import com.example.gym.mapper.WorkOutPlanMapper;
import com.example.gym.repository.ExerciseRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.repository.WorkOutPlanRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkOutPlanService {
    WorkOutPlanRepository workOutPlanRepository;
    WorkOutPlanMapper workOutPlanMapper;
    UserRepository userRepository;
    ExerciseRepository exerciseRepository;

    public WorkOutPlanResponse createWorkOutPlan(WorkOutPlanRequest workOutPlanRequest) {
        User user = userRepository.findById(workOutPlanRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));

        Exercise exercise = exerciseRepository.findById(workOutPlanRequest.getExerciseId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));

        WorkOutPlan workOutPlan = workOutPlanMapper.maptWorkOutPlan(workOutPlanRequest);
        workOutPlan.setUser(user);
        workOutPlan.setExercise(exercise);
        workOutPlan.setActive(false);
        return workOutPlanMapper.toWorkOutPlanResponse(workOutPlanRepository.save(workOutPlan));
    }

    public WorkOutPlanResponse getWorkOutPlan(Long id) {
        WorkOutPlan workOutPlan = workOutPlanRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        return workOutPlanMapper.toWorkOutPlanResponse(workOutPlan);
    }

    public List<WorkOutPlan> getWorkOutPlanByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        return workOutPlanRepository.findAllByUserId(user.getId());
    }

    public List<WorkOutPlan> getWorkOutPlanByUserIdAndDate(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        return workOutPlanRepository.findByUserIdAndDate(user.getId(), date);
    }

    public WorkOutPlanResponse updateWorkOutPlan(Long id, WorkOutPlanUpdateRequest workOutPlanRequest) {
        WorkOutPlan workOutPlan = workOutPlanRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        workOutPlanMapper.updateWorkOutPlan(workOutPlanRequest, workOutPlan);
        workOutPlan.setActive(true);
        return workOutPlanMapper.toWorkOutPlanResponse(workOutPlanRepository.save(workOutPlan));
    }

    public void deleteWorkOutPlan(Long id) {
        WorkOutPlan workOutPlan = workOutPlanRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID));
        workOutPlanRepository.delete(workOutPlan);
    }
}
