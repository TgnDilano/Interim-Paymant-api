package com.iwomi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="temp_trans")
public class MavTempTrans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique=true )
    private Long id;
    private String ptn;
    private String timestamp1;
    private String agent_balance;
    private String receipt_number;
    private String veri_code;
    private String status;
    private String price_local_cur;

    private String price_system_cur;

    private String local_cur;
    private String system_cur;
    @Column(name="trid", unique=true )
    private String trid;
    private String pin;
    private String pay_item_id;
    private String pay_item_descr;
    private String tag;
    private String dele;

    private String chl1;
    private String chl2;
    private String chl3;

//general constructor
     
    
    
    //empty constructor

    public MavTempTrans() {
        
    }

    public MavTempTrans(String ptn, String timestamp, String agent_balance, String receipt_number, String veri_code,
            String status, String price_local_cur, String price_system_cur, String local_cur, String system_cur,
            String trid, String pin, String pay_item_id, String pay_item_descr, String tag, String dele, String chl1,
            String chl2, String chl3) {
        this.ptn = ptn;
        this.timestamp1 = timestamp;
        this.agent_balance = agent_balance;
        this.receipt_number = receipt_number;
        this.veri_code = veri_code;
        this.status = status;
        this.price_local_cur = price_local_cur;
        this.price_system_cur = price_system_cur;
        this.local_cur = local_cur;
        this.system_cur = system_cur;
        this.trid = trid;
        this.pin = pin;
        this.pay_item_id = pay_item_id;
        this.pay_item_descr = pay_item_descr;
        this.tag = tag;
        this.dele = dele;
        this.chl1 = chl1;
        this.chl2 = chl2;
        this.chl3 = chl3;
    }


    //getters

    public String getPtn() {
        return ptn;
    }

    public String getTimestamp1() {
        return timestamp1;
    }

    public String getAgent_balance() {
        return agent_balance;
    }

    public String getReceipt_number() {
        return receipt_number;
    }

    public String getVeri_code() {
        return veri_code;
    }

    public String getStatus() {
        return status;
    }

    public String getPrice_local_cur() {
        return price_local_cur;
    }

    public String getPrice_system_cur() {
        return price_system_cur;
    }

    public String getLocal_cur() {
        return local_cur;
    }

    public String getSystem_cur() {
        return system_cur;
    }

    public String getTrid() {
        return trid;
    }

    public String getPin() {
        return pin;
    }

    public String getPay_item_id() {
        return pay_item_id;
    }

    public String getPay_item_descr() {
        return pay_item_descr;
    }

    public String getTag() {
        return tag;
    }

    public String getDele() {
        return dele;
    }

    public String getChl1() {
        return chl1;
    }

    public String getChl2() {
        return chl2;
    }

    public String getChl3() {
        return chl3;
    }

    //setters

    public void setPtn(String ptn) {
        this.ptn = ptn;
    }

    public void setTimestamp1(String timestamp) {
        this.timestamp1 = timestamp;
    }

    public void setAgent_balance(String agent_balance) {
        this.agent_balance = agent_balance;
    }

    public void setReceipt_number(String receipt_number) {
        this.receipt_number = receipt_number;
    }

    public void setVeri_code(String veri_code) {
        this.veri_code = veri_code;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrice_local_cur(String price_local_cur) {
        this.price_local_cur = price_local_cur;
    }

    public void setPrice_system_cur(String price_system_cur) {
        this.price_system_cur = price_system_cur;
    }

    public void setLocal_cur(String local_cur) {
        this.local_cur = local_cur;
    }

    public void setSystem_cur(String system_cur) {
        this.system_cur = system_cur;
    }

    public void setTrid(String trid) {
        this.trid = trid;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setPay_item_id(String pay_item_id) {
        this.pay_item_id = pay_item_id;
    }

    public void setPay_item_descr(String pay_item_descr) {
        this.pay_item_descr = pay_item_descr;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setDele(String dele) {
        this.dele = dele;
    }

    public void setChl1(String chl1) {
        this.chl1 = chl1;
    }

    public void setChl2(String chl2) {
        this.chl2 = chl2;
    }

    public void setChl3(String chl3) {
        this.chl3 = chl3;
    }

    


    
    
    

    

    

    

    
    
}
