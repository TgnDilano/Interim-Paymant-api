/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.repository;

import com.iwomi.model.TransactionHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface TransHisRepository extends JpaRepository<TransactionHistory, Long> {

    @Query(value = "UPDATE thisty set status =?1 where id_reference = ?2", nativeQuery = true)
    public void updateSTatus(String status, String trans_key);
    
    List<TransactionHistory> findByIdReference(String referenceId);
    
    @Query(value = "SELECT * FROM thisty e WHERE e.id_reference = ?1",nativeQuery=true)
    TransactionHistory findByIdReferenceID(String referenceId);
    
    @Query(value = "SELECT * FROM thisty e WHERE e.external_id = ?1",nativeQuery=true)
    public TransactionHistory findByPnbr(String pnbr);
    
    @Query(value = "SELECT e.* FROM thisty e",nativeQuery=true)
    List<TransactionHistory> findall();
   
}
