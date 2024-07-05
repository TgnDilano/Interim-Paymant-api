package com.iwomi.repository;


import com.iwomi.model.MavianceMerchants;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface MavianceMerchantRepository extends CrudRepository<MavianceMerchants, Long>,JpaSpecificationExecutor {

    @Query(value = "select * from mav_merchant  where merchant = ?1", nativeQuery = true)
    public MavianceMerchants findByMerchantCode(String merchant);

     @Query(value = "select * from mav_merchant ", nativeQuery = true)
    public List <MavianceMerchants> findAllMerchants();
    
}
