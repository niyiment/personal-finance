package com.niyiment.finance.repository;

import com.niyiment.finance.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);

    List<Account> findByUserIdAndIsActiveTrue(Long userId);
}
