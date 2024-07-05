/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

// import com.iwomi.model.partner.*;
// import com.iwomi.seriazable.PartnerId;
// import com.iwomi.seriazable.TransHistoryId;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author HP
 */
@Entity
@Table(name="transhismav")
//@IdClass(TransHistoryId.class)
public class MavTransHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique=true )
    
    private Long history_id;
    private String amount;
    private String fee;
    private String tva = "0";
    private String total_fee ;
    private String com;
    private Date date;
    private String time;
    private String partner_name;
    private String optype;
    private String motive;
    private String acc_num;
    private String pt_balance;
    private String iwomi_balance;
    private String external_id;
    @Column(name="transaction_id", unique=true )
    private String transaction_id;
    private String reference_id;
    private String counter_part;
    private String other;
    private String callback;
    private String success_url;
    private String failed_url;
    private String status;
    private String chl1;
    private String chl2;
    private String chl3;
    private String reason;
    private String dele;
    private String first_name;
    private String last_name;
    private String country;
    private String city;
    private String address;
    private String currency;
    private String email;
    private String source;
    private String message;
    private String fee_bearer;

    public MavTransHistory() {
    }

    public MavTransHistory( String amount, String fee, Date date, String time, 
            String partner_name, String optype, String motive, String acc_num, 
            String pt_balance, String iwomi_balance, String external_id, 
            String transaction_id, String referenceId, String counter_part, 
            String status, String dele) {
        this.amount = amount;
        this.fee = fee;
        this.date = date;
        this.time = time;
        this.partner_name = partner_name;
        this.optype = optype;
        this.motive = motive;
        this.acc_num = acc_num;
        this.pt_balance = pt_balance;
        this.iwomi_balance = iwomi_balance;
        this.external_id = external_id;
        this.transaction_id = transaction_id;
        this.reference_id = referenceId;
        this.counter_part = counter_part;
        this.status = status;
        this.dele = dele;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }
    

    public MavTransHistory(Long history_id, String amount, String fee, Date date, String time, String partner_name, String optype, String motive, String acc_num, String pt_balance, String iwomi_balance, String external_id, String transaction_id, String reference_id, String counter_part, String other, String status, String reason, String dele) {
        this.history_id = history_id;
        this.amount = amount;
        this.fee = fee;
        this.date = date;
        this.time = time;
        this.partner_name = partner_name;
        this.optype = optype;
        this.motive = motive;
        this.acc_num = acc_num;
        this.pt_balance = pt_balance;
        this.iwomi_balance = iwomi_balance;
        this.external_id = external_id;
        this.transaction_id = transaction_id;
        this.reference_id = reference_id;
        this.counter_part = counter_part;
        this.other = other;
        this.status = status;
        this.reason = reason;
        this.dele = dele;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFee_bearer() {
        return fee_bearer;
    }

    public void setFee_bearer(String fee_bearer) {
        this.fee_bearer = fee_bearer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
    
    public MavTransHistory(Long history_id, String amount, String fee, Date date, String time, String partner_name, String optype, String motive, String acc_num, String pt_balance, String iwomi_balance, String external_id, String transaction_id, String reference_id, String counter_part, String other, String status, String chl1, String chl2, String chl3, String reason, String dele) {
        this.history_id = history_id;
        this.amount = amount;
        this.fee = fee;
        this.date = date;
        this.time = time;
        this.partner_name = partner_name;
        this.optype = optype;
        this.motive = motive;
        this.acc_num = acc_num;
        this.pt_balance = pt_balance;
        this.iwomi_balance = iwomi_balance;
        this.external_id = external_id;
        this.transaction_id = transaction_id;
        this.reference_id = reference_id;
        this.counter_part = counter_part;
        this.other = other;
        this.status = status;
        this.chl1 = chl1;
        this.chl2 = chl2;
        this.chl3 = chl3;
        this.reason = reason;
        this.dele = dele;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTva() {
        return tva;
    }

    public void setTva(String tva) {
        this.tva = tva;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }
    
    
    
    
    public Long getHistory_id() {
        return history_id;
    }

    public void setHistory_id(Long history_id) {
        this.history_id = history_id;
    }

    public String getDele() {
        return dele;
    }

    public void setDele(String dele) {
        this.dele = dele;
    }

    public String getSuccess_url() {
        return success_url;
    }

    public void setSuccess_url(String success_url) {
        this.success_url = success_url;
    }

    public String getFailed_url() {
        return failed_url;
    }

    public void setFailed_url(String failed_url) {
        this.failed_url = failed_url;
    }
    
    
    

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }
    
    

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public Date getDate() {
        return date;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
    

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }

    public String getOptype() {
        return optype;
    }

    public void setOptype(String optype) {
        this.optype = optype;
    }

    public String getMotive() {
        return motive;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }

    public String getAcc_num() {
        return acc_num;
    }

    public void setAcc_num(String acc_num) {
        this.acc_num = acc_num;
    }

    public String getPt_balance() {
        return pt_balance;
    }

    public void setPt_balance(String pt_balance) {
        this.pt_balance = pt_balance;
    }

    public String getIwomi_balance() {
        return iwomi_balance;
    }

    public void setIwomi_balance(String iwomi_balance) {
        this.iwomi_balance = iwomi_balance;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getCounter_part() {
        return counter_part;
    }

    public void setCounter_part(String counter_part) {
        this.counter_part = counter_part;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChl1() {
        return chl1;
    }

    public void setChl1(String chl1) {
        this.chl1 = chl1;
    }

    public String getChl2() {
        return chl2;
    }

    public void setChl2(String chl2) {
        this.chl2 = chl2;
    }

    public String getChl3() {
        return chl3;
    }

    public void setChl3(String chl3) {
        this.chl3 = chl3;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }
    
    
    
}
