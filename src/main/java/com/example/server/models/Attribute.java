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
@Table(name = "attributes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attribute{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String price;

    String acreage;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

}
