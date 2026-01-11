package com.myapp.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.account.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByCustomerId(Integer customerId);
    boolean existsByNumber(String number);
}
