/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi;

import com.iwomi.model.Nomenclature;
import com.iwomi.model.Pwd;
import com.iwomi.repository.NomenclatureRepository;
import com.iwomi.repository.PwdRepository;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import static java.lang.Thread.sleep;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import static org.apache.commons.lang3.StringUtils.trim;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author IWOMI
 */

@Service
public class DataBase {
    Connection connection,con = null;
    String url = "jdbc:oracle:thin:@10.100.30.35:1521:stockv10";
    String login ="amphom";
    String password ="amphom";
    String COMPTE_TVA,COMPTE_COMMISSION,TAUX_TVA;
    String date = "";
    String SFTPUSER2 = "";
    String MachineIP2 = "";
    int SFTPPORT2 = 22;
    String SFTPPASS2 = "";
    String SFTPWORKINGDIR_EXACT =" ";
    String NOMENGABTABLE ="5001";
    String NOMENGABTABLE_SYS ="0012";
    String NOMENGABTABLE_TAUX ="5006";
    String NOMENGABTABLE_Produit ="5007";
    String NOMENGABTABLE_NATURE ="5000";
    String PAYMENT_FACTURE_PROCESSING_CODE ="50";
    String PAYMENT_COMMERCANT_PROCESSING_CODE = "52";
    String SFTPWORKINGDIR,FIC_ERR,FIC_ARC,S2M_PREF;
    String AMPLITUDE_IP = "";
    String NBRE_MAX_PIN = "";
    public static final String USERNAME="root";
    public static final String PASSWORD="";
    public static final String CONN_STRING="jdbc:mysql://localhost/bicec";
    Map<String, String> FOR_STAT = new HashMap<String, String>();
    Session     session     = null;
    Channel     channel     = null;
    ChannelSftp channelSftp = null;
    String LOGIN_ORACLE = "";
    String PASSWORD_ORACLE = "";
    String URL_ORACLE = "";
    String[] ORACLE_CON_PARAM = new String[]{}; 
    String COMPTE_TVA_GIMAC = "";
    String COMPTE_COMMISSION_GIMAC="";
    String COMPTE_VIREMENT_GIMAC="";
    String TEMP_ACCOUNT ="";
    String COMMISSION = "";
    String COMMISSION_GIMAC = "";
    String COMPTE_GIMAC_PAY = "";
    String RETRAIT_PROCESSING_CODE="01";
    String VIREMENT_PROCESSING_CODE="40";
    String REMITTANCE_PROCESSING_CODE="43";
    private String viii="";
    
    @Autowired
    NomenclatureRepository nomenclatureRepository;
    Nomenclature nomenclature = null;
    
        @Autowired
    PwdRepository pwdRepository;
        
        @Autowired
        MyUtil util;
    
    
    public String getSchema(){
        nomenclature = nomenclatureRepository.findTabcdAndAcsd("0012","0123","0");
        if (nomenclature != null){
            return nomenclature.getLib2();
        }
        return "bank";
    }
    
    public String DateValDate() throws SQLException, ClassNotFoundException {     
        ORACLE_CON_PARAM = getOracleCon(); 
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        try {
           // connection = DriverManager.getConnection(url, login, password);
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs =null;
        String query = "";
//        if(type == "0"){
//            query = "select DISTINCT SDE,SIN FROM bkcom ";
//        }else{
//            query = "select DISTINCT SDE,SIN FROM bkcom ";
//        }
        
        query = "select to_date((SUBSTR('000'|| (CASE WHEN mnt2 = 0\n" +
            "THEN mnt1 ELSE mnt2 END),-8,8) )\n" +
            ", 'DDMMYYYY') dco from "+ getSchema()+".bknom where ctab = '001' and cacc = '99000'";
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                date = rs.getString("dco");
            }
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
        } finally {
//            if (stmt != null) { stmt.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }
        
        connection.close();
        if (connection.isClosed())System.out.println("Connection 1 closed.");
        return date;
    }
    
    public String DateVal() throws SQLException, ClassNotFoundException {     
        ORACLE_CON_PARAM = getOracleCon();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        try {
           // connection = DriverManager.getConnection(url, login, password);
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs =null;
        String query = "";
//        if(type == "0"){
//            query = "select DISTINCT SDE,SIN FROM bkcom ";
//        }else{
//            query = "select DISTINCT SDE,SIN FROM bkcom ";
//        }
        
        query = "select (SUBSTR('000'|| (CASE WHEN mnt2 = 0\n" +
            "THEN mnt1 ELSE mnt2 END),-8,8) )\n" +
            " dco from "+ getSchema()+".bknom where ctab = '001' and cacc = '99000'";
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                date = rs.getString("dco");
            }
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
        } finally {
//            if (stmt != null) { stmt.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }
        
        connection.close();
        if (connection.isClosed())System.out.println("Connection 1 closed.");
        return date;
    }
         
