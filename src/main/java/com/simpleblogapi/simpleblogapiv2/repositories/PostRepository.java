package com.simpleblogapi.simpleblogapiv2.repositories;

import com.simpleblogapi.simpleblogapiv2.entities.Category;
import com.simpleblogapi.simpleblogapiv2.entities.Post;
import com.simpleblogapi.simpleblogapiv2.entities.User;
import com.simpleblogapi.simpleblogapiv2.enums.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    boolean existsByCategory(Category category);
    List<Post> findByAuthor(User author);
    List<Post> findByStatus(PostStatus status);

}