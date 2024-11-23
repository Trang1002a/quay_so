package com.example.quay_so.model.repository;

import com.example.quay_so.model.entity.VotesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<VotesEntity, String> {

    @Query("SELECT e FROM VotesEntity e " +
            "WHERE ((COALESCE(:bookIds, '0') = '0' OR e.bookId IN :bookIds)) " +
            "AND ((COALESCE(:accountIds, '0') = '0' OR e.accountId IN :accountIds)) " +
            "AND ((COALESCE(:status, '0') = '0' OR e.status IN :status))")
    Page<VotesEntity> findVotesWithRequest(List<String> bookIds,
                                           List<String> accountIds,
                                           List<String> status,
                                           Pageable pageable);

    List<VotesEntity> findAllByBookIdAndStatus(String bookId, String status);

    List<VotesEntity> findAllByBookIdAndStatusOrderByCreatedAtAsc(String bookId, String status);
}
