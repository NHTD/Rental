package com.example.server.repositories;

import com.example.server.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByAccountType(String accountType);

    @Query("SELECT u.id FROM User u")
    List<String> findAllUserIds();

    Optional<User> findByGoogleAccountId(String googleAccountId);
}
