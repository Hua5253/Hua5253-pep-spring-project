package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.IncorrectPasswordException;
import com.example.exception.InvalidPasswordException;
import com.example.exception.InvalidUsernameException;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.exception.UsernameNotExistException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        // username is empty.
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) 
            throw new InvalidUsernameException("Username cannot be empty");
        
        // Password is too short
        if (account.getPassword() == null || account.getPassword().length() < 4) 
            throw new InvalidPasswordException("Password must be at least 4 characters long");
        
        // Username already exists
        Account accountInDatabase = accountInDataBase(account.getUsername());
        if(accountInDatabase != null) 
            throw new UsernameAlreadyExistsException("Username already exists");

        return accountRepository.save(account);
    }

    public Account varifyLogin(Account account) {
        Account accountInDatabase = accountInDataBase(account.getUsername());
        // username the client entered doesn't exist
        if(accountInDatabase == null) throw new UsernameNotExistException("Username not exist");
        // password the client entered wrong;
        if(!accountInDatabase.getPassword().equals(account.getPassword()))
            throw new IncorrectPasswordException("wrong password");

        return accountInDatabase;
    }

    private Account accountInDataBase(String username) {
        return accountRepository.findByUsername(username).orElse(null);
    }
}
