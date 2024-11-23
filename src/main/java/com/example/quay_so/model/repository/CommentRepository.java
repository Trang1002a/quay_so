package com.bookstore.quay_so.model.repository;

import com.example.quay_so.model.entity.CommentsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentsEntity, String> {
    @Query("SELECT e FROM CommentsEntity e " +
            "WHERE (COALESCE(:bookIds, '0') = '0' OR e.bookId IN :bookIds) " +
            "AND (COALESCE(:accountIds, '0') = '0' OR e.accountId IN :accountIds) " +
            "AND (COALESCE(:status, '0') = '0' OR e.status IN :status)")
    Page<CommentsEntity> findCommentsWithRequest(List<String> bookIds,
                                                 List<String> accountIds,
                                                 List<String> status,
                                                 Pageable pageable);

    Optional<CommentsEntity> findByIdAndStatusAndParentIdIsNull(String id, String status);

    List<CommentsEntity> findAllByBookIdAndStatusOrderByCreatedAtAsc(String id, String status);
}
