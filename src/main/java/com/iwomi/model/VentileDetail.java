/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Tebit Roger
 */
@Entity
@Table(name = "vtdtl")
public class VentileDetail  extends AbstractModel<Long> {
     @Column(name = "cdebf")
    private String codeBeneficiair;
      @Column(name = "rstrm")
    private String resultTraitm;

    public String getCodeBeneficiair() {
        return codeBeneficiair;
    }

    public void setCodeBeneficiair(String codeBeneficiair) {
        this.codeBeneficiair = codeBeneficiair;
    }

    public String getResultTraitm() {
        return resultTraitm;
    }

    public void setResultTraitm(String resultTraitm) {
        this.resultTraitm = resultTraitm;
    }

    public VentileDetail() {
    }
}
