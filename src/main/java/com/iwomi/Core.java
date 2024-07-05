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
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.apache.commons.lang3.StringUtils.trim;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */

@Service
public class Core {
    
    @Autowired
    NomenclatureRepository nomenclatureRepository;
   
    @Autowired
    PwdRepository pwdRepository;
    
    Nomenclature nomenclature = null;
    
    
    public static final String CONN_STRING = "jdbc:mysql://localhost:3306/mtnapimanagement?autoReconnect=true&useSSL=false";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    Connection connection, con = null;
    private String SFTPWORKINGDIR;
    private String S2M_PREF;
    private String FIC_ERR;
    private String FIC_ARC;
    private String MachineIP2;
    private String SFTPUSER2;
    private int SFTPPORT2;
    private String SFTPPASS2;
    private Map<String, String> FOR_STAT = new HashMap();
    private String URL_ORACLE;
    private String LOGIN_ORACLE;
    private String PASSWORD_ORACLE;
    private String[] ORACLE_CON_PARAM = null;
    private ChannelSftp channelSftp = null;
    private Session session;
    private Channel channel = null;
        
    public JSONObject checkstatus(String code) throws SQLException, JSchException, ClassNotFoundException, SftpException, InterruptedException, JSONException{
       
        try {
            ORACLE_CON_PARAM = getOracleCon();
        } catch (JSONException ex) {
            //Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
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
        }
       
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }


