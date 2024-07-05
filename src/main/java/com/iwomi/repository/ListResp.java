/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.repository;

import com.iwomi.model.Bank;
import com.iwomi.model.CamTrans;
import com.iwomi.model.NotList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
*/
@Repository
public interface ListResp extends JpaRepository<NotList, Long> {    
}