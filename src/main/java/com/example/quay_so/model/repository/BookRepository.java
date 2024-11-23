package com.example.quay_so.model.repository;

import com.example.quay_so.model.entity.BooksEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BooksEntity, String> {

    Optional<BooksEntity> findByName(String name);

    Optional<BooksEntity> findByIdAndStatus(String id, String status);

    List<BooksEntity> findAllByNameContaining(String name);

    List<BooksEntity> findAllByIdInAndStatus(List<String> ids, String status);

    @Query("SELECT COUNT(b) FROM BooksEntity b ")
    long countAllBook();

    @Query("SELECT e FROM BooksEntity e " +
            "WHERE (COALESCE(:booksId, '0') = '0' OR e.id IN :booksId) " +
            "AND (COALESCE(:status, '0') = '0' OR e.status IN :status) " +
            "AND (:name IS NULL OR e.name LIKE %:name%) " +
            "AND (:author IS NULL OR e.author LIKE %:author%) ")
    Page<BooksEntity> findBooksWithRequest(List<String> booksId,
                                           String name,
                                           String author,
                                           List<String> status,
                                           Pageable pageable);

    @Query("SELECT e FROM BooksEntity e " +
            "WHERE (COALESCE(:booksId, '0') = '0' OR e.id IN :booksId) " +
            "AND (e.status = :status) " +
            "AND (:name IS NULL OR e.name LIKE %:name%) " +
            "AND (:author IS NULL OR e.author LIKE %:author%) ")
    Page<BooksEntity> findBooksActiveWithRequest(List<String> booksId,
                                                 String name,
                                                 String author,
                                                 String status,
                                                 Pageable pageable);
}
