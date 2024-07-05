/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.repository;

import com.iwomi.model.PaymentObjectAPI;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */

@Repository
public interface BicecTransDetialsResp extends JpaRepository<PaymentObjectAPI, Long> {

    @Query(value = "SELECT * FROM gths e WHERE e.pnbr = ?1",nativeQuery=true)
    public PaymentObjectAPI findAllByPnbr(String pnbr);

    
}
