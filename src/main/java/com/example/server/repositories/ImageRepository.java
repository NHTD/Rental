package com.example.server.repositories;

import com.example.server.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {

    @Query("select i from Image i join i.post p where p.id like %:postId%")
    List<Image> getAllOriginalFileInPost(String postId);

    @Modifying
    @Transactional
    @Query("delete from Image i where i.orginalFile like %:originalFile% and i.post.id = :postId")
    void deleteImageByOriginalFile(String originalFile, String postId);

    @Query("select i from Image  i where i.image = :image")
    Image getImageByImageUrl(String image);

    @Transactional
    void deleteImageByImage(String imageUrl);
}
