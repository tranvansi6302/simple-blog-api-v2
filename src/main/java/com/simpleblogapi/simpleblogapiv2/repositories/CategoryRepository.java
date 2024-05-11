package com.simpleblogapi.simpleblogapiv2.repositories;

import com.simpleblogapi.simpleblogapiv2.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByName(String name);
    Optional<Category> findByName(String name);
}