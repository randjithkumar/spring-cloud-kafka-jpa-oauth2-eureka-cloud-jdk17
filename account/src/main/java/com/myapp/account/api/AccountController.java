package com.myapp.account.api;

import com.myapp.account.model.Account;
import com.myapp.account.repository.AccountRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    
    private final AccountRepository accountRepository;
    
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Integer id) {
        return accountRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(accountRepository.save(account));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Integer id, 
                                               @Valid @RequestBody Account account) {
        return accountRepository.findById(id)
            .map(existing -> {
                account.setId(id);
                return ResponseEntity.ok(accountRepository.save(account));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