        try {
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        try {
            con = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
        } catch (SQLException ex) {
        }
        String st= "SELECT * FROM sanm WHERE  tabcd='0012' and dele='0'";
        String query = "select distinct (case when coalesce(eta,'EMPTY') in ('VA','VF','FO') then '0' "
                + "when coalesce(eta,'EMPTY') in ('AT') then '1' when coalesce(eta,'EMPTY') in ('IG','AB') then '2' else '3' end)res "
                + "from bkeve where ndos=(select distinct refi from bkdvir where trim(fich) like '"+code+"')";
        
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
                                status = "119";
                                obj.put("status", status);
                                obj.put("message", "No result");
                            } finally {
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
                    try { stmt.close(); } catch (Exception e) { /* ignored */ }
                    try { rs.close(); } catch (Exception e) { /* ignored */ }
                }
            }
        } catch (SQLException e ) {System.out.println("la requete n'a pas marche pour query de la vÃ©rification du fichier");
            status = "122";
            obj.put("status", status);
            obj.put("message", "Erreur de requête");
        } finally {
            try { stm3.close();if (stm3.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { sys1.close();if (sys1.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }   
        con.close();
        connection.close();
        if (connection.isClosed())System.out.println("Connection 1 closed.");
        if (con.isClosed())System.out.println("Connection 2 closed.");
        return obj;
    }
    

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
        if(pwd != null){
            System.out.println("yann see this please I pwd is not null");
        }
        else{
            System.out.println("yann see this please pwd is  null, it can't get the oracle connection details");
        }
        
        
        byte[] decoder = Base64.getDecoder().decode(trim(pwd.getPass()));
        String v = new String(decoder);
        URL_ORACLE = trim(pwd.getLib1());
        LOGIN_ORACLE = trim(pwd.getLogin());
        PASSWORD_ORACLE = trim(v);
     
        return new String[]{URL_ORACLE,LOGIN_ORACLE,PASSWORD_ORACLE};

    }
    
    /*
    public String[] checkClient(String agence, String compte,String montant,String frais ) throws SQLException, ClassNotFoundException {
        ORACLE_CON_PARAM = getOracleCon();
        BigDecimal SOLDE_DISPONIBLE = null,SOLDE_INDICATIF = null ;
        JSONArray response = new JSONArray();
        int result = 0;
        JSONObject ret = new JSONObject();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        
        try {
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Integer isAbonneEtebac=0;
        Integer isEventAttente=0;
        Integer isOppCli=0;
        Integer isOppCom=0;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        String query= "SELECT\n" +
                            "  (\n" +
                            "    CASE\n" +
                            "      WHEN\n" +
                            "        (\n" +
                            "          a.age, a.ncp\n" +
                            "        )\n" +
                            "        IN\n" +
                            "        (\n" +
                            "          SELECT\n" +
                            "            age,\n" +
                            "            ncp\n" +
                            "          FROM\n" +
                            "            "+getSchema()+".bkdetebac\n" +
                            "        )\n" +
                            "      THEN 1\n" +
                            "      ELSE 0\n" +
                            "    END )etb,\n" +
                            "  (\n" +
                            "    SELECT\n" +
                            "      (\n" +
                            "        CASE\n" +
                            "          WHEN EXISTS\n" +
                            "            (\n" +
                            "              SELECT\n" +
                            "                1\n" +
                            "              FROM\n" +
                            "                "+getSchema()+".bkeve\n" +
                            "              WHERE\n" +
                            "                eta='AT'\n" +
                            "              AND\n" +
                            "                (\n" +
                            "                  (\n" +
                            "                    age1  =a.age\n" +
                            "                  AND ncp1=a.ncp\n" +
                            "                  )\n" +
                            "                )\n" +
                            "            )\n" +
                            "          THEN 0\n" +
                            "          ELSE 1\n" +
                            "        END)\n" +
                            "    FROM\n" +
                            "      dual\n" +
                            "  )\n" +
                            "  eve,\n" +
                            "  (\n" +
                            "    SELECT\n" +
                            "      (\n" +
                            "        CASE\n" +
                            "          WHEN EXISTS\n" +
                            "            (\n" +
                            "              SELECT\n" +
                            "                1\n" +
                            "              FROM\n" +
                            "                "+getSchema()+".bkoppcli\n" +
                            "              WHERE\n" +
                            "                eta  ='V'\n" +
                            "              AND cli=\n" +
                            "                (\n" +
                            "                  SELECT\n" +
                            "                    cli\n" +
                            "                  FROM\n" +
                            "                    "+getSchema()+".bkcom\n" +
                            "                  WHERE\n" +
                            "                    age  =a.age\n" +
                            "                  AND ncp=a.ncp\n" +
                            "                )\n" +
                            "            )\n" +
                            "          THEN 0\n" +
                            "          ELSE 1\n" +
                            "        END)\n" +
                            "    FROM\n" +
                            "      dual\n" +
                            "  )\n" +
                            "  oppcli,\n" +
                            "  (\n" +
                            "    SELECT\n" +
                            "      (\n" +
                            "        CASE\n" +
                            "          WHEN EXISTS\n" +
                            "            (\n" +
                            "              SELECT\n" +
                            "                1\n" +
                            "              FROM\n" +
                            "                "+getSchema()+".bkoppcom\n" +
                            "              WHERE\n" +
                            "                eta  ='V'\n" +
                            "              AND age=a.age\n" +
                            "              AND ncp=a.ncp\n" +
                            "            )\n" +
                            "          THEN 0\n" +
                            "          ELSE 1\n" +
                            "        END)\n" +
                            "    FROM\n" +
                            "      dual\n" +
                            "  )\n" +
                            "  oppcom,\n" +
                            "  NVL(\n" +
                            "  (\n" +
                            "    SELECT\n" +
                            "      maut\n" +
                            "    FROM\n" +
                            "      "+getSchema()+".bkautc\n" +
                            "    WHERE\n" +
                            "      age    =a.age\n" +
                            "    AND ncp  =a.ncp\n" +
                            "    AND eta IN ('VA','FO','VF')\n" +
                            "    AND to_date(\n" +
                            "      (\n" +
                            "        SELECT\n" +
                            "          SUBSTR('000'\n" +
                            "          ||(\n" +
                            "            CASE\n" +
                            "              WHEN mnt2 = 0\n" +
                            "              THEN mnt1\n" +
                            "              ELSE mnt2\n" +
                            "            END),-8,-8)\n" +
                            "        FROM\n" +
                            "          "+getSchema()+".bknom\n" +
                            "        WHERE\n" +
                            "          ctab  ='001'\n" +
                            "        AND cacc=a.age\n" +
                            "      )\n" +
                            "      ,'DDMMYYYY') BETWEEN debut AND fin\n" +
                            "  )\n" +
                            "  ,0) decouvert,\n" +
                            "  NVL((\n" +
                            "      CASE\n" +
                            "        WHEN a.sin<0\n" +
                            "        THEN a.sin\n" +
                            "        ELSE 0\n" +
                            "      END),0) util_deco,\n" +
                            "  NVL(\n" +
                            "  (\n" +
                            "    SELECT\n" +
                            "      NVL(SUM(mplf),0)\n" +
                            "    FROM\n" +
                            "      "+getSchema()+".bkaplf z,\n" +
                            "      "+getSchema()+".bkaplfr y\n" +
                            "    WHERE\n" +
                            "      z.cli     = a.cli\n" +
                            "    AND z.nplf  = y.nplf\n" +
                            "    AND z.cli   =y.cli\n" +
                            "    AND z.ddeb <=to_date(\n" +
                            "      (\n" +
                            "        SELECT\n" +
                            "          SUBSTR('000'\n" +
                            "          || (\n" +
                            "            CASE\n" +
                            "              WHEN mnt2 = 0\n" +
                            "              THEN mnt1\n" +
                            "              ELSE mnt2\n" +
                            "            END),-8,8)\n" +
                            "        FROM\n" +
                            "          "+getSchema()+".bknom\n" +
                            "        WHERE\n" +
                            "          ctab  ='001'\n" +
                            "        AND cacc=a.age\n" +
                            "      )\n" +
                            "      , 'DDMMYYYY')\n" +
                            "    AND z.dech>to_date(\n" +
                            "      (\n" +
                            "        SELECT\n" +
                            "          SUBSTR('000'\n" +
                            "          || (\n" +
                            "            CASE\n" +
                            "              WHEN mnt2 = 0\n" +
                            "              THEN mnt1\n" +
                            "              ELSE mnt2\n" +
                            "            END),-8,8)\n" +
                            "        FROM\n" +
                            "          "+getSchema()+".bknom\n" +
                            "        WHERE\n" +
                            "          ctab  ='001'\n" +
                            "        AND cacc=a.age\n" +
                            "      )\n" +
                            "      , 'DDMMYYYY')\n" +
                            "    AND\n" +
                            "      (\n" +
                            "        y.ncp  = a.ncp\n" +
                            "      OR y.cha =a.cha\n" +
                            "      OR y.cpro=a.cpro\n" +
                            "      )\n" +
                            "  )\n" +
                            "  ,0) aplf,\n" +
                            "  NVL(\n" +
                            "  (\n" +
                            "    SELECT\n" +
                            "      NVL(SUM(sin),0)\n" +
                            "    FROM\n" +
                            "      "+getSchema()+".bkaplf z,\n" +
                            "      "+getSchema()+".bkaplfr y,\n" +
                            "      "+getSchema()+".bkcom x\n" +
                            "    WHERE\n" +
                            "      x.sin     <0\n" +
                            "    AND z.cli   =x.cli\n" +
                            "    AND z.cli   = a.cli\n" +
                            "    AND z.nplf  = y.nplf\n" +
                            "    AND z.cli   =y.cli\n" +
                            "    AND z.ddeb <=to_date(\n" +
                            "      (\n" +
                            "        SELECT\n" +
                            "          SUBSTR('000'\n" +
                            "          || (\n" +
                            "            CASE\n" +
                            "              WHEN mnt2 = 0\n" +
                            "              THEN mnt1\n" +
                            "              ELSE mnt2\n" +
                            "            END),-8,8)\n" +
                            "        FROM\n" +
                            "          "+getSchema()+".bknom\n" +
                            "        WHERE\n" +
                            "          ctab  ='001'\n" +
                            "        AND cacc=a.age\n" +
                            "      )\n" +
                            "      , 'DDMMYYYY')\n" +
                            "    AND z.dech>to_date(\n" +
                            "      (\n" +
                            "        SELECT\n" +
                            "          SUBSTR('000'\n" +
                            "          || (\n" +
                            "            CASE\n" +
                            "              WHEN mnt2 = 0\n" +
                            "              THEN mnt1\n" +
                            "              ELSE mnt2\n" +
                            "            END),-8,8)\n" +
                            "        FROM\n" +
                            "          "+getSchema()+".bknom\n" +
                            "        WHERE\n" +
                            "          ctab  ='001'\n" +
                            "        AND cacc=a.age\n" +
                            "      )\n" +
                            "      , 'DDMMYYYY')\n" +
                            "    AND\n" +
                            "      (\n" +
                            "        y.ncp  = x.ncp\n" +
                            "      OR y.cha =x.cha\n" +
                            "      OR y.cpro=x.cpro\n" +
                            "      )\n" +
                            "  )\n" +
                            "  ,0) utilisation_aplf,\n" +
                            "  NVL(\n" +
                            "  (\n" +
                            "    SELECT\n" +
                            "      SUM(mon)\n" +
                            "    FROM\n" +
                            "      "+getSchema()+".bkreserv a\n" +
                            "    WHERE\n" +
                            "      agecpt  =a.age\n" +
                            "    AND ncpcpt=a.ncp\n" +
                            "    AND devcpt=a.dev\n" +
                            "    AND dech  >to_date(\n" +
                            "      (\n" +
                            "        SELECT\n" +
                            "          SUBSTR('000'\n" +
                            "          || (\n" +
                            "            CASE\n" +
                            "              WHEN mnt2 = 0\n" +
                            "              THEN mnt1\n" +
                            "              ELSE mnt2\n" +
                            "            END),-8,8)\n" +
                            "        FROM\n" +
                            "          "+getSchema()+".bknom\n" +
                            "        WHERE\n" +
                            "          ctab  ='001'\n" +
                            "        AND cacc=a.age\n" +
                            "      )\n" +
                            "      , 'DDMMYYYY')\n" +
                            "    AND dco<=to_date(\n" +
                            "      (\n" +
                            "        SELECT\n" +
                            "          SUBSTR('000'\n" +
                            "          || (\n" +
                            "            CASE\n" +
                            "              WHEN mnt2 = 0\n" +
                            "              THEN mnt1\n" +
                            "              ELSE mnt2\n" +
                            "            END),-8,8)\n" +
                            "        FROM\n" +
                            "          "+getSchema()+".bknom\n" +
                            "        WHERE\n" +
                            "          ctab  ='001'\n" +
                            "        AND cacc=a.age\n" +
                            "      )\n" +
                            "      , 'DDMMYYYY')\n" +
                            "    AND eta IN ('VA','VF','FO')\n" +
                            "  )\n" +
                            "  ,0) reserv,\n" +
                            "  (\n" +
                            "    CASE\n" +
                            "      WHEN\n" +
                            "        (\n" +
                            "          a.sin+ NVL(\n" +
                            "          (\n" +
                            "            SELECT\n" +
                            "              NVL(SUM(mplf),0)\n" +
                            "            FROM\n" +
                            "              "+getSchema()+".bkaplf z,\n" +
                            "              "+getSchema()+".bkaplfr y\n" +
                            "            WHERE\n" +
                            "              z.cli     = a.cli\n" +
                            "            AND z.nplf  = y.nplf\n" +
                            "            AND z.cli   =y.cli\n" +
                            "            AND z.ddeb <=to_date(\n" +
                            "              (\n" +
                            "                SELECT\n" +
                            "                  SUBSTR('000'\n" +
                            "                  || (\n" +
                            "                    CASE\n" +
                            "                      WHEN mnt2 = 0\n" +
                            "                      THEN mnt1\n" +
                            "                      ELSE mnt2\n" +
                            "                    END),-8,8)\n" +
                            "                FROM\n" +
                            "                  "+getSchema()+".bknom\n" +
                            "                WHERE\n" +
                            "                  ctab  ='001'\n" +
                            "                AND cacc=a.age\n" +
                            "              )\n" +
                            "              , 'DDMMYYYY')\n" +
                            "            AND z.dech>to_date(\n" +
                            "              (\n" +
                            "                SELECT\n" +
                            "                  SUBSTR('000'\n" +
                            "                  || (\n" +
                            "                    CASE\n" +
                            "                      WHEN mnt2 = 0\n" +
                            "                      THEN mnt1\n" +
                            "                      ELSE mnt2\n" +
                            "                    END),-8,8)\n" +
                            "                FROM\n" +
                            "                  "+getSchema()+".bknom\n" +
                            "                WHERE\n" +
                            "                  ctab  ='001'\n" +
                            "                AND cacc=a.age\n" +
                            "              )\n" +
                            "              , 'DDMMYYYY')\n" +
                            "            AND\n" +
                            "              (\n" +
                            "                y.ncp  = a.ncp\n" +
                            "              OR y.cha =a.cha\n" +
                            "              OR y.cpro=a.cpro\n" +
                            "              )\n" +
                            "          )\n" +
                            "          ,0) + NVL(\n" +
                            "          (\n" +
                            "            SELECT\n" +
                            "              NVL(SUM(sin),0)\n" +
                            "            FROM\n" +
                            "              "+getSchema()+".bkaplf z,\n" +
                            "              "+getSchema()+".bkaplfr y,\n" +
                            "              "+getSchema()+".bkcom x\n" +
                            "            WHERE\n" +
                            "              x.sin     <0\n" +
                            "            AND z.cli   =x.cli\n" +
                            "            AND z.cli   = a.cli\n" +
                            "            AND z.nplf  = y.nplf\n" +
                            "            AND z.cli   =y.cli\n" +
                            "            AND z.ddeb <=to_date(\n" +
                            "              (\n" +
                            "                SELECT\n" +
                            "                  SUBSTR('000'\n" +
                            "                  || (\n" +
                            "                    CASE\n" +
                            "                      WHEN mnt2 = 0\n" +
                            "                      THEN mnt1\n" +
                            "                      ELSE mnt2\n" +
                            "                    END),-8,8)\n" +
                            "                FROM\n" +
                            "                  "+getSchema()+".bknom\n" +
                            "                WHERE\n" +
                            "                  ctab  ='001'\n" +
                            "                AND cacc=a.age\n" +
                            "              )\n" +
                            "              , 'DDMMYYYY')\n" +
                            "            AND z.dech>to_date(\n" +
                            "              (\n" +
                            "                SELECT\n" +
                            "                  SUBSTR('000'\n" +
                            "                  || (\n" +
                            "                    CASE\n" +
                            "                      WHEN mnt2 = 0\n" +
                            "                      THEN mnt1\n" +
                            "                      ELSE mnt2\n" +
                            "                    END),-8,8)\n" +
                            "                FROM\n" +
                            "                  "+getSchema()+".bknom\n" +
                            "                WHERE\n" +
                            "                  ctab  ='001'\n" +
                            "                AND cacc=a.age\n" +
                            "              )\n" +
                            "              , 'DDMMYYYY')\n" +
                            "            AND\n" +
                            "              (\n" +
                            "                y.ncp  = x.ncp\n" +
                            "              OR y.cha =x.cha\n" +
                            "              OR y.cpro=x.cpro\n" +
                            "              )\n" +
                            "          )\n" +
                            "          ,0) + NVL(\n" +
                            "          (\n" +
                            "            SELECT\n" +
                            "              maut\n" +
                            "            FROM\n" +
                            "              "+getSchema()+".bkautc\n" +
                            "            WHERE\n" +
                            "              age    =a.age\n" +
                            "            AND ncp  =a.ncp\n" +
                            "            AND eta IN ('VA','FO','VF')\n" +
                            "            AND to_date(\n" +
                            "              (\n" +
                            "                SELECT\n" +
                            "                  SUBSTR('000'\n" +
                            "                  ||(\n" +
                            "                    CASE\n" +
                            "                      WHEN mnt2 = 0\n" +
                            "                      THEN mnt1\n" +
                            "                      ELSE mnt2\n" +
                            "                    END),-8,-8)\n" +
                            "                FROM\n" +
                            "                  "+getSchema()+".bknom\n" +
                            "                WHERE\n" +
                            "                  ctab  ='001'\n" +
                            "                AND cacc=a.age\n" +
                            "              )\n" +
                            "              ,'DDMMYYYY') BETWEEN debut AND fin\n" +
                            "          )\n" +
                            "          ,0) -NVL(\n" +
                            "          (\n" +
                            "            SELECT\n" +
                            "              SUM(mon)\n" +
                            "            FROM\n" +
                            "              "+getSchema()+".bkreserv a\n" +
                            "            WHERE\n" +
                            "              agecpt  =a.age\n" +
                            "            AND ncpcpt=a.ncp\n" +
                            "            AND devcpt=a.dev\n" +
                            "            AND dech  >to_date(\n" +
                            "              (\n" +
                            "                SELECT\n" +
                            "                  SUBSTR('000'\n" +
                            "                  || (\n" +
                            "                    CASE\n" +
                            "                      WHEN mnt2 = 0\n" +
                            "                      THEN mnt1\n" +
                            "                      ELSE mnt2\n" +
                            "                    END),-8,8)\n" +
                            "                FROM\n" +
                            "                  "+getSchema()+".bknom\n" +
                            "                WHERE\n" +
                            "                  ctab  ='001'\n" +
                            "                AND cacc=a.age\n" +
                            "              )\n" +
                            "              , 'DDMMYYYY')\n" +
                            "            AND dco<=to_date(\n" +
                            "              (\n" +
                            "                SELECT\n" +
                            "                  SUBSTR('000'\n" +
                            "                  || (\n" +
                            "                    CASE\n" +
                            "                      WHEN mnt2 = 0\n" +
                            "                      THEN mnt1\n" +
                            "                      ELSE mnt2\n" +
                            "                    END),-8,8)\n" +
                            "                FROM\n" +
                            "                  "+getSchema()+".bknom\n" +
                            "                WHERE\n" +
                            "                  ctab  ='001'\n" +
                            "                AND cacc=a.age\n" +
                            "              )\n" +
                            "              , 'DDMMYYYY')\n" +
                            "            AND eta IN ('VA','VF','FO')\n" +
                            "          )\n" +
                            "          ,0)-NVL(\n" +
                            "          (\n" +
                            "            SELECT\n" +
                            "              MAX(val)\n" +
                            "            FROM\n" +
                            "              "+getSchema()+".bkcond\n" +
                            "            WHERE\n" +
                            "              nat    = 'SMCLIV'\n" +
                            "            AND ope IN ('594','595','596')\n" +
                            "            AND\n" +
                            "              (\n" +
                            "                dfin  IS NULL\n" +
                            "              OR dfin >=(to_date(\n" +
                            "                (\n" +
                            "                  SELECT\n" +
                            "                    SUBSTR('000'\n" +
                            "                    || (\n" +
                            "                      CASE\n" +
                            "                        WHEN mnt2=0\n" +
                            "                        THEN mnt1\n" +
                            "                        ELSE mnt2\n" +
                            "                      END),-8,8)\n" +
                            "                  FROM\n" +
                            "                    "+getSchema()+".bknom\n" +
                            "                  WHERE\n" +
                            "                    ctab   ='001'\n" +
                            "                  AND cacc =a.age\n" +
                            "                )\n" +
                            "                , 'DDMMYYYY'))\n" +
                            "              )\n" +
                            "          )\n" +
                            "          ,0)\n" +
                            "        )\n" +
                            "        <=0\n" +
                            "      THEN 0\n" +
                            "      ELSE (a.sin+ NVL(\n" +
                            "        (\n" +
                            "          SELECT\n" +
                            "            NVL(SUM(mplf),0)\n" +
                            "          FROM\n" +
                            "            "+getSchema()+".bkaplf z,\n" +
                            "            "+getSchema()+".bkaplfr y\n" +
                            "          WHERE\n" +
                            "            z.cli     = a.cli\n" +
                            "          AND z.nplf  = y.nplf\n" +
                            "          AND z.cli   =y.cli\n" +
                            "          AND z.ddeb <=to_date(\n" +
                            "            (\n" +
                            "              SELECT\n" +
                            "                SUBSTR('000'\n" +
                            "                || (\n" +
                            "                  CASE\n" +
                            "                    WHEN mnt2 = 0\n" +
                            "                    THEN mnt1\n" +
                            "                    ELSE mnt2\n" +
                            "                  END),-8,8)\n" +
                            "              FROM\n" +
                            "                "+getSchema()+".bknom\n" +
                            "              WHERE\n" +
                            "                ctab  ='001'\n" +
                            "              AND cacc=a.age\n" +
                            "            )\n" +
                            "            , 'DDMMYYYY')\n" +
                            "          AND z.dech>to_date(\n" +
                            "            (\n" +
                            "              SELECT\n" +
                            "                SUBSTR('000'\n" +
                            "                || (\n" +
                            "                  CASE\n" +
                            "                    WHEN mnt2 = 0\n" +
                            "                    THEN mnt1\n" +
                            "                    ELSE mnt2\n" +
                            "                  END),-8,8)\n" +
                            "              FROM\n" +
                            "                "+getSchema()+".bknom\n" +
                            "              WHERE\n" +
                            "                ctab  ='001'\n" +
                            "              AND cacc=a.age\n" +
                            "            )\n" +
                            "            , 'DDMMYYYY')\n" +
                            "          AND\n" +
                            "            (\n" +
                            "              y.ncp  = a.ncp\n" +
                            "            OR y.cha =a.cha\n" +
                            "            OR y.cpro=a.cpro\n" +
                            "            )\n" +
                            "        )\n" +
                            "        ,0) + NVL(\n" +
                            "        (\n" +
                            "          SELECT\n" +
                            "            NVL(SUM(sin),0)\n" +
                            "          FROM\n" +
                            "            "+getSchema()+".bkaplf z,\n" +
                            "            "+getSchema()+".bkaplfr y,\n" +
                            "            "+getSchema()+".bkcom x\n" +
                            "          WHERE\n" +
                            "            x.sin     <0\n" +
                            "          AND z.cli   =x.cli\n" +
                            "          AND z.cli   = a.cli\n" +
                            "          AND z.nplf  = y.nplf\n" +
                            "          AND z.cli   =y.cli\n" +
                            "          AND z.ddeb <=to_date(\n" +
                            "            (\n" +
                            "              SELECT\n" +
                            "                SUBSTR('000'\n" +
                            "                || (\n" +
                            "                  CASE\n" +
                            "                    WHEN mnt2 = 0\n" +
                            "                    THEN mnt1\n" +
                            "                    ELSE mnt2\n" +
                            "                  END),-8,8)\n" +
                            "              FROM\n" +
                            "                "+getSchema()+".bknom\n" +
                            "              WHERE\n" +
                            "                ctab  ='001'\n" +
                            "              AND cacc=a.age\n" +
                            "            )\n" +
                            "            , 'DDMMYYYY')\n" +
                            "          AND z.dech>to_date(\n" +
                            "            (\n" +
                            "              SELECT\n" +
                            "                SUBSTR('000'\n" +
                            "                || (\n" +
                            "                  CASE\n" +
                            "                    WHEN mnt2 = 0\n" +
                            "                    THEN mnt1\n" +
                            "                    ELSE mnt2\n" +
                            "                  END),-8,8)\n" +
                            "              FROM\n" +
                            "                "+getSchema()+".bknom\n" +
                            "              WHERE\n" +
                            "                ctab  ='001'\n" +
                            "              AND cacc=a.age\n" +
                            "            )\n" +
                            "            , 'DDMMYYYY')\n" +
                            "          AND\n" +
                            "            (\n" +
                            "              y.ncp  = x.ncp\n" +
                            "            OR y.cha =x.cha\n" +
                            "            OR y.cpro=x.cpro\n" +
                            "            )\n" +
                            "        )\n" +
                            "        ,0) + NVL(\n" +
                            "        (\n" +
                            "          SELECT\n" +
                            "            maut\n" +
                            "          FROM\n" +
                            "            "+getSchema()+".bkautc\n" +
                            "          WHERE\n" +
                            "            age    =a.age\n" +
                            "          AND ncp  =a.ncp\n" +
                            "          AND eta IN ('VA','FO','VF')\n" +
                            "          AND to_date(\n" +
                            "            (\n" +
                            "              SELECT\n" +
                            "                SUBSTR('000'\n" +
                            "                ||(\n" +
                            "                  CASE\n" +
                            "                    WHEN mnt2 = 0\n" +
                            "                    THEN mnt1\n" +
                            "                    ELSE mnt2\n" +
                            "                  END),-8,-8)\n" +
                            "              FROM\n" +
                            "                "+getSchema()+".bknom\n" +
                            "              WHERE\n" +
                            "                ctab  ='001'\n" +
                            "              AND cacc=a.age\n" +
                            "            )\n" +
                            "            ,'DDMMYYYY') BETWEEN debut AND fin\n" +
                            "        )\n" +
                            "        ,0) -NVL(\n" +
                            "        (\n" +
                            "          SELECT\n" +
                            "            SUM(mon)\n" +
                            "          FROM\n" +
                            "            "+getSchema()+".bkreserv a\n" +
                            "          WHERE\n" +
                            "            agecpt  =a.age\n" +
                            "          AND ncpcpt=a.ncp\n" +
                            "          AND devcpt=a.dev\n" +
                            "          AND dech  >to_date(\n" +
                            "            (\n" +
                            "              SELECT\n" +
                            "                SUBSTR('000'\n" +
                            "                || (\n" +
                            "                  CASE\n" +
                            "                    WHEN mnt2 = 0\n" +
                            "                    THEN mnt1\n" +
                            "                    ELSE mnt2\n" +
                            "                  END),-8,8)\n" +
                            "              FROM\n" +
                            "                "+getSchema()+".bknom\n" +
                            "              WHERE\n" +
                            "                ctab  ='001'\n" +
                            "              AND cacc=a.age\n" +
                            "            )\n" +
                            "            , 'DDMMYYYY')\n" +
                            "          AND dco<=to_date(\n" +
                            "            (\n" +
                            "              SELECT\n" +
                            "                SUBSTR('000'\n" +
                            "                || (\n" +
                            "                  CASE\n" +
                            "                    WHEN mnt2 = 0\n" +
                            "                    THEN mnt1\n" +
                            "                    ELSE mnt2\n" +
                            "                  END),-8,8)\n" +
                            "              FROM\n" +
                            "                "+getSchema()+".bknom\n" +
                            "              WHERE\n" +
                            "                ctab  ='001'\n" +
                            "              AND cacc=a.age\n" +
                            "            )\n" +
                            "            , 'DDMMYYYY')\n" +
                            "          AND eta IN ('VA','VF','FO')\n" +
                            "        )\n" +
                            "        ,0)-(\n" +
                            "          CASE\n" +
                            "            WHEN a.cha LIKE '373%'\n" +
                            "            THEN NVL(\n" +
                            "              (\n" +
                            "                SELECT\n" +
                            "                  MAX(val)\n" +
                            "                FROM\n" +
                            "                  "+getSchema()+".bkcond\n" +
                            "                WHERE\n" +
                            "                  nat    = 'SMCLIV'\n" +
                            "                AND ope IN ('594','595','596')\n" +
                            "                AND\n" +
                            "                  (\n" +
                            "                    dfin  IS NULL\n" +
                            "                  OR dfin >=(to_date(\n" +
                            "                    (\n" +
                            "                      SELECT\n" +
                            "                        SUBSTR('000'\n" +
                            "                        || (\n" +
                            "                          CASE\n" +
                            "                            WHEN mnt2=0\n" +
                            "                            THEN mnt1\n" +
                            "                            ELSE mnt2\n" +
                            "                          END),-8,8)\n" +
                            "                      FROM\n" +
                            "                        "+getSchema()+".bknom\n" +
                            "                      WHERE\n" +
                            "                        ctab   ='001'\n" +
                            "                      AND cacc =a.age\n" +
                            "                    )\n" +
                            "                    , 'DDMMYYYY'))\n" +
                            "                  )\n" +
                            "              )\n" +
                            "              ,0)\n" +
                            "            ELSE 0\n" +
                            "          END) )\n" +
                            "    END) sde_dispo,\n" +
                            "  a.sin solde\n" +
                            "FROM\n" +
                            "  "+getSchema()+".bkcom a\n" +
                            "WHERE\n" +
                            "  a.age  = '"+agence+"' " +
                            "AND a.ncp = '"+compte+"'";
                try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            try{
                rs2 = rs;
                response = convert(rs2);
                System.out.println("yann this is the checkClient :"+response.toList().toString());
                System.out.println("yann this is the checkClient :"+response.toString());
                System.out.println("yann this is the checkClient :"+response);
            
            }
            catch(Exception ex){
                
            }
            
            while (rs.next()) {
                result = 1;
                isAbonneEtebac = rs.getInt("ETB") ;
                isEventAttente = rs.getInt("EVE") ;
                isOppCli = rs.getInt("OPPCLI") ;
                isOppCom = rs.getInt("OPPCOM") ;
                SOLDE_INDICATIF = rs.getBigDecimal("SDE_DISPO");
            }
            //String GIMAC = getCompteTVACOMM_GIMAC()[3].replaceAll(" ", "");
            String ncp = agence+compte;
            //System.out.println("compte gimac "+GIMAC+"== compte client "+ncp);
            //System.out.println("Status cpt :"+ncp.equalsIgnoreCase(GIMAC));
            if(isAbonneEtebac==0){
            
                return new String[]{"0","157","No Etebac Subscription"};
            }
            if(isEventAttente==0){
            
              return new String[]{"0","161","Pending Event"};
            }
            if(isOppCli==0){
            
                return new String[]{"0","105","Account Blocked"};
            }
            if(isOppCom==0){
            
                return new String[]{"0","105","Account Blocked"};
            }
            BigDecimal SOLDE = new BigDecimal(montant);
            BigDecimal SOLDEFRAIS = new BigDecimal(frais);
            BigDecimal TVA = new BigDecimal("19.25");
            BigDecimal SOLDETVA = SOLDEFRAIS.multiply(TVA).divide(new BigDecimal("100"));
                    
            System.out.println("*******yann see this is the balance*******"+SOLDE_INDICATIF);
            if(SOLDE_INDICATIF.subtract(SOLDETVA).subtract(SOLDEFRAIS).compareTo(SOLDE) < 0){
              
                return new String[]{"0","151","Insufficient Balance"};
            }
            
            return new String[]{"1","01", "Success"};         
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
            e.printStackTrace();
        } finally {
//            if (stm3 != null) { stm3.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) {  }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) {  }
        }   
        connection.close();
        if (connection.isClosed())System.out.println("Connection 2 closed.");
         
        return new String[]{"0","120","Account not found"};         

    }
    */
    
    public String[] checkClient(String agence, String compte,String montant,String frais ) throws SQLException, ClassNotFoundException {
        ORACLE_CON_PARAM = getOracleCon();
        BigDecimal SOLDE_DISPONIBLE = null,SOLDE_INDICATIF = null ;
        JSONArray response = new JSONArray();
        int result = 0;
        JSONObject ret = new JSONObject();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        
        try {
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Integer isAbonneEtebac=0;
        Integer isEventAttente=0;
        Integer isOppCli=0;
        Integer isOppCom=0;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        String auto = "";
        //String query = "select (case when (a.age, a.ncp) in (select age, ncp from "+getSchema()+".bkdetebac) then 1 else 0 end )etb,(select (case when exists (select 1 from "+getSchema()+".bkeve where eta='AT' and ((age1=a.age and ncp1=a.ncp))) then 0 else 1 end) from dual)eve,(select (case when exists (select 1 from "+getSchema()+".bkoppcli where eta='V' and cli=(select cli from "+getSchema()+".bkcom where age=a.age and ncp=a.ncp) and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcli,(select (case when exists (select 1 from "+getSchema()+".bkoppcom where eta='V' and age=a.age and ncp=a.ncp and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcom,( case when (a.sin+nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||mnt2,-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0)-nvl((select sum(mon) from "+getSchema()+".bkreserv a where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| mnt2,-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| mnt2,-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl((case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat = 'SMCLIV' and ope in ('594','595','596')and (dfin is null or dfin >=(to_date((SELECT SUBSTR('000' || ( CASE WHEN mnt2=0 THEN mnt4 ELSE mnt2 END),-8,8) FROM "+getSchema()+".bknom WHERE ctab='001' AND cacc  =a.age), 'DDMMYYYY')))) else 0 end),0)) <=0 then 0 else (a.sin+nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||mnt2,-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0)-nvl((select sum(mon) from "+getSchema()+".bkreserv a where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| mnt2,-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| mnt2,-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl((case when a.CHA LIKE '373%' then(select max(val) from "+getSchema()+".bkcond where nat = 'SMCLIV' and ope in ('594','595','596') and (dfin is null or dfin >=(to_date((SELECT SUBSTR('000' || (CASE WHEN mnt2=0 THEN mnt4 ELSE mnt2 END),-8,8)FROM "+getSchema()+".bknom WHERE ctab='001' AND cacc  =a.age  ), 'DDMMYYYY'))))else 0 end),0)) end) sde_dispo, a.sin solde from "+getSchema()+".bkcom a where a.age='"+agence+"' and a.ncp='"+compte+"'";

        //String query = "select (case when (a.age, a.ncp) in (select age, ncp from  "+getSchema()+".bkdetebac) then 1 else 0 end )etb,(select (case when exists (select 1 from  "+getSchema()+".bkeve where eta='AT' and ((age1=a.age and ncp1=a.ncp and sen1='D') or (age2=a.age and ncp2=a.ncp and sen2='D'))) then 0 else 1 end) from dual)eve, (select (case when exists (select 1 from  "+getSchema()+".bkoppcli where eta='V' and cli=(select cli from  "+getSchema()+".bkcom where age=a.age and ncp=a.ncp) and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcli, (select (case when exists (select 1 from  "+getSchema()+".bkoppcom where eta='V' and age=a.age and ncp=a.ncp and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcom, ( case when (a.sin+nvl((select maut from  "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||mnt2,-8, 8) from  "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0)-nvl((select sum(mon) from  "+getSchema()+".bkreserv a where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| mnt2,-8,8) from  "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| mnt2,-8,8) from  "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0) -nvl((case when a.CHA LIKE '373%' then (select max(val) from  "+getSchema()+".bkcond where nat = 'SMCLIV' and ope in ('594','595','596')and (dfin is null or dfin >=(to_date((SELECT SUBSTR('000' || ( CASE WHEN mnt2=0 THEN mnt4 ELSE mnt2 END),-8,8) FROM  "+getSchema()+".bknom WHERE ctab='001' AND cacc  =a.age), 'DDMMYYYY')))) else 0 end),0)) <=0 then 0 else (a.sin+nvl((select maut from  "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||mnt2,-8, 8) from  "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl((select sum(mon) from  "+getSchema()+".bkreserv a where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| mnt2,-8,8) from  "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| mnt2,-8,8) from  "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl((case when a.CHA LIKE '373%' then(select max(val) from  "+getSchema()+".bkcond where nat = 'SMCLIV' and ope in ('594','595','596') and (dfin is null or dfin >=(to_date((SELECT SUBSTR('000' || (CASE WHEN mnt2=0 THEN mnt4 ELSE mnt2 END),-8,8)FROM  "+getSchema()+".bknom WHERE ctab='001' AND cacc  =a.age  ), 'DDMMYYYY'))))else 0 end),0) ) end) sde_dispo, nvl((select maut from  "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||mnt2,-8, 8) from  "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) auto, a.sin solde from  "+getSchema()+".bkcom a where a.age='"+agence+"' and a.ncp='"+compte+"'";
        //System.out.println("yann see this your query:"+query);
        
        //String query = "select (case when (a.age, a.ncp) in (select age, ncp from "+getSchema()+".bkdetebac) then 1 else 0 end )etb, (select (case when exists (select 1 from "+getSchema()+".bkeve where eta='AT' and ((age1=a.age and ncp1=a.ncp))) then 0 else 1 end) from dual)eve, (select (case when exists (select 1 from "+getSchema()+".bkoppcli where eta='V' and cli=(select cli from "+getSchema()+".bkcom where age=a.age and ncp=a.ncp) and (dfin is null or dfin >current_date) ) then 0 else 1 end) from dual) oppcli, (select (case when exists (select 1 from "+getSchema()+".bkoppcom where eta='V' and age=a.age and ncp=a.ncp and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcom, ( case when (a.sin+nvl((select sum(mon) from "+getSchema()+".bksin where age=a.age and ncp=a.ncp and dev=a.dev),0)+ nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0)-nvl( (select sum(mon) from "+getSchema()+".bkreserv a where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and /*dech >=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and*/ dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('367', '368', '369', '370','371','372','373','374') and ban='O') else 0 end,0)) <=0 then 0 else (a.sin+nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl((select sum(mon) from "+getSchema()+".bkreserv a where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev /*and dech >=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') */ and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('367', '368', '369', '370','371','372','373','374') and ban='O') else 0 end,0) +nvl((select sum(mon) from "+getSchema()+".bksin where ncp = a.ncp and age=a.age),0) ) end) sde_dispo, a.sin solde from "+getSchema()+".bkcom a where a.age='"+agence+"' and a.ncp='"+compte+"'"; 

        //String query = "select (case when (a.age, a.ncp) in (select age, ncp from "+getSchema()+".bkdetebac) then 1 else 0 end )etb, (select (case when exists (select 1 from "+getSchema()+".bkeve where eta='AT' and ((age1=a.age and ncp1=a.ncp))) then 0 else 1 end) from dual)eve, (select (case when exists (select 1 from "+getSchema()+".bkoppcli where eta='V' and cli=(select cli from "+getSchema()+".bkcom where age=a.age and ncp=a.ncp) and (dfin is null or dfin >current_date) ) then 0 else 1 end) from dual) oppcli, (select (case when exists (select 1 from "+getSchema()+".bkoppcom where eta='V' and age=a.age and ncp=a.ncp and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcom, ( case when (a.sin+nvl((select sum(mon) from "+getSchema()+".bksin where age=a.age and ncp=a.ncp and dev=a.dev),0)+ nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl( (select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('367', '368', '369', '370','371','372','373','374') and ban='O') else 0 end,0)) <=0 then 0 else (a.sin+nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl((select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('367', '368', '369', '370','371','372','373','374') and ban='O') else 0 end,0) +nvl((select sum(mon) from "+getSchema()+".bksin where ncp = a.ncp and age=a.age),0) ) end) sde_dispo, a.sin solde from "+getSchema()+".bkcom a where a.age='"+agence+"' and a.ncp='"+compte+"'";
        
        //String query = "select (case when (a.age, a.ncp) in (select age, ncp from "+getSchema()+".bkdetebac) then 1 else 0 end )etb, (select (case when exists (select 1 from "+getSchema()+".bkeve where eta='AT' and ((age1=a.age and ncp1=a.ncp))) then 0 else 1 end) from dual)eve, (select (case when exists (select 1 from "+getSchema()+".bkoppcli where eta='V' and cli=(select cli from "+getSchema()+".bkcom where age=a.age and ncp=a.ncp) and (dfin is null or dfin >current_date) ) then 0 else 1 end) from dual) oppcli, (select (case when exists (select 1 from "+getSchema()+".bkoppcom where eta='V' and age=a.age and ncp=a.ncp and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcom, ( case when (a.sin+nvl((select sum(mon) from "+getSchema()+".bksin where age=a.age and ncp=a.ncp and dev=a.dev),0)+ nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl( (select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('594','595','596') and ban='O') else 0 end,0)) <=0 then 0 else (a.sin+nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl((select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('594','595','596') and ban='O') else 0 end,0) +nvl((select sum(mon) from "+getSchema()+".bksin where ncp = a.ncp and age=a.age),0) ) end) sde_dispo, a.sin solde from "+getSchema()+".bkcom a where a.age='"+agence+"' and a.ncp='"+compte+"'";
        
        String query = "select (case when (a.age, a.ncp) in (select age, ncp from "+getSchema()+".bkdetebac) then 1 else 0 end )etb, (select (case when exists (select 1 from "+getSchema()+".bkeve where eta='AT' and ((age1=a.age and ncp1=a.ncp))) then 0 else 1 end) from dual)eve, (select (case when exists (select 1 from "+getSchema()+".bkoppcli where eta='V' and cli=(select cli from "+getSchema()+".bkcom where age=a.age and ncp=a.ncp) and (dfin is null or dfin >current_date) ) then 0 else 1 end) from dual) oppcli, (select (case when exists (select 1 from "+getSchema()+".bkoppcom where eta='V' and age=a.age and ncp=a.ncp and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcom, nvl((select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)reserve, ( case when (a.sin+nvl((select sum(mon) from "+getSchema()+".bksin where age=a.age and ncp=a.ncp and dev=a.dev),0) + nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl( (select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0) -nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('594','595','596') and ban='O') else 0 end,0)) <=0 then 0 else (a.sin+nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl((select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0) -nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('594','595','596') and ban='O') else 0 end,0) +nvl((select sum(mon) from "+getSchema()+".bksin where ncp = a.ncp and age=a.age),0) ) end) sde_dispo, a.sin solde from "+getSchema()+".bkcom a where a.age='"+agence+"' and a.ncp='"+compte+"'";
        
       
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            /*
            try{
                rs2 = rs;
                response = convert(rs2);
                System.out.println("yann this is the checkClient :"+response.toList().toString());
                System.out.println("yann this is the checkClient :"+response.toString());
                System.out.println("yann this is the checkClient :"+response);
            
            }
            catch(Exception ex){
                
            }
            */
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
                
                result = 1;
                isAbonneEtebac = rs.getInt("ETB") ;
                isEventAttente = rs.getInt("EVE") ;
                isOppCli = rs.getInt("OPPCLI") ;
                isOppCom = rs.getInt("OPPCOM") ;
                SOLDE_INDICATIF = rs.getBigDecimal("SDE_DISPO");
            }
            //String GIMAC = getCompteTVACOMM_GIMAC()[3].replaceAll(" ", "");
            String ncp = agence+compte;
            if(isAbonneEtebac==0){
            
                return new String[]{"0","157","No Etebac Subscription"};
            }
            if(isEventAttente==0){
            
              return new String[]{"0","161","Pending Event"};
            }
            if(isOppCli==0){
            
                return new String[]{"0","105","Account Blocked"};
            }
            if(isOppCom==0){
            
                return new String[]{"0","105","Account Blocked"};
            }
            BigDecimal SOLDE = new BigDecimal(montant);
            BigDecimal SOLDEFRAIS = new BigDecimal(frais);
            BigDecimal TVA = new BigDecimal("19.25");
            BigDecimal SOLDETVA = SOLDEFRAIS.multiply(TVA).divide(new BigDecimal("100"));
                    
            System.out.println("*******yann see this is the balance*******"+SOLDE_INDICATIF);
            if(SOLDE_INDICATIF.subtract(SOLDETVA).subtract(SOLDEFRAIS).compareTo(SOLDE) < 0){
              
                return new String[]{"0","151","Insufficient Balance"};
            }
            
            return new String[]{"1","01", "Success"};         
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
            e.printStackTrace();
        } finally {
//            if (stm3 != null) { stm3.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }   
        connection.close();
        if (connection.isClosed())System.out.println("Connection 2 closed.");
         
        return new String[]{"0","120","Account not found"};         

    }
    
    public String getSchema(){
        Nomenclature nomen = nomenclatureRepository.findTabcdAndAcsd("0012","0123","0");
        if (nomen != null){
            return nomen.getLib2();
        }
        return "bank";
    }
    
    public Map<String, Object> getSolde(String agence, String compte ) throws SQLException, ClassNotFoundException {
        ORACLE_CON_PARAM = getOracleCon();
        BigDecimal SOLDE_DISPONIBLE = null,SOLDE_INDICATIF = null ;
        JSONArray response1 = new JSONArray();
        int result = 0;
        JSONObject ret = new JSONObject();
        Map<String, Object> response = new HashMap();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        
        try {
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Integer isAbonneEtebac=0;
        Integer isEventAttente=0;
        Integer isOppCli=0;
        Integer isOppCom=0;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        
        //String query = "select (case when (a.age, a.ncp) in (select age, ncp from "+getSchema()+".bkdetebac) then 1 else 0 end )etb,(select (case when exists (select 1 from "+getSchema()+".bkeve where eta='AT' and ((age1=a.age and ncp1=a.ncp))) then 0 else 1 end) from dual)eve,(select (case when exists (select 1 from "+getSchema()+".bkoppcli where eta='V' and cli=(select cli from "+getSchema()+".bkcom where age=a.age and ncp=a.ncp) and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcli,(select (case when exists (select 1 from "+getSchema()+".bkoppcom where eta='V' and age=a.age and ncp=a.ncp and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcom,( case when (a.sin+nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||mnt2,-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0)-nvl((select sum(mon) from "+getSchema()+".bkreserv a where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| mnt2,-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| mnt2,-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl((case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat = 'SMCLIV' and ope in ('594','595','596')and (dfin is null or dfin >=(to_date((SELECT SUBSTR('000' || ( CASE WHEN mnt2=0 THEN mnt4 ELSE mnt2 END),-8,8) FROM "+getSchema()+".bknom WHERE ctab='001' AND cacc  =a.age), 'DDMMYYYY')))) else 0 end),0)) <=0 then 0 else (a.sin+nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||mnt2,-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0)-nvl((select sum(mon) from "+getSchema()+".bkreserv a where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| mnt2,-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| mnt2,-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl((case when a.CHA LIKE '373%' then(select max(val) from "+getSchema()+".bkcond where nat = 'SMCLIV' and ope in ('594','595','596') and (dfin is null or dfin >=(to_date((SELECT SUBSTR('000' || (CASE WHEN mnt2=0 THEN mnt4 ELSE mnt2 END),-8,8)FROM "+getSchema()+".bknom WHERE ctab='001' AND cacc  =a.age  ), 'DDMMYYYY'))))else 0 end),0)) end) sde_dispo, a.sin solde from "+getSchema()+".bkcom a where a.age='"+agence+"' and a.ncp='"+compte+"'";

        //String query = "select (case when (a.age, a.ncp) in (select age, ncp from "+getSchema()+".bkdetebac) then 1 else 0 end )etb, (select (case when exists (select 1 from "+getSchema()+".bkeve where eta='AT' and ((age1=a.age and ncp1=a.ncp))) then 0 else 1 end) from dual)eve, (select (case when exists (select 1 from "+getSchema()+".bkoppcli where eta='V' and cli=(select cli from "+getSchema()+".bkcom where age=a.age and ncp=a.ncp) and (dfin is null or dfin >current_date) ) then 0 else 1 end) from dual) oppcli, (select (case when exists (select 1 from "+getSchema()+".bkoppcom where eta='V' and age=a.age and ncp=a.ncp and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcom, ( case when (a.sin+nvl((select sum(mon) from "+getSchema()+".bksin where age=a.age and ncp=a.ncp and dev=a.dev),0)+ nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0)-nvl( (select sum(mon) from "+getSchema()+".bkreserv a where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and /*dech >=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and*/ dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('367', '368', '369', '370','371','372','373','374') and ban='O') else 0 end,0)) <=0 then 0 else (a.sin+nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl((select sum(mon) from "+getSchema()+".bkreserv a where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev /*and dech >=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') */ and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('367', '368', '369', '370','371','372','373','374') and ban='O') else 0 end,0) +nvl((select sum(mon) from "+getSchema()+".bksin where ncp = a.ncp and age=a.age),0) ) end) sde_dispo, a.sin solde from "+getSchema()+".bkcom a where a.age='"+agence+"' and a.ncp='"+compte+"'"; 

        // String query = "select (case when (a.age, a.ncp) in (select age, ncp from "+getSchema()+".bkdetebac) then 1 else 0 end )etb, (select (case when exists (select 1 from "+getSchema()+".bkeve where eta='AT' and ((age1=a.age and ncp1=a.ncp))) then 0 else 1 end) from dual)eve, (select (case when exists (select 1 from "+getSchema()+".bkoppcli where eta='V' and cli=(select cli from "+getSchema()+".bkcom where age=a.age and ncp=a.ncp) and (dfin is null or dfin >current_date) ) then 0 else 1 end) from dual) oppcli, (select (case when exists (select 1 from "+getSchema()+".bkoppcom where eta='V' and age=a.age and ncp=a.ncp and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcom, ( case when (a.sin+nvl((select sum(mon) from "+getSchema()+".bksin where age=a.age and ncp=a.ncp and dev=a.dev),0)+ nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl( (select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('367', '368', '369', '370','371','372','373','374') and ban='O') else 0 end,0)) <=0 then 0 else (a.sin+nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl((select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('367', '368', '369', '370','371','372','373','374') and ban='O') else 0 end,0) +nvl((select sum(mon) from "+getSchema()+".bksin where ncp = a.ncp and age=a.age),0) ) end) sde_dispo, a.sin solde from "+getSchema()+".bkcom a where a.age='"+agence+"' and a.ncp='"+compte+"'";
        
/*       // String query = "select (case when (a.age, a.ncp) in (select age, ncp from "+getSchema()+".bkdetebac) then 1 else 0 end )etb, (select (case when exists (select 1 from "+getSchema()+".bkeve where eta='AT' and ((age1=a.age and ncp1=a.ncp))) then 0 else 1 end) from dual)eve, (select (case when exists (select 1 from "+getSchema()+".bkoppcli where eta='V' and cli=(select cli from "+getSchema()+".bkcom where age=a.age and ncp=a.ncp) and (dfin is null or dfin >current_date) ) then 0 else 1 end) from dual) oppcli, (select (case when exists (select 1 from "+getSchema()+".bkoppcom where eta='V' and age=a.age and ncp=a.ncp and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcom, ( case when (a.sin+nvl((select sum(mon) from "+getSchema()+".bksin where age=a.age and ncp=a.ncp and dev=a.dev),0)+ nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl( (select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('594','595','596') and ban='O') else 0 end,0)) <=0 then 0 else (a.sin+nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl((select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('594','595','596') and ban='O') else 0 end,0) +nvl((select sum(mon) from "+getSchema()+".bksin where ncp = a.ncp and age=a.age),0) ) end) sde_dispo, a.sin solde from "+getSchema()+".bkcom a where a.age='"+agence+"' and a.ncp='"+compte+"'";
        String query ="select (case when (a.age, a.ncp) in (select age, ncp from "+getSchema()+".bkdetebac) then 1 else 0 end )etb, \n" +
                        "(select (case when exists (select 1 from "+getSchema()+".bkeve where eta='AT' and ((age1=a.age and ncp1=a.ncp))) then 0 else 1 end) from dual)eve, \n" +
                        "(select (case when exists (select 1 from "+getSchema()+".bkoppcli where eta='V' and cli=(select cli from "+getSchema()+".bkcom where age=a.age and ncp=a.ncp) and \n" +
                        "(dfin is null or dfin >current_date) ) then 0 else 1 end) from dual) oppcli, \n" +
                        "(select (case when exists (select 1 from "+getSchema()+".bkoppcom where eta='V' \n" +
                        "and age=a.age and ncp=a.ncp and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcom, \n" +
                        "nvl((select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev  \n" +
                        "and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') \n" +
                        "and eta in ('VA','VF','FO')),0)reserve,\n" +
                        "( case when (a.sin+nvl((select sum(mon) from "+getSchema()+".bksin where age=a.age and ncp=a.ncp and dev=a.dev),0)\n" +
                        "+ nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) \n" +
                        "from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) \n" +
                        "-nvl( (select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev \n" +
                        "and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') \n" +
                        "and eta in ('VA','VF','FO')),0)\n" +
                        "-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('594','595','596') \n" +
                        "and ban='O') else 0 end,0)) <=0 then 0 else (a.sin+nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') \n" +
                        "and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) \n" +
                        "-nvl((select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev  \n" +
                        "and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') \n" +
                        "and eta in ('VA','VF','FO')),0)\n" +
                        "-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('594','595','596') and ban='O') else 0 end,0) \n" +
                        "+nvl((select sum(mon) from "+getSchema()+".bksin where ncp = a.ncp and age=a.age),0) ) end) sde_dispo, a.sin solde from "+getSchema()+".bkcom a where a.age='06800' and a.ncp='66666666666'";
*/
        //String query = "select (case when (a.age, a.ncp) in (select age, ncp from "+getSchema()+".bkdetebac) then 1 else 0 end )etb, (select (case when exists (select 1 from "+getSchema()+".bkeve where eta='AT' and ((age1=a.age and ncp1=a.ncp))) then 0 else 1 end) from dual)eve, (select (case when exists (select 1 from "+getSchema()+".bkoppcli where eta='V' and cli=(select cli from "+getSchema()+".bkcom where age=a.age and ncp=a.ncp) and (dfin is null or dfin >current_date) ) then 0 else 1 end) from dual) oppcli, (select (case when exists (select 1 from "+getSchema()+".bkoppcom where eta='V' and age=a.age and ncp=a.ncp and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcom, ( case when (a.sin+nvl((select sum(mon) from "+getSchema()+".bksin where age=a.age and ncp=a.ncp and dev=a.dev),0)+ nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl( (select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('594','595','596') and ban='O') else 0 end,0)) <=0 then 0 else (a.sin+nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl((select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dech >=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)-nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('594','595','596') and ban='O') else 0 end,0) +nvl((select sum(mon) from "+getSchema()+".bksin where ncp = a.ncp and age=a.age),0) ) end) sde_dispo, a.sin solde from "+getSchema()+".bkcom a where a.age='"+agence+"' and a.ncp='"+compte+"'";
        
        String query = "select (case when (a.age, a.ncp) in (select age, ncp from "+getSchema()+".bkdetebac) then 1 else 0 end )etb, (select (case when exists (select 1 from "+getSchema()+".bkeve where eta='AT' and ((age1=a.age and ncp1=a.ncp))) then 0 else 1 end) from dual)eve, (select (case when exists (select 1 from "+getSchema()+".bkoppcli where eta='V' and cli=(select cli from "+getSchema()+".bkcom where age=a.age and ncp=a.ncp) and (dfin is null or dfin >current_date) ) then 0 else 1 end) from dual) oppcli, (select (case when exists (select 1 from "+getSchema()+".bkoppcom where eta='V' and age=a.age and ncp=a.ncp and (dfin is null or dfin >current_date)) then 0 else 1 end) from dual) oppcom, nvl((select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0)reserve, ( case when (a.sin+nvl((select sum(mon) from "+getSchema()+".bksin where age=a.age and ncp=a.ncp and dev=a.dev),0) + nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl( (select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0) -nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('594','595','596') and ban='O') else 0 end,0)) <=0 then 0 else (a.sin+nvl((select maut from "+getSchema()+".bkautc where age=a.age and ncp=a.ncp and eta in ('VA','FO','VF') and to_date((select substr('000'||(case when mnt2!=0 then mnt2 else mnt1 end),-8, 8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age),'DDMMYYYY') between debut and fin),0) -nvl((select sum(mon) from "+getSchema()+".bkreserv where agecpt=a.age and ncpcpt=a.ncp and devcpt=a.dev and dco<=to_date((select substr('000'|| (case when mnt2!=0 then mnt2 else mnt1 end),-8,8) from "+getSchema()+".bknom where ctab='001' and cacc=a.age), 'DDMMYYYY') and eta in ('VA','VF','FO')),0) -nvl(case when a.CHA LIKE '373%' then (select max(val) from "+getSchema()+".bkcond where nat='SMCLIV' and ope in('594','595','596') and ban='O') else 0 end,0) +nvl((select sum(mon) from "+getSchema()+".bksin where ncp = a.ncp and age=a.age),0) ) end) sde_dispo, a.sin solde from "+getSchema()+".bkcom a where a.age='"+agence+"' and a.ncp='"+compte+"'";
        
        
        
        //System.out.println("yann see this query "+query);
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            
            /*try{
                rs2 = rs;
                response1 = convert(rs2);
                System.out.println("yann this is the checkSolde :"+response1.toList().toString());
                System.out.println("yann this is the checkSolde :"+response1.toString());
                System.out.println("yann this is the checkSolde :"+response1);
            }
            catch(Exception ex){
                
            }
            */
            while (rs.next()) {
                result = 1;
                isAbonneEtebac = rs.getInt("ETB") ;
                isEventAttente = rs.getInt("EVE") ;
                isOppCli = rs.getInt("OPPCLI") ;
                isOppCom = rs.getInt("OPPCOM") ;
                SOLDE_DISPONIBLE = rs.getBigDecimal("SDE_DISPO");
                SOLDE_INDICATIF = rs.getBigDecimal("SOLDE");
                System.out.println("yann please see these soldes "+SOLDE_DISPONIBLE +"   "+ SOLDE_INDICATIF);
            }
            //String GIMAC = getCompteTVACOMM_GIMAC()[3].replaceAll(" ", "");
            String ncp = agence+compte;
            //System.out.println("compte gimac "+GIMAC+"== compte client "+ncp);
            //System.out.println("Status cpt :"+ncp.equalsIgnoreCase(GIMAC));
            
            if(isOppCli==0){
                response.put("success", "100");
                response.put("data", "Account Blocked");
                return  response;
            }
            if(isOppCom==0){
                response.put("success", "100");
                response.put("data", "Account Blocked");
                return  response;
            }
            
            response.put("success", "01");
            response.put("data", SOLDE_INDICATIF.toString());
            response.put("solde_dispo", SOLDE_DISPONIBLE.toString());
            response.put("solde_indic", SOLDE_INDICATIF.toString());
            return  response;
                 
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
            e.printStackTrace();
        } finally {
//            if (stm3 != null) { stm3.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }   
        connection.close();
        if (connection.isClosed())System.out.println("Connection 2 closed.");
        
        response.put("success", "100");
        response.put("data", "Account not found");
        return  response;    

    }
    
    
    
    public JSONObject getMiniStatement(String agence, String compte , int number ) throws SQLException, ClassNotFoundException {
        ORACLE_CON_PARAM = getOracleCon();
        BigDecimal amount = null;
        String transactionId = null;
        String op_type = null;
        Date op_date = null;
        int result = 0;
        JSONObject ret = new JSONObject();
        JSONObject response = new JSONObject();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        
        try {
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs = null;
        
        ArrayList<JSONObject> historyList = new ArrayList();
        

        String query= "select * from "+getSchema()+".bkhis where ncp='"+compte+"' and age='"+agence+"' and ROWNUM <= "+number+" order by dco desc";
        System.out.println("yann see this query "+query);        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            int j = 0;
            while (rs.next()) {
                result = 1;
                transactionId = rs.getString("LIB") ;
                op_type = rs.getString("SEN") ;
                op_date = rs.getDate("dco");
                amount = rs.getBigDecimal("MON");
                
                //System.out.println("hey yann see this "+transactionId +" "+op_type +" "+ op_date+ " "+amount);
                JSONObject history = new JSONObject();
                history.put("amount", amount.toString());
                history.put("transaction_id", transactionId);
                history.put("op_type", op_type);
                history.put("op_date", op_date);
                
                //System.out.println("yann see history "+history.toString());
                
                historyList.add(history);
                j++;
            }
            
            if(result==0){
                response.put("success", "100");
                response.put("message", "some error occured");
                return  response;
            }
            System.out.println(" yann see history inside core "+historyList.toString());
            response.put("success", "01");
            response.put("data", historyList);
            return  response;
                 
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
            e.printStackTrace();
        } finally {
//            if (stm3 != null) { stm3.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }   
        connection.close();
        if (connection.isClosed())System.out.println("Connection 2 closed.");
        
        response.put("success", "100");
        response.put("data", "Account not found");
        return  response;    

    }
    
    
    
    
    public JSONObject getMiniStatementInEven(String agence, String compte , int number ) throws SQLException, ClassNotFoundException {

        ORACLE_CON_PARAM = getOracleCon();
        BigDecimal amount = null;
        String transactionId = null;
        String op_type = null;
        Date op_date = null;
        int result = 0;
        JSONObject ret = new JSONObject();
        JSONObject response = new JSONObject();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }        
        try {
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<JSONObject> historyList = new ArrayList();
       
        String query=  "select * from  "+getSchema()+".bkeve where  ncp1='"+compte+"' and  age1='"+agence+"' order by dsai,hsai desc";
        System.out.println("yann see this query "+query);        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            int j = 0;
            while (rs.next()) {
                result = 1;
                transactionId = rs.getString("LIB1") ;
                op_type = rs.getString("SEN1") ;
                op_date = rs.getDate("dco");
                amount = rs.getBigDecimal("MON1");
                
                //System.out.println("hey yann see this "+transactionId +" "+op_type +" "+ op_date+ " "+amount);
                JSONObject history = new JSONObject();
                history.put("amount", amount.toString());
                history.put("transaction_id", transactionId);
                history.put("op_type", op_type);
                history.put("op_date", op_date);
                
                //System.out.println("yann see history "+history.toString());
                
                historyList.add(history);
                j++;
            }
            
            if(result==0){
                response.put("success", "100");
                response.put("message", "some error occured");
                return  response;
            }
            System.out.println(" yann see history inside core "+historyList.toString());
            response.put("success", "01");
            response.put("data", historyList);
            return  response;
                 
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
            e.printStackTrace();
        } finally {
//            if (stm3 != null) { stm3.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }   
        connection.close();
        if (connection.isClosed())System.out.println("Connection 2 closed.");
        
        response.put("success", "100");
        response.put("data", "Account not found");
        return  response;    

    }
    
    
    
    
    public JSONObject getMiniStatementInEven2(String agence, String compte , int number ) throws SQLException, ClassNotFoundException {

        ORACLE_CON_PARAM = getOracleCon();
        BigDecimal amount = null;
        String transactionId = null;
        String op_type = null;
        Date op_date = null;
        int result = 0;
        JSONObject ret = new JSONObject();
        JSONObject response = new JSONObject();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }        
        try {
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<JSONObject> historyList = new ArrayList();
       
        String query=  "select * from  "+getSchema()+".bkeve where  ncp2='"+compte+"' and  age2='"+agence+"' order by dsai,hsai desc";
        System.out.println("yann see this query "+query);        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            int j = 0;
            while (rs.next()) {
                result = 1;
                transactionId = rs.getString("LIB1") ;
                op_type = rs.getString("SEN1") ;
                op_date = rs.getDate("dco");
                amount = rs.getBigDecimal("MON1");
                
                //System.out.println("hey yann see this "+transactionId +" "+op_type +" "+ op_date+ " "+amount);
                JSONObject history = new JSONObject();
                history.put("amount", amount.toString());
                history.put("transaction_id", transactionId);
                history.put("op_type", op_type);
                history.put("op_date", op_date);
                
                //System.out.println("yann see history "+history.toString());
                
                historyList.add(history);
                j++;
            }
            
            if(result==0){
                response.put("success", "100");
                response.put("message", "some error occured");
                return  response;
            }
            System.out.println(" yann see history inside core "+historyList.toString());
            response.put("success", "01");
            response.put("data", historyList);
            return  response;
                 
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
            e.printStackTrace();
        } finally {
//            if (stm3 != null) { stm3.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }   
        connection.close();
        if (connection.isClosed())System.out.println("Connection 2 closed.");
        
        response.put("success", "100");
        response.put("data", "Account not found");
        return  response;    

    }
    
    
    public JSONObject getOperationStatusBkhis(String agence, String compte , String pnbr ) throws SQLException, ClassNotFoundException {
        ORACLE_CON_PARAM = getOracleCon();
        String status = "" , status2 = "";
        int result = 0;
        JSONObject response = new JSONObject();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        
        try {
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs = null;
        
        String query = "select distinct (case when coalesce(eta,'EMPTY') in ('VA','VF','FO') then '0'\n" +
"when coalesce (eta, 'EMPTY') in ('AT') then '1' when coalesce(eta,'EMPTY') in ('IG','AB') then '2' else '3' end)res\n" +
"from "+getSchema()+".bkhis where lib ='"+pnbr+"' and age='"+agence+"' and ncp='"+compte+"'";
        
        //System.out.println("yann see query 1: "+query);
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            //System.out.println("yann rs1: "+rs.toString());
            int j = 0;
            if(rs!=null){
                while (rs.next()) {
                    
                    if(rs.getObject("RES").toString() != null){
                        result = 1;
                        status = rs.getObject("RES").toString() ;
                    }
                    
                }
                
            }
            
            if(result==0){
                response.put("success", "100");
                response.put("message", "some error occured");
                return  response;
            }
                //System.out.println(" yann see status inside core "+status.toString() );
                response.put("success", "01");
                response.put("status", status);
                return  response;
                 
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
//            if (stm3 != null) { stm3.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { 
                rs.close();
                if (rs.isClosed()){
                    System.out.println("Connection Resultset closed.");
                }
            
            
            
            } catch (Exception e) { /* ignored */System.out.println(e.getMessage()); }
        }   
        connection.close();
        if (connection.isClosed())System.out.println("Connection 2 closed.");
        
        response.put("success", "100");
        response.put("data", "Account not found");
        return  response;    

    }
    
    public JSONObject getOperationStatusBkeve(String agence, String compte , String pnbr ) throws SQLException, ClassNotFoundException {
        ORACLE_CON_PARAM = getOracleCon();
        String status = "" , status2 = "";
        int result = 0;
        JSONObject response = new JSONObject();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        
        try {
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs2 = null;
        
        
        String query2 = "select distinct (case when coalesce(eta,'EMPTY') in ('VA','VF','FO') then '0'\n" +
"when coalesce (eta, 'EMPTY') in ('AT') then '1' when coalesce(eta,'EMPTY') in ('IG','AB') then '2' else '3' end)res\n" +
"from "+getSchema()+".bkeve where lib1 ='"+pnbr+"' and age1='"+agence+"' and ncp1='"+compte+"'";
        
        //System.out.println("yann see query 1: "+query2);
        try {
            stmt = connection.createStatement();
            rs2 = stmt.executeQuery(query2);
            //System.out.println("yann rs2: "+rs2);
            int j = 0;
            if(rs2!=null){
                while (rs2.next()) {
                    //System.out.println("hello yann see this1 "+rs2.getObject("RES").toString());
                    if(rs2.getObject("RES").toString() != null){
                        //System.out.println("hello yann see this2 "+rs2.getObject("RES").toString());
                        result = 1;
                        status2 = rs2.getObject("RES").toString() ;
                    }
                    
                }
            }
            
            
            //System.out.println("hello yann see this ");
            
            if(result==0){
                response.put("success", "100");
                response.put("message", "some error occured");
                return  response;
            }
                //System.out.println(" yann see status inside core "+ status2.toString());
                response.put("success", "01");
                response.put("status", status2);
                return  response;
                 
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
//            if (stm3 != null) { stm3.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { 
                rs2.close();
                if (rs2.isClosed()){
                    System.out.println("Connection Resultset2 closed.");
                }
            
            
            
            } catch (Exception e) { /* ignored */ System.out.println(e.getMessage());}
        }   
        connection.close();
        if (connection.isClosed())System.out.println("Connection 2 closed.");
        
        response.put("success", "100");
        response.put("data", "Account not found");
        return  response;    

    }
    
    
    public String checkTfjo() throws SQLException, ClassNotFoundException {
        
        System.out.println("checkTfjo Debut :");
        ORACLE_CON_PARAM = getOracleCon();
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();
        System.out.println("connexion Oracle BD :"+df.format(dateobj));

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        DateFormat df1 = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj1 = new Date();
        System.out.println("connexion Oracle BD :"+df1.format(dateobj1));
        try {
//            connection = DriverManager.getConnection(url, login, password);
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }        
        String val = "3" ;
        
        Statement stmt = null;
        ResultSet rs = null;
        String schema = getSchema();
        
        String query = "select (case when trim(lib2)='OU' then 1 else 0 end) as val from "+schema+".bknom where ctab='001' and cacc='99000'";
        
//        String query = "SELECT \n" +
//"	(case when (select trim(lib2) from "+schema+"bknom where ctab='001' and cacc='09800')='OU' then 1\n" +
//"  when \n" +
//"  (select trim(lib2) from "+schema+"bknom where ctab='001' and cacc='09800')='FE' and \n" +
//"	(select trim(mnt2) from "+schema+"bknom where ctab='089' and cacc='0060')='1' and\n" +
//"	(select trim(mnt2) from "+schema+"bknom where ctab='089' and cacc='3250')='0'\n" +
//"  then 1 \n" +
//"  when\n" +
//"  (select trim(lib2) from "+schema+"bknom where ctab='001' and cacc='09800')='FE' and \n" +
//"	(select trim(mnt2) from "+schema+"bknom where ctab='089' and cacc='0060')='1' and\n" +
//"	(select trim(mnt2) from "+schema+"bknom where ctab='089' and cacc='3250')='1' and\n" +
//"  (select trim(mnt2) from "+schema+"bknom where ctab='089' and cacc='3270')='1'\n" +
//"  then 1\n" +
//"  when\n" +
//"  (select trim(lib2) from "+schema+"bknom where ctab='001' and cacc='09800')='FE' and \n" +
//"	(select trim(mnt2) from "+schema+"bknom where ctab='089' and cacc='0060')='1' and\n" +
//"	(select trim(mnt2) from "+schema+"bknom where ctab='089' and cacc='3250')='1' and\n" +
//"  (select trim(mnt2) from "+schema+"bknom where ctab='089' and cacc='3270')='0'\n" +
//"  then 0 \n" +
//"  else 0 end) as val\n" +
//"	from dual";
        
        //System.out.println(query);bkprdcli
        try {
            DateFormat df2 = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateobj2 = new Date();
            System.out.println("execution requête Oracle :"+df2.format(dateobj2));
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                
                val = rs.getString("val") ;  // souscription 
                
            }
            DateFormat df3 = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateobj3 = new Date();
            System.out.println("fin execution requête Oracle :"+df3.format(dateobj3));
            System.out.println("fin controle checkTfjo :"+val);
         
        } catch (SQLException e ) {
            e.printStackTrace();
            return val;
        } finally {
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }   
        connection.close();
        if (connection.isClosed())System.out.println("Connection 2 closed.");
        System.out.println("fin checkTfjo val = :"+val);
        return  val;        

    }
    
    public Map<String, Object> getClientInfo(String compte, String token ) throws SQLException, ClassNotFoundException {
        ORACLE_CON_PARAM = getOracleCon();
        Map<String, Object>  data = new HashMap();
        int result = 0;
        JSONObject ret = new JSONObject();
        Map<String, Object> response = new HashMap();
        String  getSchema = getSchema();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        
        try {
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs = null;
        
        String query= "SELECT\n" +
        "  (select lib1 from "+getSchema+".bknom where ctab='001' and a.age = cacc ) agence,\n" +
        "  '10001' code_banque,\n" +
        "  a.age,\n" +
        "  a.cli,\n" +
        "  a.nom,\n" +
        "  a.ges ges_code,\n" +
        "  a.sext sexe,\n" +
        "(select max(p.bpos) FROM "+getSchema+".bkadcli p where p.cli = a.cli and fmt='BP') boite_postale,"+        
        "  a.pre prenom,\n" +
        "  (select max(nom) from "+getSchema+".bkgestionnaire where ges=a.ges) ges_id,\n" +
        "  (case when a.sit ='C' then 'Celibataire' when a.sit ='M' then 'Marié(e)' else 'Indéterminé' end) situ_fam,\n" +
        "  a.reg reg_matri,\n" +
        "  (select max(lib1) from "+getSchema+".bknom where cacc=a.nat and ctab ='033')  nat_code,\n" +
        "  (select max(lib1) from "+getSchema+".bknom where cacc=a.res and ctab ='033')  pays,\n" +
        "  a.nmer nom_mere,\n" +
        "  a.datc date_creation,\n" +
        "  a.dna date_nai,\n" +
        "  a.viln lieu_nai,\n" +
        "  a.did  date_de_delivrance,\n" +
        "\n" +
        "  a.tid,\n" +
        "  (SELECT max(b.lib1) FROM "+getSchema+".bknom b WHERE b.ctab = '078' AND b.cacc = a.tid\n" +
        "  ) pieceidnom,\n" +
        "  a.nid numeropieceid,\n" +
        "  a.vid expiddate,\n" +
        "  a.lid lieudelivid,\n" +
        "  (SELECT max(REPLACE(REPLACE(REPLACE(REPLACE(c.num, '.'), ')'), '('), '+'))\n" +
        "FROM "+getSchema+".bktelcli c\n" +
        "WHERE c.cli=a.cli\n" +
        "AND c.typ  =(\n" +
        "  CASE\n" +
        "    WHEN a.tcli='1'\n" +
        "    THEN '001'\n" +
        "    ELSE '002'\n" +
        "  END)\n" +
        ")phone1,\n" +
        "(\n" +
        "  CASE\n" +
        "  WHEN (SELECT REPLACE(REPLACE(REPLACE(REPLACE(e.num, '.'), ')'), '('), '+')\n" +
        "    FROM "+getSchema+".bktelcli e\n" +
        "    WHERE e.cli=a.cli\n" +
        "    AND e.typ  =(\n" +
        "      CASE\n" +
        "        WHEN a.tcli='1'\n" +
        "        THEN '010'\n" +
        "        ELSE '020'\n" +
        "      END))IS NULL THEN\n" +
        "    (SELECT REPLACE(REPLACE(REPLACE(REPLACE(f.num, '.'), ')'), '('), '+')\n" +
        "    FROM "+getSchema+".bktelcli f\n" +
        "    WHERE f.cli=a.cli\n" +
        "    AND f.typ  =(\n" +
        "      CASE\n" +
        "        WHEN a.tcli='1'\n" +
        "        THEN '001'\n" +
        "        ELSE '002'\n" +
        "      END)\n" +
        "    )\n" +
        "  ELSE\n" +
        "    (SELECT REPLACE(REPLACE(REPLACE(REPLACE(d.num, '.'), ')'), '('), '+')\n" +
        "    FROM "+getSchema+".bktelcli d\n" +
        "    WHERE d.cli=a.cli\n" +
        "    AND d.typ  =(\n" +
        "      CASE\n" +
        "        WHEN a.tcli='1'\n" +
        "        THEN '010'\n" +
        "        ELSE '020'\n" +
        "      END)\n" +
        "    )\n" +
        "  END)phone2,\n" +
        "  (SELECT max(n.email) FROM "+getSchema+".bkemacli n where n.cli = a.cli ) email,\n" +
        "  (SELECT max(v.adr1) FROM "+getSchema+".bkadcli v where v.cli = a.cli and v.typ = 'D') address,\n" +
        "  (SELECT max(j.ville) FROM "+getSchema+".bkadcli j where j.cli = a.cli and j.typ = 'D') ville,\n" +
        "  (SELECT max(h.demb) FROM "+getSchema+".bkprfcli h where h.cli = a.cli) emp_date,\n" +
        "  (SELECT max(k.nom) FROM "+getSchema+".bkempl k where k.emp = (SELECT max(g.emp) FROM "+getSchema+".bkprfcli g where g.cli = a.cli))employer,\n" +
        "  (select max(nom) from (select nom,pre from "+getSchema+".bkcntcli where cli = (select max(cli) from "+getSchema+".bkcom where ncp = '"+compte+"') order by tel desc) where ROWNUM <= 1) pers_contact_nom,\n" +
        "  (select max(pre) from (select nom,pre from "+getSchema+".bkcntcli where cli = (select max(cli) from "+getSchema+".bkcom where ncp = '"+compte+"') order by tel desc) where ROWNUM <= 1) pers_contact_pre,\n" +
        "  (select max(tel) from (select tel from "+getSchema+".bkcntcli  where cli = (select max(cli) from "+getSchema+".bkcom where ncp = '"+compte+"') order by tel desc) where ROWNUM <= 1) pers_contact_contact1,\n" +
        "  (select max(tel) from (select tel from "+getSchema+".bkcntcli  where cli = (select max(cli) from "+getSchema+".bkcom where ncp = '"+compte+"') order by tel asc) where ROWNUM <= 1) pers_contact_contact2,\n" +
        "  a.met profession, (select max(clc) from "+getSchema+".bkcom where ncp = '"+compte+"' ) cle \n" +
        " \n" +
        "FROM "+getSchema+".bkcli a where a.cli = (select distinct cli from "+getSchema+".bkcom where ncp = '"+compte+"')";
        
        //System.out.println("yann see this query "+query);
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            
            
            while (rs.next()) {
                result = 1;
                //i would get the different elements from the result sets
                data.put("numero_compte", compte);
                data.put("token", token);
                data.put("client_trouve", true);
                data.put("agence", rs.getObject("agence"));
                data.put("code_agence", rs.getObject("age"));
                data.put("code_banque", rs.getObject("code_banque"));
                data.put("cle", rs.getObject("cle"));
                data.put("gestionnaire_code", rs.getObject("ges_code"));
                data.put("gestionnaire", rs.getObject("ges_id"));
                data.put("montant_plafond", "");
                data.put("nom", rs.getObject("nom"));
                data.put("prenom", rs.getObject("prenom"));
                data.put("sexe", rs.getObject("sexe"));
                
                data.put("date_naissance", rs.getObject("date_nai"));
                data.put("lieu_naissance", rs.getObject("lieu_nai"));
                data.put("nationalite", rs.getObject("nat_code"));
                data.put("situation_familliale", rs.getObject("situ_fam"));
                data.put("regime_matrimoniale", rs.getObject("reg_matri"));
                data.put("ville", rs.getObject("ville"));
                data.put("pays", rs.getObject("pays"));
                data.put("adresse", rs.getObject("address"));
                data.put("boite_postale", rs.getObject("boite_postale"));
                data.put("status_professionnel", "");
                data.put("employeur", rs.getObject("employer"));
                data.put("profession", rs.getObject("profession"));
                data.put("date_embauche", rs.getObject("emp_date"));
                data.put("email", rs.getObject("email"));
                data.put("telephone1", rs.getObject("phone1"));
                data.put("telephone2", rs.getObject("phone2"));
                data.put("telephone3", "");
                data.put("nom_mere", rs.getObject("nom_mere"));
                data.put("nom_pere", "");
                data.put("piece_identite", rs.getObject("pieceidnom"));
                data.put("numero_piece_identite", rs.getObject("numeropieceid"));
                data.put("ville_delivrance_piece_identite", rs.getObject("lieudelivid"));
                data.put("date_delivrance_piece_identite", rs.getObject("date_de_delivrance"));
                data.put("date_expiration_piece_identite", rs.getObject("expiddate"));
                data.put("nom_personne_a_contacter", rs.getObject("pers_contact_nom"));
                data.put("prenom_personne_a_contacter", rs.getObject("pers_contact_pre"));
                data.put("personne_a_contacter_telephone1", rs.getObject("pers_contact_contact1"));
                data.put("personne_a_contacter_telephone2", rs.getObject("pers_contact_contact2"));
                
            }
            if(result == 0){
                data.put("numero_compte", compte);
                data.put("token", token);
                data.put("client_trouve", false);
                return  data;
            }
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
            e.printStackTrace();
        } finally {
//            if (stm3 != null) { stm3.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }   
        connection.close();
        if (connection.isClosed())System.out.println("Connection 2 closed.");
        
        return  data;    

    }
    
    public Map<String, Object> clientFileTreatmentStatus(String compte , String token) throws SQLException, ClassNotFoundException {
        ORACLE_CON_PARAM = getOracleCon();
        Map<String, Object>  data = new HashMap();
        int result = 0;
        JSONObject ret = new JSONObject();
        Map<String, Object> response = new HashMap();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        
        try {
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs = null;
        
        String query= "select * from (select dou date_enregistrement,\n" +
            "      (case when (eta ='VA' and ctr='1') then 'VAlIDE'\n" +
            "       when (eta ='VA' and ctr='2') then 'REJETE'\n" +
            "       when (eta ='VA' and ctr='0') then 'EN COURS'\n" +
            "       when (eta ='VA' and ctr='4') then 'EN COURS'\n" +
            "      when (eta='SI 'and ctr='0') then 'EN COURS'\n" +
            "      when (eta='SI 'and ctr='1') then 'EN COURS'\n" +
            "      when (eta='VB' and ctr='1') then 'EN COURS'\n" +
            "      when (eta='VB' and ctr='2') then 'REJETE'\n" +
            "      when (eta='VB' and ctr='3') then 'REJETE'\n" +
            "      when (eta='VC' and ctr='1') then 'EN COURS'\n" +
            "      when (eta='VC' and ctr='2') then 'REJETE'\n" +
            "      else 'ECHU' end)etat_dossier,\n" +
            "      deta date_etat,\n" +
            "      mon montant_accorder,\n" +
            "      ddec,\n" +
            "      dpec,\n" +
            "      ddec-dpec durer_credit,\n" +
            "      uti" +
            "       from "+getSchema()+".bkdosprt where cli = (select distinct cli from "+getSchema()+".bkcom where ncp = '"+compte+"') and typ in ('193','152','151') order by dou asc)where ROWNUM <= 1";
        
        //System.out.println("yann see this query "+query);
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            
            
            while (rs.next()) {
                result = 1;
                //i would get the different elements from the result sets
                data.put("numero_compte", compte.toString());
                data.put("token", token);
                data.put("dossier_deja_enregistre", true);
                data.put("date_enregistrement", rs.getObject("date_enregistrement").toString());
                data.put("heure_enregistrement", "");
                data.put("etat_dossier", rs.getObject("etat_dossier").toString());
                data.put("date_etat", rs.getObject("date_etat").toString());
                data.put("heure_etat", "");
                data.put("montant_accorde", rs.getObject("montant_accorder").toString());
                data.put("duree_credit", rs.getObject("durer_credit").toString());
                
                
            }
            if(result == 0){
                data.put("numero_compte", compte);
                data.put("token", token);
                data.put("dossier_deja_enregistre", false);
                return  data;
            }
            
                 
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
            e.printStackTrace();
        } finally {
//            if (stm3 != null) { stm3.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }   
        connection.close();
        if (connection.isClosed())System.out.println("Connection 2 closed.");
        
        return  data;    

    }
    
    public Map<String, Object> getQuotiteByCli(String compte ,Long mnt1,Long mnt2,Long mnt3,  String token) throws SQLException, ClassNotFoundException {
        ORACLE_CON_PARAM = getOracleCon();
        Map<String, Object>  data = new HashMap();
        int result = 0;
        int result2 = 0;
        String  getSchema = getSchema();
        JSONObject ret = new JSONObject();
        Map<String, Object> response = new HashMap();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        
        try {
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs = null;
        Statement stmt2 = null;
        ResultSet rs2 = null;
        
        String cli = "";
        String query_cli= "select distinct cli from "+getSchema+".bkcom where ncp = '"+compte+"'";
        
        
        //System.out.println("yann see this query "+query);
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query_cli);
            
            
            while (rs.next()) {
                result = 1;
                //i would get the different elements from the result sets
                cli =rs.getObject("cli").toString().trim();
                
                stmt2 = connection.createStatement();
                String query= "select "+getSchema+".get_quotite_by_cli('"+cli+"',"+mnt1+","+mnt2+","+mnt3+") quotite from "+getSchema+".bkcom where ncp = '"+compte+"'";  
                //String query= "select "+getSchema+".get_quotite_by_cli('"+cli+"',"+mnt1+","+mnt2+","+mnt3+") quotite from "+getSchema+".bkcom where ncp = '"+compte+"'";  
                
                System.out.println("yann see this quotite query "+query);
                //stmt.setQueryTimeout(150);
                rs2 = stmt.executeQuery(query);
                while (rs2.next()) {
                    result2 = 1;
                    //i would get the different elements from the result sets
                    //convert the result to map and send the response
                    data.put("quotite", rs2.getObject("quotite"));
                    data.put("data", convert(rs2).toList());
                    data.put("numero_compte", compte);
                    data.put("token", token);

                }
                if(result2 == 0){
                    data.put("numero_compte", compte);
                    data.put("token", token);
                    data.put("status", "FAILED");
                    data.put("message", "An Error occured");
                    return  data;
                }
                
            }
            if(result == 0){
                data.put("numero_compte", compte);
                data.put("token", token);
                data.put("status", "FAILED");
                data.put("message", "Incorrect compte number");
                return  data;
            }
            
                 
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
            e.printStackTrace();
        } finally {
//            if (stm3 != null) { stm3.close(); }
            try { stmt.close();stmt2.close();if (stmt.isClosed()&&stmt2.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();rs2.close();if (rs.isClosed()&&rs2.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }   
        connection.close();
        if (connection.isClosed())System.out.println("Connection 2 closed.");
        
        return  data;    

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
      
     
    public JSONObject getEtebac(String agence, String compte ) throws SQLException, ClassNotFoundException {
        ORACLE_CON_PARAM = getOracleCon();
        BigDecimal amount = null;
        String transactionId = null;
        String op_type = null;
        Date op_date = null;
        int result = 0;
        JSONObject ret = new JSONObject();
        JSONObject response = new JSONObject();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        
        try {
            connection = DriverManager.getConnection(ORACLE_CON_PARAM[0], ORACLE_CON_PARAM[1], ORACLE_CON_PARAM[2]);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs = null;
        
        ArrayList<JSONObject> historyList = new ArrayList();
        
//"select * from bkdetetebac where ncp='COMPTE';"
        String query= "select * from "+getSchema()+".bkdetetebac where ncp='"+compte+"'";
        System.out.println("yvo see this query "+query);        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            int j = 0;
            while (rs.next()) {
                result = 1;
              
            }
            
            if(result==0){
                response.put("success", "100");
                response.put("message", "some error occured");
                return  response;
            }
            System.out.println(" yann see history inside core "+historyList.toString());
            response.put("success", "01");
            response.put("data", rs.toString());
            return  response;
                 
            
        } catch (SQLException e ) {
//                JDBCTutorialUtilities.printSQLException(e);
            e.printStackTrace();
        } finally {
//            if (stm3 != null) { stm3.close(); }
            try { stmt.close();if (stmt.isClosed())System.out.println("Connection Statement closed."); } catch (Exception e) { /* ignored */ }
            try { rs.close();if (rs.isClosed())System.out.println("Connection Resultset closed."); } catch (Exception e) { /* ignored */ }
        }   
        connection.close();
        if (connection.isClosed())System.out.println("Connection 2 closed.");
        
        response.put("success", "100");
        response.put("data", "Account not found");
        return  response;    

    }
    
}
