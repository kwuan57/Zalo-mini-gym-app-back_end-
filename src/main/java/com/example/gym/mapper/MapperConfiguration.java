package com.example.gym.mapper;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    @Bean
    public CategoryMapper categoryMapper() {
        return Mappers.getMapper(CategoryMapper.class);
    }

    @Bean
    public ExerciseMapper exerciseMapper() {
        return Mappers.getMapper(ExerciseMapper.class);
    }

    @Bean
    public RoleMapper roleMapper() {
        return Mappers.getMapper(RoleMapper.class);
    }

    @Bean
    public GoalMapper goalMapper() {
        return Mappers.getMapper(GoalMapper.class);
    }

    @Bean
    public NutritionMapper nutritionMapper() {
        return Mappers.getMapper(NutritionMapper.class);
    }

    @Bean
    public HealthDataMapper healthDataMapper() {
        return Mappers.getMapper(HealthDataMapper.class);
    }

    @Bean
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }

    @Bean
    public MealMapper mealMapper() {
        return Mappers.getMapper(MealMapper.class);
    }
}
