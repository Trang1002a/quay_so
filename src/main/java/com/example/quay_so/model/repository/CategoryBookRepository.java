package com.example.quay_so.model.repository;

import com.example.quay_so.model.entity.CategoryBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryBookRepository extends JpaRepository<CategoryBookEntity, String> {

    List<CategoryBookEntity> findAllByBookId(String bookId);

    List<CategoryBookEntity> findAllByBookIdAndStatus(String bookId, String status);
}
