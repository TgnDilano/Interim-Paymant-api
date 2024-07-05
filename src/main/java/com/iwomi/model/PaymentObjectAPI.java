/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import com.iwomi.model.AbstractModel;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author HP
 */

@Entity
@Table(name = "gths")//Genral transaction history table
public class PaymentObjectAPI extends AbstractModel<Long>{
    private String ptn;//partner
    private String type;//type of operation
    private String total;//total amount
    private String tel;//tel number especially during momo
    private String motif;
    private String fees;
    private String pnbr;//processiing number
    private String global_id;
    
    @Transient
    private AccInfo ordrep;
    
    
@ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
        name = "gths_bks",
        joinColumns = {@JoinColumn(name = "gths_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "bks_id", referencedColumnName = "id")})
    private List<Bank> bank = new ArrayList<>();

    
    public String getPnbr() { return pnbr; }
    public void setPnbr(String value) { this.pnbr = value; }
    
    public String getPtn() { return ptn; }

    public PaymentObjectAPI(String partner, String type, String total, String tel, String motif, String fees, String processingNumber, List<Bank> bank) {
        this.ptn = partner;
        this.type = type;
        this.total = total;
        this.tel = tel;
        this.motif = motif;
        this.fees = fees;
        this.pnbr = processingNumber;
        this.bank = bank;
    }

    public PaymentObjectAPI(String ptn, String type, String total, String tel, String motif, String fees, String pnbr, String global_id, AccInfo ordrep) {
        this.ptn = ptn;
        this.type = type;
        this.total = total;
        this.tel = tel;
        this.motif = motif;
        this.fees = fees;
        this.pnbr = pnbr;
        this.global_id = global_id;
        this.ordrep = ordrep;
    }
    
    

    public AccInfo getOrdrep() {
        return ordrep;
    }

    public void setOrdrep(AccInfo ordrep) {
        this.ordrep = ordrep;
    }
    
    

    public String getGlobal_id() {
        return global_id;
    }

    public void setGlobal_id(String global_id) {
        this.global_id = global_id;
    }

    public PaymentObjectAPI(String ptn, String type, String total, String tel, String motif, String fees, String pnbr, String global_id) {
        this.ptn = ptn;
        this.type = type;
        this.total = total;
        this.tel = tel;
        this.motif = motif;
        this.fees = fees;
        this.pnbr = pnbr;
        this.global_id = global_id;
    }
    
    

    public PaymentObjectAPI() {
    }
    public void setPtn(String value) { this.ptn = value; }

    public String getType() { return type; }
    public void setType(String value) { this.type = value; }

    public String getTotal() { return total; }
    public void setTotal(String value) { this.total = value; }

    public String getTel() { return tel; }
    public void setTel(String value) { this.tel = value; }

    public String getMotif() { return motif; }
    public void setMotif(String value) { this.motif = value; }

    public String getFees() { return fees; }
    public void setFees(String value) { this.fees = value; }

    public List<Bank> getBank() { return bank; }
    public void setBank(List<Bank> value) { this.bank = value; }
}
