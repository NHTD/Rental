package com.example.server.repositories;

import com.example.server.enums.PostStatusEnum;
import com.example.server.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

    @Query("select p from Post p " +
            "join p.price pr " +
            "join p.area a " +
            "join p.category c " +
            "join p.province prov " +
            "where (:priceCode is null or :priceCode = '' or pr.code like %:priceCode%) " +
            "and (:areaCode is null or :areaCode = '' or a.code like %:areaCode%) " +
            "and (:categoryCode is null or :categoryCode = '' or c.code like %:categoryCode%) " +
            "and (:provinceCode is null or :provinceCode = '' or prov.code like %:provinceCode%) " +
            "and (:minPrice is null or :maxPrice is null or (p.priceNumber between :minPrice and :maxPrice))")
    Page<Post> getAllPost(Pageable pageable, String priceCode, String areaCode, String categoryCode, String provinceCode, Float minPrice, Float maxPrice);

    @Query("select p from Post p order by p.createdAt desc")
    List<Post> getNewPostLimit(Pageable pageable);

    @Query("select p from Post p join p.user u where u.accountType like %:accountType%")
    Page<Post> getPostsOfUser(Pageable pageable, String accountType);

    List<Post> findByStatus(PostStatusEnum status);

    Optional<Post> findByTitle(String title);
}
