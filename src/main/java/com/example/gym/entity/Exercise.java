package com.example.gym.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false, length = 100)
    String name;

    @Column(name = "description", nullable = false, length = 500)
    String description;

    @Column(name = "video_url", nullable = false, length = 255)
    String videoUrl;

    @Column(name = "time", nullable = false, length = 100)
    String time;

    @Column(name = "calories", nullable = false)
    Float calories;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @Column(name = "level")
    String level;

    @Column(name = "image_url")
    String imageUrl;
}
