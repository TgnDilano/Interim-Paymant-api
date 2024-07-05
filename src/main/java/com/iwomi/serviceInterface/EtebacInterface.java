/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.serviceInterface;

import com.iwomi.model.PaymentObjectAPI;
import java.util.Map;

/**
 *
 * @author HP
 */
public interface EtebacInterface {
        public Map<String, String> performOpertaion(PaymentObjectAPI paymentObjectApi);

    
}
