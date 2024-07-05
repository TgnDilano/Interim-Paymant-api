/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.repository;

import com.iwomi.model.CrescoApiCalls;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author HP
 */

public interface CrescoApiCallsRepo extends CrudRepository<CrescoApiCalls, Long>{

    @Query(value = "SELECT * FROM crescoapi e WHERE e.token = ?1",nativeQuery=true)
    public CrescoApiCalls findByToken(String token);
    
}