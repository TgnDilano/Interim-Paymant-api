/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.repository;

import com.iwomi.model.Pwd;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author HP
 */

public interface PwdRepository extends CrudRepository<Pwd, Integer>{

    @Query(value = "SELECT * FROM pwd e WHERE e.acscd = ?1  and e.dele= ?2 ",nativeQuery=true)
    public Pwd findByAcscd(String acscd, String del);
    
}