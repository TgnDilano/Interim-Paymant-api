/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import com.iwomi.model.AbstractModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author HP
 */

@Entity
@Table(name = "bks")

public class Bank extends AbstractModel<Long>{
    private AccInfo debit;
    
    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
        name = "bk_gpac",
        joinColumns = {@JoinColumn(name = "bk_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "gpac_id", referencedColumnName = "id")})
    private List<AccInfo> credit = new ArrayList<>();

    public AccInfo getDebit() { return debit; }
    public void setDebit(AccInfo value) { this.debit = value; }

    public List<AccInfo> getCredit() { return credit; }
    public void setCredit(List<AccInfo> value) { this.credit = value; }

    
    
}
