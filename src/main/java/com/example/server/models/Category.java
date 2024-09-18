package com.example.server.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "categories", uniqueConstraints = {@UniqueConstraint(columnNames = "code")})
public class Category{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String code;
    String value;
    String header;
    @Column(name = "sub_header")
    String subHeader;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
