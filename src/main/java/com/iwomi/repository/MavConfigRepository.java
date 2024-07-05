
package com.iwomi.repository;

import com.iwomi.model.MavianceAccessConfig;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MavConfigRepository extends JpaRepository<MavianceAccessConfig, Long>, JpaSpecificationExecutor {
 
 
 @Query(value = "SELECT * FROM mav_config e where e.type =?1", nativeQuery = true)
    public MavianceAccessConfig findByType(String s);
}

