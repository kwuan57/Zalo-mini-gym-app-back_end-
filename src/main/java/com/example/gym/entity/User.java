package com.example.gym.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Table(name = "user")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false, length = 100)
    String name;

    @Column(name = "phone_number", length = 10)
    String phoneNumber;

    @Column(name = "password", nullable = false, length = 100)
    String password;

    @Column(name = "email")
    String email;

    @Column(name = "age", nullable = false)
    Integer age;

    @Column(name = "height", nullable = false)
    Float height;

    @Column(name = "weight", nullable = false)
    Float weight;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @Column(name = "is_active")
    boolean active;

    @Column(name = "description", length = 255)
    String description;
}
