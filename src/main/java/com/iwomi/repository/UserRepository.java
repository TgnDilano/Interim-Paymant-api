package com.iwomi.repository;
import com.iwomi.model.AuthorizedAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<AuthorizedAccounts, Integer> {
    AuthorizedAccounts findByName(String username);
    
    
    

}