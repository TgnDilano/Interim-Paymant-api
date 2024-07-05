/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.repository;

import com.iwomi.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author HP
 */
public interface UserAdminRepository extends JpaRepository<User, Long> {
    
     @Query(value = "SELECT count(*) FROM sause e WHERE e.brch = ?1",nativeQuery=true)
      public  int findNbre(String brch);
       @Query(value = "SELECT * FROM sause e WHERE  e.phone=?1",nativeQuery=true)
      public  User  findByUname1(String phone);
      
       @Query(value = "SELECT * FROM sause e WHERE e.cetab = ?1 and e.uname=?2 and e.passw=?3 and e.nfac=?4",nativeQuery=true)
      public  User  findUserByUsAndPwd1(String cetab,String username, String pasword,String nfac);
      
      @Query(value = "SELECT * FROM sause e WHERE e.cetab = ?1 and e.nfac!=?2 and e.sttus=?3 ",nativeQuery=true)
      public  List<User>  findUserAll(String cetab,String nfac,String dele);
      
       @Query(value = "SELECT * FROM sause e WHERE e.cetab = ?1 and e.nfac=?2 and e.uname=?3 and e.sttus=?4 ",nativeQuery=true)
      public  User  finduserByEtabNf1(String cetab,String nfac,String uname,String dele);
      
      @Query(value = "SELECT * FROM sause e WHERE e.cetab = ?1 and e.nfac=?2 and e.sttus=?3 ",nativeQuery=true)
      public  List<User>  findUserAllNfac(String cetab,String nfac,String dele);
      
            
      @Query(value = "SELECT * FROM sause e WHERE e.uname=?1 and e.passw=?2",nativeQuery=true)
      public  User  findUserByNameAndPassword(String username, String pasword);
      
     
    
}
