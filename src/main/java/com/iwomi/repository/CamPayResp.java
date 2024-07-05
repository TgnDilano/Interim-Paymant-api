/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.repository;

import java.util.List;
import com.iwomi.model.Bank;
import com.iwomi.model.CamTrans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface CamPayResp extends JpaRepository<CamTrans, Long> {

    @Query(value = "select * from camtrans left join cam_not on cam_not.cam_id = camtrans.id where bkpn = ?1 and not_id =5 ", nativeQuery = true)
    public List<CamTrans> testInteralquery(String s);

    public List<CamTrans> findBybankPaymentNumber(String s);

    public List<CamTrans> findAll();

}
