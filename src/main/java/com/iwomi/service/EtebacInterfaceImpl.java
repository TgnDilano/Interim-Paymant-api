/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.service;

import com.iwomi.model.PaymentObjectAPI;
import com.iwomi.serviceInterface.EtebacInterface;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author HP
 */
public class EtebacInterfaceImpl implements EtebacInterface{

    @Override
    public Map<String, String> performOpertaion(PaymentObjectAPI paymentObjectApi) {
        Map<String, String> response = new HashMap();
        //Extract generate and mount the files on Amplitude
        
        return response;
    }
    
}
