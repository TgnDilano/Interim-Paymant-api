/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.repository;

import com.iwomi.model.Nomenclature;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author User
 */
public interface NomenclatureRepository extends JpaRepository<Nomenclature, Long>{
    
    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd ='0012' and dele=0",nativeQuery=true)
    public  List<Nomenclature> findInNomenclature();
    
    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.acscd=?2 and e.dele=?3",nativeQuery=true)
    public  Nomenclature  findUrl1(String tabcd,String acscd, String dele);
        
    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd ='5011' and dele=0",nativeQuery=true)
    public  Nomenclature findInNomenclatureEmail();

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.acscd=?2 and e.dele=?3",nativeQuery=true)
    public Nomenclature findTabcdAndAcsd(String string, String access_code_patner, String string0);
    
    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.dele=?2",nativeQuery=true)
    public List<Nomenclature> findTabcdAndDel(String string, String string0);
    
    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.lib5=?2 and e.dele=?3",nativeQuery=true)
    public List<Nomenclature> findTabcdAndLib5AndDel(String tabcd, String code_cpt, String string);
    
}
