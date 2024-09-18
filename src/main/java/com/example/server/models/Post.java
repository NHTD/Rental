package com.example.server.models;

import com.example.server.enums.PostStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "posts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String title;
    byte star;

    String address;

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    Attribute attribute;

    @ManyToOne
    @JoinColumn(name = "category_code", referencedColumnName = "code")
    Category category;

    @Column(name = "description", length = 100000)
    String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Image> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "price_code", referencedColumnName = "code")
    Price price;

    @ManyToOne
    @JoinColumn(name = "area_code", referencedColumnName = "code")
    Area area;

    @ManyToOne
    @JoinColumn(name = "province_code", referencedColumnName = "code")
    Province province;

    @Column(name = "price_number")
    Float priceNumber;

    @Column(name = "area_number")
    Float areaNumber;

    @Column(name = "cloudinary_image_id")
    private String cloudinaryImageId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PostStatusEnum status;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
