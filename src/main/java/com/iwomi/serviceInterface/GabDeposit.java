/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.serviceInterface;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author HP
 */
public interface GabDeposit {
    public String gabDepos(String pnbr,String patner,JSONObject obj, JSONArray arr,Map<Integer, String> iso8583ToMap) throws SftpException, IOException, JSONException, InterruptedException, JSchException, SQLException, ClassNotFoundException;
    
}
