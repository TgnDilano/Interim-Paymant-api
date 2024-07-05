/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.repository;

import com.iwomi.model.OmSystemConfig;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author HP
 */
public interface OmSystemConfigResp extends JpaRepository<OmSystemConfig, Long>{
    
    @Query(value = "SELECT * FROM orange_info e where e.code =?1", nativeQuery = true)
    public OmSystemConfig findByCode(String s);
    
}
