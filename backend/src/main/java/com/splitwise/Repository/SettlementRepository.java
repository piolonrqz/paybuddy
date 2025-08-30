package com.splitwise.Repository;

import com.splitwise.Entities.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    @Query("SELECT s FROM Settlement s WHERE s.fromUser.id = :userId")
    List<Settlement> findPaidById(@Param("userId") Long userId);

    @Query("SELECT s FROM Settlement s WHERE s.toUser.id = :userId")
    List<Settlement> findPaidToId(@Param("userId") Long userId);
}
