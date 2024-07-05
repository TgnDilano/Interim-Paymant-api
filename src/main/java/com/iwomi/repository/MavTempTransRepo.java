package com.iwomi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iwomi.model.MavTempTrans;



@Repository
public interface MavTempTransRepo extends CrudRepository<MavTempTrans, Long>,JpaSpecificationExecutor {

     @Query(value = "select * from temp_trans  where dele = ?1 ORDER BY timestamp DESC", nativeQuery = true)
    public List<MavTempTrans> findAllTempTrans(String dele);

    @Query(value = "select * from temp_trans  where trid = ?1", nativeQuery = true)
    public MavTempTrans findByTrid(String trid);


    
}
