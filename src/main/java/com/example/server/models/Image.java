package com.example.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "images")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(columnDefinition = "LONGTEXT")
    String image;

    @Column(name = "original_file")
    String orginalFile;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    Post post;

    @Column(name = "cloudinary_image_id")
    private String cloudinaryImageId;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