    public JSONObject checkstatus(String code) throws SQLException, JSchException, ClassNotFoundException, SftpException, InterruptedException, JSONException{
       
        try {
            ORACLE_CON_PARAM = getOracleCon();
        } catch (JSONException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        JSONObject obj = new JSONObject();
        Statement stmt = null;
        Statement stm3 = null;
        ResultSet sys1 = null;
        ResultSet rs1 = null;
        ResultSet rs = null;
        String status = "";
        Class.forName("com.mysql.jdbc.Driver");
        try {
            con = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
        } catch (SQLException ex) {
//                Logger.getLogger(MonThread1.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }


        try {
//            connection = DriverManager.getConnection(url, login, password);
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        try {
            con = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
        } catch (SQLException ex) {
//                Logger.getLogger(MonThread1.class.getName()).log(Level.SEVERE, null, ex);
        }
        String st= "SELECT * FROM sanm WHERE  tabcd='0012' and dele='0'";
        List<Nomenclature> nomen = nomenclatureRepository.findTabcdAndDel("0012", "0");
//        String query = "select 1 from bkdvir where fich = 'FIC160"+code+"'";
        String query = "select distinct (case when coalesce(eta,'EMPTY') in ('VA','VF','FO') then '0' "
                + "when coalesce(eta,'EMPTY') in ('AT') then '1' when coalesce(eta,'EMPTY') in ('IG','AB') then '2' else '3' end)res "
                + "from "+getSchema()+".bkeve where ndos=(select distinct refi from "+getSchema()+". bkdvir where trim(fich) like '"+code+"')";
        
        
        
        try {
            stm3 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            sys1 = stm3.executeQuery(st);
            while(sys1.next()){
                if(sys1.getString("acscd").equalsIgnoreCase("0014")){
                    SFTPWORKINGDIR=sys1.getString("lib2");  // Le chemin du repsertoire
                }
                if(sys1.getString("acscd").equalsIgnoreCase("0017")){
                    S2M_PREF = sys1.getString("lib2"); // le Prefixe du nom du fichier+le corps du nom
                }
                if(sys1.getString("acscd").equalsIgnoreCase("0026")){
                    FIC_ERR=sys1.getString("lib2");  // SFTP SFTPPORT2 GAB
                } 
                if(sys1.getString("acscd").equalsIgnoreCase("0027")){
                    FIC_ARC=sys1.getString("lib2");  // SFTP SFTPPORT2 GAB
                } 
                if(sys1.getString("acscd").equalsIgnoreCase("0019")){
                    MachineIP2=sys1.getString("lib2");   //ip adresse
                }
                if(sys1.getString("acscd").equalsIgnoreCase("0020")){
                    SFTPUSER2=sys1.getString("lib2");  // SFTP USER
                }
                if(sys1.getString("acscd").equalsIgnoreCase("0022")){
                    SFTPPORT2 = Integer.parseInt(sys1.getString("lib2"));  //SFTP PORT 
                }
                 if(sys1.getString("acscd").equalsIgnoreCase("0021")){
                    SFTPPASS2=sys1.getString("lib2");  // SFTP PASSWORD
                } 
            }
            FOR_STAT.put("SFTPUSER2", SFTPUSER2);
            FOR_STAT.put("MachineIP2", MachineIP2);
            FOR_STAT.put("SFTPPORT2", String.valueOf(SFTPPORT2));
            FOR_STAT.put("SFTPPASS2", SFTPPASS2);
            FOR_STAT.put("SFTPWORKINGDIR", SFTPWORKINGDIR);
            FOR_STAT.put("FIC_ERR", FIC_ERR);
            FOR_STAT.put("FIC_ARC", FIC_ARC);
            FOR_STAT.put("S2M_PREF", S2M_PREF);
            String vr= getFiles(code,FOR_STAT);
//            String vr ="ok";
            if(vr.equalsIgnoreCase("1000")){
                status = "1000";
                obj.put("status", status);
                obj.put("message", "Encours de traitement dans le repertoire de traitement.");
            }else if(vr.equalsIgnoreCase("100")){
                status = "112";
                obj.put("status", status);
                obj.put("message", "Echec dans le repertoire des erreurs");
            }else{
                try {
                    stmt = connection.createStatement();
                    rs = stmt.executeQuery(query);
                    if(rs.next()){
                        if(rs.getString("res").equals("0")){
                            status = "01";
                            obj.put("status", status);
                            obj.put("message", "Success");
                        }else if(rs.getString("res").equals("1")){
                            status = "113";  // forcage gestionnaire on arrete  le checkstatus
                            obj.put("status", status);
                            obj.put("message", "forçage gestionnaire");
                        }else if (rs.getString("res").equals("2")){
                            status = "113";
                            obj.put("status", status);
                            obj.put("message", "Echec : Abandonnée(AB) ou ignorée (IG)");
                        }else{
                            
//                            String query1 ="select max (case when ctr='0' then 'Pending Integration' when ctr='9' "
//                                    + "then 'Event Generate' when ctr='1' then 'ANOMALY - '||libano else (case when "
//                                    + "(select distinct refi from bkrejetb where lib1=a.fich) is not null then 'OPERATION REJETEE' else 'ERROR' end)  "
//                                    + "end)res from bkdvir a where fich  = 'FIC160"+code+"'";
                             String query1 ="select max (case when ctr='0' then 'Pending Integration' when ctr='9' "
                                    + "then 'Event Generate' when ctr='1' then 'ANOMALY - '||libano else (case when "
                                    + "(select distinct refi from bkrejetb where lib1=a.fich) is not null then 'OPERATION REJETEE' else 'ERROR' end)  "
                                    + "end)res from bkdvir a where fich  = "+code+"'";
                            try {
                                stmt = connection.createStatement();
                                rs1 = stmt.executeQuery(query1);

                                if(rs1.next()){
                                    if(rs1.getString("res").equalsIgnoreCase("Pending Integration")){
                                        status = "1001";
                                        obj.put("status", status);
                                        obj.put("message", "Encours d'integration");
                                    }else if(rs1.getString("res").equalsIgnoreCase("Event Generate")){
                                        status = "1002";
                                        obj.put("status", status);
                                        obj.put("message", "Evèenement généré.");
                                    }else if(rs1.getString("res").contains("ANOMALY - ")){
                                        status = "114";
                                        obj.put("status", status);   // ANOMALIE GENERATION DES EVENEMENTS-llabel de l anomalie.
                                        obj.put("message", rs1.getString("res"));
                                    }else if(rs1.getString("res").equalsIgnoreCase("OPERATION REJETEE")){
                                        status = "115";
                                        obj.put("status", status);
                                        obj.put("message", "OPERATION REJETEE");
                                    }else if(rs1.getString("res").equalsIgnoreCase("Erreur")){
                                        status = "116";
                                        obj.put("status", status);
                                        obj.put("message","Erreur");
                                    }else{
                                        status = "117";
                                        obj.put("status", status);
                                        obj.put("message","Erreurs");
                                    }
                                }else{
                                    status = "118";
                                    obj.put("status", status);
                                    obj.put("message","No results found");
                                }
                            } catch (SQLException e ) {System.out.println("la requÃªte n'a pas marchÃ© pour query 1");
                    //                JDBCTutorialUtilities.printSQLException(e);
                                status = "119";
                                obj.put("status", status);
                                obj.put("message", "No result");
                            } finally {
//                                if (stmt != null) { stmt.close(); }
                                try { stmt.close(); } catch (Exception e) { /* ignored */ }
                                try { rs1.close(); } catch (Exception e) { /* ignored */ }
                            } 

                        }

                    }else{
                        status = "1000";
                        obj.put("status", status);
                        obj.put("message", "Aucune donnée trouvée");

                    }
                } catch (SQLException e ) {System.out.println("la requÃªte n'a pas marchÃ© pour query");
                    status = "121";
                    obj.put("status", status);
                    obj.put("message", "Erreur   base de donnée.");
                } finally {
//                    if (stmt != null) { stmt.close(); }
                    try { stmt.close(); } catch (Exception e) { /* ignored */ }
                    try { rs.close(); } catch (Exception e) { /* ignored */ }
                }
            }
        } catch (SQLException e ) {System.out.println("la requete n'a pas marche pour query de la vÃ©rification du fichier");
            status = "122";
            obj.put("status", status);
            obj.put("message", "Erreur de requête");
        } finally {
//            if (stm3 != null) { stm3.close(); }
            try { stm3.close();if (stm3.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { sys1.close();if (sys1.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }   
        con.close();
        connection.close();
        if (connection.isClosed())System.out.println("Connection 1 closed.");
        if (con.isClosed())System.out.println("Connection 2 closed.");
        return obj;
    }
    
//    
    public String getFiles(String code,Map<String, String> FOR_STAT) throws JSchException, SftpException{
        
        JSch jsch = new JSch();
        session = jsch.getSession(FOR_STAT.get("SFTPUSER2"),FOR_STAT.get("MachineIP2"),Integer.parseInt(FOR_STAT.get("SFTPPORT2")));
        session.setPassword(FOR_STAT.get("SFTPPASS2"));
        System.out.println(FOR_STAT.get("SFTPUSER2")+'-'+FOR_STAT.get("MachineIP2")+'-'+Integer.parseInt(FOR_STAT.get("SFTPPORT2"))+'-'
        +FOR_STAT.get("SFTPPASS2"));
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        channel = session.openChannel("sftp");
        channel.connect();
        channelSftp = (ChannelSftp)channel;
        channelSftp.cd(FOR_STAT.get("SFTPWORKINGDIR"));
        //Vector filelist = channelSftp.ls(SFTPWORKINGDIR+S2M_PREF+"*");
       // Vector filelist = channelSftp.ls(FOR_STAT.get("SFTPWORKINGDIR")+FOR_STAT.get("S2M_PREF")+code);
        Vector fileList =channelSftp.ls(FOR_STAT.get("SFTPWORKINGDIR"));
        System.out.println("FIND THE NAMES OF FILES "+ code);
        System.out.println("VERIFY IF THE FILE IS IN THE PENDING FOLDER");
        int cnt=0;
        for(int z=0; z<fileList.size();z++){
             
                String[] data = fileList.get(z).toString().split(" ", -1);
                for (String filename : data){
                    
                    if(filename.contains(code)){
        System.out.println("RESPONSE YES.................................");
                        return "1000";
                    }
                    
                }
        }
     
       fileList = channelSftp.ls(FOR_STAT.get("FIC_ERR"));
       System.out.println("VERIFY IF THE FILE IS IN THE ERROR FOLDER"+FOR_STAT.get("FIC_ERR"));
       for(int z=0; z<fileList.size();z++){
             
                String[] data = fileList.get(z).toString().split(" ", -1);
                for (String filename : data){
                    
                    if(filename.contains(code)){
        System.out.println("RESPONSE YES.................................");
                        return "100";
                    }
                    
                }
        }
        System.out.println("RESPONSE NO.................................");
       fileList = channelSftp.ls(FOR_STAT.get("FIC_ARC"));
       System.out.println("VERIFY IF THE FILE IS IN THE ARCHIVE FOLDER");
       for(int z=0; z<fileList.size();z++){
             
                String[] data = fileList.get(z).toString().split(" ", -1);
                for (String filename : data){
                    
                    if(filename.contains(code)){
        System.out.println("RESPONSE YES.................................");
                        return "OK";
                    }
                    
                }
        }
        System.out.println("RESPONSE NO.................................");
        return "100"; 
    }    

    
    public String [] getOracleCon() throws SQLException, ClassNotFoundException, JSONException{
        
        //pwd repository and extra url, login and pass****/
        Pwd pwd = pwdRepository.findByAcscd("0043","0");
//        LOGIN_ORACLE=pwd.getLogin();
//        URL_ORACLE =  pwd.getLib1();
//        //SFTPPORT = Integer.parseInt(pwd.getLib2());
//        PASSWORD_ORACLE=pwd.getPass();
        
        byte[] decoder = Base64.getDecoder().decode(trim(pwd.getPass().toString()));
        String v = new String(decoder);
        URL_ORACLE = trim(pwd.getLib1());
        LOGIN_ORACLE = trim(pwd.getLogin());
        PASSWORD_ORACLE = trim(v);
        
     
        return new String[]{URL_ORACLE,LOGIN_ORACLE,PASSWORD_ORACLE};

    } 

//    public Boolean getLicense () throws ClassNotFoundException, SQLException, JSchException, SftpException, FileNotFoundException, NoSuchAlgorithmException{
//        String rep = "/var/www/html/bicec/license";
//        String st= "SELECT * FROM sanm WHERE  tabcd='0012' and dele='0'";
//        String NOM = null, CODE = null;
////        String query = "select 1 from bkdvir where fich = 'FIC160"+code+"'";
//       
//        try {
//            Statement stm3 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//            ResultSet sys1 = stm3.executeQuery(st);
//            while(sys1.next()){
//                if(sys1.getString("acscd").equalsIgnoreCase("0001")){
//                    NOM=sys1.getString("lib2");  // Le chemin du repsertoire
//                }
//                if(sys1.getString("acscd").equalsIgnoreCase("0009")){
//                    CODE = sys1.getString("lib2"); // le Prefixe du nom du fichier+le corps du nom
//                }
//                if(sys1.getString("acscd").equalsIgnoreCase("0019")){
//                    MachineIP2=sys1.getString("lib2");   //ip adresse
//                }
//                if(sys1.getString("acscd").equalsIgnoreCase("0020")){
//                    SFTPUSER2=sys1.getString("lib2");  // SFTP USER
//                }
//                if(sys1.getString("acscd").equalsIgnoreCase("0022")){
//                    SFTPPORT2 = Integer.parseInt(sys1.getString("lib2"));  //SFTP PORT 
//                }
//                if(sys1.getString("acscd").equalsIgnoreCase("0021")){
//                    SFTPPASS2=sys1.getString("lib2");  // SFTP PASSWORD
//                } 
//            }
//            FOR_STAT.put("nom", NOM);
//            FOR_STAT.put("code", CODE);
//            FOR_STAT.put("SFTPUSER2", SFTPUSER2);
//            FOR_STAT.put("MachineIP2", MachineIP2);
//            FOR_STAT.put("SFTPPORT2", String.valueOf(SFTPPORT2));
//            FOR_STAT.put("SFTPPASS2", SFTPPASS2);
//            
//            JSch jsch = new JSch();
//            session = jsch.getSession(FOR_STAT.get("SFTPUSER2"),FOR_STAT.get("MachineIP2"),Integer.parseInt(FOR_STAT.get("SFTPPORT2")));
//            session.setPassword(FOR_STAT.get("SFTPPASS2"));
//            System.out.println(FOR_STAT.get("SFTPUSER2")+'-'+FOR_STAT.get("MachineIP2")+'-'+Integer.parseInt(FOR_STAT.get("SFTPPORT2"))+'-'
//            +FOR_STAT.get("SFTPPASS2"));
//            java.util.Properties config = new java.util.Properties();
//            config.put("StrictHostKeyChecking", "no");
//            session.setConfig(config);
//            session.connect();
//            channel = session.openChannel("sftp");
//            channel.connect();
//            channelSftp = (ChannelSftp)channel;
//            channelSftp.cd(rep);
//            //Vector filelist = channelSftp.ls(SFTPWORKINGDIR+S2M_PREF+"*");
//           // Vector filelist = channelSftp.ls(FOR_STAT.get("SFTPWORKINGDIR")+FOR_STAT.get("S2M_PREF")+code);
//            Vector fileList =channelSftp.ls(rep);
//            String secret = "";
//            String key = "";
//            for(int z=0; z<fileList.size();z++){
//                if( fileList.get(z).toString().contains("secret"))secret = fileList.get(z).toString();
//                if( fileList.get(z).toString().contains(NOM))key = fileList.get(z).toString();
//                
//            }
//            String serial = "0";
//            String license = "";
//            String secret_key = "";
//            File myObj = new File(secret);
//            Scanner myReader = new Scanner(myObj);
//            while (myReader.hasNextLine()) {
//                secret_key = myReader.nextLine();
////                System.out.println(data);
//            }
//            myReader.close();
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            String pass = NOM+CODE+serial+secret_key;
//            md.update(pass.getBytes());
//            byte[] digest = md.digest();
//            
//            String myHash = DatatypeConverter
//                .printHexBinary(digest).toUpperCase();
//            File myObj1 = new File(key);
//            Scanner myReader1 = new Scanner(myObj1);
//            while (myReader1.hasNextLine()) {
//                license = myReader1.nextLine();
////                System.out.println(data);
//            }
//            myReader1.close();
//            String[] arrOfStr = license.split("-", 10); 
//            if(myHash.equals(arrOfStr[2]+arrOfStr[4]+arrOfStr[0]) &&  arrOfStr[1]> strtotime){
//                
//            }else{
//                System.out.println("Licence expirée et/ou fichier endomagée");
//                return false;
//            }
//            
//        } catch (SQLException e ) {
//            System.out.println("Licence expirée et/ou fichier endomagée");
//            return false;
//        }  
//        return true;
//    }
    
    //return a persons account information by cli
    public Map<String, String> getPersonInfoCli(String cli) throws SQLException, ClassNotFoundException {     
        ORACLE_CON_PARAM = getOracleCon();        
        JSONArray response = new JSONArray();        
        Map<String, String> result = new HashMap();        
        result.put("success", "100");        
        result.put("data", null);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        try {
           // connection = DriverManager.getConnection(url, login, password);
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs =null;
        String query = "";
        
        query = "select a.age agence, a.cli, a.nomrest, ('')phone_efacture, ('')addr,\n" +
            "a.nid, a.tid, (select lib1 from "+getSchema()+".bknom where ctab='078' and cacc=a.tid) lib_tid,\n" +
            "a.did deliv_id, a.lid lieu_deliv_id, a.oid aut_id, a.vid exp_id, a.dna date_naiss,\n" +
            "a.viln lieu_naiss, ('')email,('')telsec from "+getSchema()+".bkcli a where tcli = '1' and cli = '"+cli+"'";
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            response = convert(rs);
            
            if(response != null || !response.isEmpty()){
                result.put("success", "01");
                result.put("data", response.toString());
            }
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
        } finally {
//            if (stmt != null) { stmt.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }
        
        connection.close();
        if (connection.isClosed())System.out.println("Connection 1 closed.");
        return result;
    }
    //returns a company acccount information by cli
    public Map<String, String> getCompInfoCli(String cli) throws SQLException, ClassNotFoundException {     
        ORACLE_CON_PARAM = getOracleCon();        
        JSONArray response = new JSONArray();        
        Map<String, String> result = new HashMap();        
        result.put("success", "100");        
        result.put("data", null);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        try {
           // connection = DriverManager.getConnection(url, login, password);
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs =null;
        String query = "";
        
        query = "select a.age agence, a.cli, a.nomrest, ('')phone_efacture,('')addr,\n" +
            "a.nrc, a.nidf carte_contr, a.datc date_creation, a.sig sigle, \n" +
            " ('')email,('')telsec \n" +
            "from "+getSchema()+".bkcli a where tcli = '2' and cli ='"+cli+"'";
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            response = convert(rs);
            
            if(response != null || !response.isEmpty()){
                result.put("success", "01");
                result.put("data", response.toString());
            }
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
        } finally {
//            if (stmt != null) { stmt.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }
        
        connection.close();
        if (connection.isClosed())System.out.println("Connection 1 closed.");
        return result;
    }
    //return a persons account information by cpt
    public Map<String, String> getPersonInfoCpt(String cpt) throws SQLException, ClassNotFoundException {     
        ORACLE_CON_PARAM = getOracleCon();        
        JSONArray response = new JSONArray();        
        Map<String, String> result = new HashMap();        
        result.put("success", "100");        
        result.put("data", null);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        try {
           // connection = DriverManager.getConnection(url, login, password);
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs =null;
        String query = "";
        
        query = "select a.age agence, a.cli, a.nomrest, ('')phone_efacture, ('')addr,\n" +
            "a.nid, a.tid, (select lib1 from "+getSchema()+".bknom where ctab='078' and cacc=a.tid) lib_tid,\n" +
            "a.did deliv_id, a.lid lieu_deliv_id, a.oid aut_id, a.vid exp_id, a.dna date_naiss,\n" +
            "a.viln lieu_naiss, ('')email,('')telsec from "+getSchema()+".bkcli a where tcli = '1' and cli = \n" +
            "(select distinct cli from "+getSchema()+".bkcom where ncp ='"+cpt+"')";
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            response = convert(rs);
            
            if(response != null || !response.isEmpty()){
                result.put("success", "01");
                result.put("data", response.toString());
            }
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
        } finally {
//            if (stmt != null) { stmt.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }
        
        connection.close();
        if (connection.isClosed())System.out.println("Connection 1 closed.");
        return result;
    }
    //returns a company acccount information by cpt
    public Map<String, String> getCompInfoCpt(String cpt) throws SQLException, ClassNotFoundException {     
        ORACLE_CON_PARAM = getOracleCon();        
        JSONArray response = new JSONArray();        
        Map<String, String> result = new HashMap();        
        result.put("success", "100");        
        result.put("data", null);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        try {
           // connection = DriverManager.getConnection(url, login, password);
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs =null;
        String query = "";
        
        query = "select a.age agence, a.cli, a.nomrest, ('')phone_efacture,('')addr,\n" +
            "a.nrc, a.nidf carte_contr, a.datc date_creation, a.sig sigle, \n" +
            " ('')email,('')telsec \n" +
            "from "+getSchema()+".bkcli a where tcli = '2' and cli = \n" +
            "(select distinct cli from "+getSchema()+".bkcom where ncp = '"+cpt+"')";
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            response = convert(rs);
            
            if(response != null || !response.isEmpty()){
                result.put("success", "01");
                result.put("data", response.toString());
            }
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
        } finally {
//            if (stmt != null) { stmt.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }
        
        connection.close();
        if (connection.isClosed())System.out.println("Connection 1 closed.");
        return result;
    }
    //returns account infornation of either company&individual by cli
    public Map<String, String> getInfoCompOrIndByCli(String cli) throws SQLException, ClassNotFoundException {     
        ORACLE_CON_PARAM = getOracleCon();        
        JSONArray response = new JSONArray();        
        Map<String, String> result = new HashMap();        
        result.put("success", "100");        
        result.put("data", null);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        try {
           // connection = DriverManager.getConnection(url, login, password);
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs =null;
        String query = "";
        
        query = "select a.age agence, a.cli, a.nomrest, ('')phone_efacture,('')addr,\n" +
            "a.nrc, a.nidf carte_contr, a.datc date_creation, a.sig sigle, \n" +
            "a.nid, a.tid, (select lib1 from "+getSchema()+".bknom where ctab='078' and cacc=a.tid) lib_tid,\n" +
            "a.did deliv_id, a.lid lieu_deliv_id, a.oid aut_id, a.vid exp_id, a.dna date_naiss,\n" +
            "a.viln lieu_naiss, ('')email,('')telsec \n" +
            "from "+getSchema()+".bkcli a where cli ='"+cli+"'";
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            response = convert(rs);
            
            if(response != null || !response.isEmpty()){
                result.put("success", "01");
                result.put("data", response.toString());
            }
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
        } finally {
//            if (stmt != null) { stmt.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }
        
        connection.close();
        if (connection.isClosed())System.out.println("Connection 1 closed.");
        return result;
    }
    //return account information of either company or individual by cpt
    public Map<String, String> getInfoCompOrIndByCpt(String cpt) throws SQLException, ClassNotFoundException {     
        ORACLE_CON_PARAM = getOracleCon();        
        JSONArray response = new JSONArray();        
        Map<String, String> result = new HashMap();        
        result.put("success", "100");        
        result.put("data", null);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        try {
           // connection = DriverManager.getConnection(url, login, password);
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs =null;
        String query = "";
        
        query = "select a.age agence, a.cli, a.nomrest, ('')phone_efacture,('')addr,\n" +
            "a.nrc, a.nidf carte_contr, a.datc date_creation, a.sig sigle, \n" +
            "a.nid, a.tid, (select lib1 from "+getSchema()+".bknom where ctab='078' and cacc=a.tid) lib_tid,\n" +
            "a.did deliv_id, a.lid lieu_deliv_id, a.oid aut_id, a.vid exp_id, a.dna date_naiss,\n" +
            "a.viln lieu_naiss, ('')email,('')telsec \n" +
            "from "+getSchema()+".bkcli a where cli = (select distinct cli from "+getSchema()+".bkcom where ncp ='"+cpt+"')";
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            response = convert(rs);
            
            if(response != null || !response.isEmpty()){
                result.put("success", "01");
                result.put("data", response.toString());
            }
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
        } finally {
//            if (stmt != null) { stmt.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }
        
        connection.close();
        if (connection.isClosed())System.out.println("Connection 1 closed.");
        return result;
    }
    
    //List all agencies
    public Map<String, String> getAllAgencies() throws SQLException, ClassNotFoundException {     
        ORACLE_CON_PARAM = getOracleCon();
        JSONArray response = new JSONArray();
        Map<String, String> result = new HashMap();
        result.put("success", "100");
        result.put("data", null);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        try {
           // connection = DriverManager.getConnection(url, login, password);
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs =null;
        String query = "";
        
//        query = "select cacc code_agence, lib1 libelle, lib2 etat_site, lib4 nom_court, lib5 etat_age, \n" +
//                "mnt1 dco, mnt2 dco2 from "+getSchema()+".bknom where ctab = '001' order by ctab";
        
        query = "select cacc code_agence, lib1 libelle, lib2 etat_site, lib4 nom_court, lib5 etat_age, \n" +
                "mnt1 dco, mnt2 dco2 from "+getSchema()+".bknom where ctab = '001' order by ctab,cacc asc";
        
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            response = convert(rs);
            
            if(response != null || !response.isEmpty()){
                result.put("success", "01");
                result.put("data", response.toString());
            }
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
                System.out.println(e);
        } finally {
//            if (stmt != null) { stmt.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }
        
        connection.close();
        if (connection.isClosed())System.out.println("Connection 1 closed.");
        return result;
    }
    
    //List all different accounts by cli
    public Map<String, String> getDiffAccByCli(String cli) throws SQLException, ClassNotFoundException {     
        ORACLE_CON_PARAM = getOracleCon();
        JSONArray response = new JSONArray();
        Map<String, String> result = new HashMap();
        result.put("success", "100");
        result.put("data", null);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        try {
           // connection = DriverManager.getConnection(url, login, password);
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs =null;
        String query = "";
        
//        query = "select age, ncp, dev, cli, inti, cha, cfe, ife, sde, sin, dou, dmo from " +
//            getSchema()+".bkcom where cha like '37%' and cfe='N' and ife='N' and cli='"+cli+"'";
        
          query = "select age, ncp, dev, cli, inti, cha, cfe, ife, sde, sin, dou, dmo from " +
            getSchema()+".bkcom where cha in('371100','372100','373100') and cfe='N' and ife='N' and cli='"+cli+"'";
     
        
        System.out.println("yann see this "+query);
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            response = convert(rs);
            
            if(response != null || !response.isEmpty()){
                result.put("success", "01");
                result.put("data", response.toString());
            }
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
        } finally {
//            if (stmt != null) { stmt.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }
        
        connection.close();
        if (connection.isClosed())System.out.println("Connection 1 closed.");
        return result;
    }
    
    //List all different accounts by cpt
    public Map<String, String> getDiffAccByCpt(String cpt) throws SQLException, ClassNotFoundException {     
        ORACLE_CON_PARAM = getOracleCon();        
        JSONArray response = new JSONArray();        
        Map<String, String> result = new HashMap();        
        result.put("success", "100");        
        result.put("data", null);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        try {
           // connection = DriverManager.getConnection(url, login, password);
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs =null;
        String query = "";
//        
//         query = "select age, ncp, dev, cli, inti, cha, cfe, ife, sde, sin, dou, dmo from " +
//            getSchema()+".bkcom where cha like '37%' and cfe='N' and ife='N' and cli = (select distinct cli from "+getSchema()+".bkcom where ncp ='"+ cpt+"')";
     
        
        query = "select age, ncp, dev, cli, inti, cha, cfe, ife, sde, sin, dou, dmo from " +
            getSchema()+".bkcom where cha in('371100','372100','373100') and cfe='N' and ife='N' and cli = (select distinct cli from "+getSchema()+".bkcom where ncp ='"+ cpt+"')";
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            response = convert(rs);
            
            if(response != null || !response.isEmpty()){
                result.put("success", "01");
                result.put("data", response.toString());
            }
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
        } finally {
//            if (stmt != null) { stmt.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }
        
        connection.close();
        if (connection.isClosed())System.out.println("Connection 1 closed.");
        return result;
    }
    
    
      public static JSONArray mapResultSet(ResultSet rs) throws SQLException, JSONException
	{ 
        JSONArray jArray = new JSONArray();
        JSONObject jsonObject = null;
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        do
        {
	        jsonObject = new JSONObject();
	        for (int index = 1; index <= columnCount; index++) 
	        {
	            String column = rsmd.getColumnName(index);
	            Object value = rs.getObject(column);
	            if (value == null) 
	            {
	                jsonObject.put(column, "");
	            } else if (value instanceof Integer) {
	                jsonObject.put(column, (Integer) value);
	            } else if (value instanceof String) {
	                jsonObject.put(column, (String) value);                
	            } else if (value instanceof Boolean) {
	                jsonObject.put(column, (Boolean) value);           
	            } else if (value instanceof java.sql.Date) {
	                jsonObject.put(column, ((java.sql.Date) value).getTime());                
	            } else if (value instanceof Long) {
	                jsonObject.put(column, (Long) value);                
	            } else if (value instanceof Double) {
	                jsonObject.put(column, (Double) value);                
	            } else if (value instanceof Float) {
	                jsonObject.put(column, (Float) value);                
	            } else if (value instanceof BigDecimal) {
	                jsonObject.put(column, (BigDecimal) value);
	            } else if (value instanceof Byte) {
	                jsonObject.put(column, (Byte) value);
	            } else if (value instanceof byte[]) {
	                jsonObject.put(column, (byte[]) value);                
	            } else {
	                throw new IllegalArgumentException("Unmappable object type: " + value.getClass());
	            }
        	}
        	jArray.put(jsonObject);
        }while(rs.next());
        return jArray;
    }
      
      public static JSONArray convert( ResultSet rs )throws SQLException, JSONException{
    JSONArray json = new JSONArray();
    ResultSetMetaData rsmd = rs.getMetaData();

    while(rs.next()) {
      int numColumns = rsmd.getColumnCount();
      JSONObject obj = new JSONObject();

      for (int i=1; i<numColumns+1; i++) {
        String column_name = rsmd.getColumnName(i);

        if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
         obj.put(column_name, rs.getArray(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
         obj.put(column_name, rs.getBoolean(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
         obj.put(column_name, rs.getBlob(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
         obj.put(column_name, rs.getDouble(column_name)); 
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
         obj.put(column_name, rs.getFloat(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
         obj.put(column_name, rs.getNString(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
         obj.put(column_name, rs.getString(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
         obj.put(column_name, rs.getDate(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
        obj.put(column_name, rs.getTimestamp(column_name));   
        }
        else{
         obj.put(column_name, rs.getObject(column_name));
        }
      }

      json.put(obj);
    }

    return json;
  }
      
      public String DateValService() throws SQLException, ClassNotFoundException {
        ORACLE_CON_PARAM = getOracleCon();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR WITH THE ORACLE JDBC DRIVER Where is your Oracle JDBC Driver?");
            System.out.println("Date Valeur Returned :" );
            System.out.println("INCORRECT Valeur Returned :" );
            e.printStackTrace();

            return "";
        }
        try {
        // connection = DriverManager.getConnection(url, login, password);
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("CAN'T GET VALUE DATE FROM THE DATABASE:" );
            System.out.println("INCORRECT Value Date return:" );
            e.printStackTrace();
            return "";
        // e.printStackTrace();
        }
        // connection = JDBCUtils.getConnection("env ");
        // connection.setAutoCommit(false);
        Statement stmt = null;
        ResultSet rs =null;
        String query = "";
        // if(type == "0"){
        // query = "select DISTINCT SDE,SIN FROM bkcom ";
        // }else{
        // query = "select DISTINCT SDE,SIN FROM bkcom ";
        // }
        query = "select (case when mnt2!=0 then mnt2 else mnt1 end)mnt from "+getSchema()+"bknom where ctab='001' and cacc='99000'";
        //System.out.println("Query to get Value Date: "+query);
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
            date = rs.getString("mnt");
            // System.out.println("dateValeur" +rs.getString("mnt2") );
        }

        } catch (SQLException e ) {
        //JDBCTutorialUtilities.printSQLException(e);
            e.printStackTrace();
            System.out.println("SOMETHING WENT WRONG DURING STATEMENT CREATION. INCORRECT VALUE DATE RETURNED" );
            System.out.println("CHECK ORACLE DATABASE CONNECTION AND VERIFY IF THE SERVER IS ONLINE");
            return "";

        } finally {
        // if (stmt != null) { stmt.close(); }
            try { 
                stmt.close();
                if (stmt.isClosed())
                    System.out.println("Connection Statement closed."); 
            } catch (Exception e) {
                /// ignored / 
            }
            try { 
                rs.close();
                if (rs.isClosed())
                    System.out.println("Connection Resultset closed."); 
            } catch (Exception e) {
                /// ignored / 
            }
        }

            connection.close();
            if (connection.isClosed())System.out.println("Connection 1 closed.");
            System.out.println("DateValeur returned" +date );

            String result = "000"+date;
            String val = result.substring(result.length()- 8);
            return val.substring(4, 8) +'-' +val.substring(2,4 )+'-'+val.substring(0,2 );
            // return date;
     }
      
      public String getNatureCode(String code) throws SQLException, ClassNotFoundException, JSONException{
        String select = "";
        JSONObject obj = new JSONObject();
        String nature = "";
        //boolean val = false;
        Class.forName("com.mysql.jdbc.Driver");
        try {
            connection = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
      
        Statement stmt =null;
        stmt = (Statement) connection.createStatement();
        select = "SELECT * FROM sanm  WHERE tabcd='"+NOMENGABTABLE_NATURE+"' AND dele='0' And acscd="+code+"";
        ResultSet result = stmt.executeQuery(select);
        while(result.next()) { 
            nature = result.getString("lib3"); // l adresse ip du serveur Amplitude
               
        }
        connection.close();
        } catch (SQLException ex) {
//                Logger.getLogger(MonThread1.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return nature;
        }
        if (connection.isClosed())System.out.println("Connection 2 closed.");
        return nature;

    }
      
      public Map<String, String> getPain001conf() throws SQLException, ClassNotFoundException, JSONException{
       
        Map<String, String> paincontent = new HashMap<String, String>();
        paincontent.put("DltPrvtDataFlwInd", ""); 
        paincontent.put("DltPrvtDataDtlPrvtDtInf", ""); 
        paincontent.put("DltPrvtDataDtlCdOrPrtry", ""); 
        paincontent.put("PmtInfPmtMtd", ""); 
        paincontent.put("PmtInfBtchBookg", ""); 
        paincontent.put("DltPrvtDataTp", ""); 
        paincontent.put("DltPrvtDataMd", ""); 
        paincontent.put("PmtTpInfInstrPrty", ""); 
        paincontent.put("SvcLvlPrtry", ""); 
        paincontent.put("DbtrAcctSchmeNmPrtry", ""); 
        paincontent.put("DbtrAcctcc", ""); 
        paincontent.put("DbtrAcctSchmeNm", ""); 
        paincontent.put("DbtrAgtSchmeNm", ""); 
        paincontent.put("myNamespace", ""); 
        paincontent.put("myNamespaceURI", ""); 
        paincontent.put("FinInstnIdNm", ""); 
        paincontent.put("FinInstnIdId", ""); 
        paincontent.put("soapEndpointUrl", ""); 
        paincontent.put("DltPrvtDataFlwInd", ""); 
        paincontent.put("DltPrvtDataFlwInd", ""); 
        paincontent.put("DltPrvtDataFlwInd", ""); 
        
        String PAIN_TABLE ="5011";

        String select = "";
        //boolean val = false;
        Class.forName("com.mysql.jdbc.Driver");
        try { 
            
            connection = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
        } catch (SQLException ex) {
            System.out.println("FATAL ERROR CANNOT CONNECT TO IWOMICORE DATABASE.");
            System.out.println("VERIFY YOUR DATABASE PARAMETERS IN THE CONFIGURATION FILE.");
            ex.printStackTrace();
            return paincontent;
//                Logger.getLogger(MonThread1.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
        Statement stmt = (Statement) connection.createStatement();
        select = "SELECT * FROM sanm  WHERE tabcd='"+PAIN_TABLE+"' AND dele='0'";
        ResultSet result = stmt.executeQuery(select);
        while(result.next()) { 
             
           if(result.getString("acscd").equalsIgnoreCase("0001")){
                 paincontent.put("DltPrvtDataFlwInd", result.getString("lib3")); // Le compte tva
           }
           if(result.getString("acscd").equalsIgnoreCase("0002")){
                 paincontent.put("DltPrvtDataDtlPrvtDtInf", result.getString("lib3")); // Le compte tva

           } 
           if(result.getString("acscd").equalsIgnoreCase("0003")){
                 paincontent.put("DltPrvtDataDtlCdOrPrtry", result.getString("lib3")); // Le compte tva
           } 
           if(result.getString("acscd").equalsIgnoreCase("0004")){
                 paincontent.put("PmtInfPmtMtd", result.getString("lib3")); // Le compte tva
           } 
           if(result.getString("acscd").equalsIgnoreCase("0005")){
                 paincontent.put("PmtInfBtchBookg", result.getString("lib3")); // Le compte tva
           } 
           if(result.getString("acscd").equalsIgnoreCase("0006")){
                 paincontent.put("DltPrvtDataTp", result.getString("lib3")); // Le compte tva
           } 
           if(result.getString("acscd").equalsIgnoreCase("0007")){
                 paincontent.put("DltPrvtDataMd", result.getString("lib3")); // Le compte tva
           } 
           if(result.getString("acscd").equalsIgnoreCase("0008")){
                 paincontent.put("PmtTpInfInstrPrty", result.getString("lib3")); // Le compte tva
           } 
           if(result.getString("acscd").equalsIgnoreCase("0009")){
                 paincontent.put("SvcLvlPrtry", result.getString("lib3")); // Le compte tva
           } 
           if(result.getString("acscd").equalsIgnoreCase("0010")){
                 paincontent.put("DbtrAcctSchmeNmPrtry", result.getString("lib3")); // Le compte tva
           } 
           if(result.getString("acscd").equalsIgnoreCase("0011")){
                 paincontent.put("DbtrAcctcc", result.getString("lib3")); // Le compte tva
           } 
           if(result.getString("acscd").equalsIgnoreCase("0012")){
                 paincontent.put("DbtrAcctSchmeNm", result.getString("lib3")); // Le compte tva
           } 
           if(result.getString("acscd").equalsIgnoreCase("0013")){
                 paincontent.put("DbtrAgtSchmeNm", result.getString("lib3")); // Le compte tva
           } 

           if(result.getString("acscd").equalsIgnoreCase("0014")){
                 paincontent.put("myNamespace", result.getString("lib3")); // Le compte tva
           } 
                if(result.getString("acscd").equalsIgnoreCase("0015")){
                 paincontent.put("myNamespaceURI", result.getString("lib3")); // Le compte tva
           } 
           if(result.getString("acscd").equalsIgnoreCase("0016")){
                 paincontent.put("FinInstnIdNm", result.getString("lib3")); // Le compte tva
           } 
                if(result.getString("acscd").equalsIgnoreCase("0017")){
                 paincontent.put("FinInstnIdId", result.getString("lib3")); // Le compte tva
           } 
           if(result.getString("acscd").equalsIgnoreCase("0018")){
                 paincontent.put("soapEndpointUrl", result.getString("lib3")); // Le compte tva
           } 
        }
        connection.close();
        stmt.close();
       
        } catch (SQLException ex) {
            connection.close();       
            ex.printStackTrace();
            return paincontent;

               // Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       if (connection.isClosed())System.out.println("Connection to iwomi database successfully closed.");

        return paincontent;

       }
}
