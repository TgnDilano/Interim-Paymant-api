/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.repository;

import com.iwomi.model.SystemProp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface SystePropRepository extends JpaRepository<SystemProp, Long> {
 List<SystemProp> findByCode(String s);  
 	@Query(value = "SELECT e.* FROM mtn_info e", nativeQuery = true)
	List<SystemProp> findall();
}
