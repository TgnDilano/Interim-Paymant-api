/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.repository;


import com.iwomi.model.MavTransHistory;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface MavTransHistoryRepository extends CrudRepository<MavTransHistory, Long>,JpaSpecificationExecutor{
    
    @Query(value = "select * from transhismav  where partner_name = ?1 ORDER BY date DESC", nativeQuery = true)
    public  List<MavTransHistory> findBypartner_name(String username);
    
    @Query(value = "UPDATE transhismav set status =?1 where external_id = ?2", nativeQuery = true)
    public void updateSTatus(String status, String trans_key);
    
    @Query(value = "select * from transhismav  where external_id = ?1 and partner_name = ?2 ORDER BY date DESC", nativeQuery = true)
    public List<MavTransHistory> findByExternalIdUsername(String externalId, String username);
    
    @Query(value = "select * from transhismav  where external_id = ?1", nativeQuery = true)
    public MavTransHistory findByExternalId(String externalId);
    
    @Query(value = "select * from transhismav  where transaction_id = ?1", nativeQuery = true)
    public MavTransHistory findByTrans_internal(String transaction_id);
    
    @Query(value = "select * from transhismav  where reference_id = ?1", nativeQuery = true)
    public MavTransHistory findByIdReference(String reference_id);
    @Query(value = "select * from transhismav  where dele = ?1 ORDER BY date DESC", nativeQuery = true)
    public List<MavTransHistory> findAllHistory(String dele);
    
    @Query(value = "select * from transhismav  where partner_name = ?1 ORDER BY date DESC", nativeQuery = true)
    public  List<MavTransHistory> findBypartner_nameChart(String username );
    
    @Query(value = "select * from transhismav  where partner_name = ?1 and date <= ?3 and date >= ?2 ORDER BY date DESC", nativeQuery = true)
    public  List<MavTransHistory> findBypartner_nameAndDateRange(String username, Date st_date,Date en_date );
    
    @Query(value = "select * from transhismav  where date <= ?2 and date >= ?1 ORDER BY date DESC", nativeQuery = true)
    public  List<MavTransHistory> findByDateRange( Date st_date,Date en_date );
    
    @Query(value = "select * from transhismav  where acc_num = ?1 ORDER BY date DESC", nativeQuery = true)
    public List<MavTransHistory> findByAccNum(String accNum);
    
    @Query(value = "select * from transhismav  where optype = ?1 ORDER BY date DESC", nativeQuery = true)
    public List<MavTransHistory> findByOptype(String Optype);
    
    @Query(value = "select * from transhismav  where date <= ?2 and date >= ?1 and optype like ?3% ORDER BY date DESC", nativeQuery = true)
    public  List<MavTransHistory> findByDateRangePaym( Date st_date,Date en_date, String paym );
    
    @Query(value = "select * from transhismav  where acc_num = ?1 or acc_num = ?2", nativeQuery = true)
    public List<MavTransHistory> findHisByAccs(String acc1, String acc2);
}
