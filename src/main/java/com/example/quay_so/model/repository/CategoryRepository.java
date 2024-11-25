package com.example.quay_so.model.repository;

import com.example.quay_so.model.entity.CategoriesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoriesEntity, String> {
    List<CategoriesEntity> findAllByStatusOrderByCreatedAtAsc(String status);

    List<CategoriesEntity> findAllByIdInAndStatus(List<String> ids, String status);

    Optional<CategoriesEntity> findByName(String name);

    @Query("SELECT cb.bookId FROM CategoriesEntity c " +
            "INNER JOIN CategoryBookEntity cb " +
            "ON (c.id = cb.categoryId) " +
            "WHERE cb.status = :status " +
            "AND c.id IN :categoriesId")
    List<String> findListBookIds(String status,
                                 List<String> categoriesId);

    @Query("SELECT c FROM CategoriesEntity c " +
            "WHERE ((COALESCE(:status , '0') = '0' OR c.status IN :status)) " +
            "AND (:name IS NULL OR c.name LIKE %:name%)")
    Page<CategoriesEntity> findCategoriesWithRequest(String name, List<String> status, Pageable pageable);
}
