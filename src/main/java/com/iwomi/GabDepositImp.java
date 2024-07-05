/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi;

import com.iwomi.controller.BicecPayController;
import com.iwomi.model.Nomenclature;
import com.iwomi.model.Pwd;
import com.iwomi.repository.NomenclatureRepository;
import com.iwomi.repository.PwdRepository;
import com.iwomi.repository.UserRepository;
import com.iwomi.serviceInterface.GabDeposit;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.util.Base64;
import java.util.List;
import static org.apache.commons.lang3.StringUtils.trim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 *
 * @author HP
 */
@Service
public class GabDepositImp implements GabDeposit{
    
    Session session = null;
    JSch jsch = new JSch();
    Channel channel=null;
    int ISO8583_HEAD_LENGTH =34; //

    String sys,vaal,res2,OUT_TXT22,S2M_PREF,TAUX_TVA,lib,com,way,agen,comp,OUT_TXT2,codeTrans,OUT_TXT,SFTPUSER, MachineIP,SFTPPASS,SFTPWORKINGDIR ,SFTPUSER2, MachineIP2,SFTPPASS2,SFTPWORKINGDIR_EXACT,SFTPWORKINGDIR_DEST_FINAL,VATEST;
    //VATEST= 21001
    //SFTPWORKINGDIR_DEST_FINAL ou on va deposer les fichiers traités.pour historique.
    //SFTPWORKINGDIR_EXACT ou on va deposé les fichiers dupliqués
    //SFTPWORKINGDIR ou on va envoyer les fichier au format AFB160
    //VATEST= 21001  LA VALEUR TESTE  DU CODE DEPOSIT
    int SFTPPORT,SFTPPORT2,SEC_GAB,NBRE;
    String mont="";
    ChannelSftp channelSftp; 
    InputStream stream;
    public static final String USERNAME="root";
    public static final String PASSWORD="";
    public static final String CONN_STRING="jdbc:mysql://localhost/bicec";
    public static final String PENDING="1000";
    public static final String PENDING1="1001";
    public static final String PENDING2="1002";
    
    

    ResultSet result,set,comm,sys1,res22,ret; 
    String first11[]=null;
    String DE[]=null;
 //   String iso8583_TO_ARRAY[]=null;
    String details11[][]=null;
    String foote11[]=null;
    Connection conn=null;
    Map<Integer, String> first = new HashMap<Integer, String>();
    Map<Integer, String> foote = new HashMap<Integer, String>();
    Map<Integer, String> detail = new HashMap<Integer, String>();
    Map<Integer, String> iso8583ToMap = new HashMap<Integer, String>();
    JSONArray credit = null;
    JSONObject debit = null;
    JSONObject var=null;
    JSONArray credit_save = new JSONArray();
    JSONArray debit_save = new JSONArray() ;
    int isVirement;
    Long amtTotal=0L;
    
    
    Connection connection,con = null;
    String url = "jdbc:oracle:thin:@10.100.30.35:1521:stockv10";
    String login ="amphom";
    String password ="amphom";
    String date = "";
    String NOMENGABTABLE ="5001";
    String NOMENGABTABLE_SYS ="0012";
    String NOMENGABTABLE_TAUX ="5006";
    String NOMENGABTABLE_Produit ="5007";
    String NOMENGABTABLE_NATURE ="5000";
    String PAYMENT_FACTURE_PROCESSING_CODE ="50";
    String PAYMENT_COMMERCANT_PROCESSING_CODE = "52";
    String AMPLITUDE_IP = "";
    String NBRE_MAX_PIN = "";
    Map<String, String> FOR_STAT = new HashMap<String, String>();
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
    UserRepository userRepository;
    
    @Autowired
    NomenclatureRepository nomenclatureRepository;
   
    @Autowired
    PwdRepository pwdRepository;
    
    
    Nomenclature nomenclature = null;

    /**
     *
     * @param pnbr
     * @param patner
     * @param obj
     * @param arr
     * @param iso8583ToMap
     * @return
     * @throws SftpException
     * @throws IOException
     * @throws JSONException
     * @throws InterruptedException
     * @throws JSchException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Override
    public String gabDepos(String pnbr,String patner,JSONObject obj, JSONArray arr,Map<Integer, String> iso8583ToMap) throws SftpException, IOException, JSONException, InterruptedException, JSchException, SQLException, ClassNotFoundException {
        final afb160 AB= new afb160();
        String newIso8583=null;

        
        String access_code_patner = userRepository.findByName(patner).getCode();
        nomenclature = nomenclatureRepository.findTabcdAndAcsd("5000",access_code_patner,"0");
        
        
        if(nomenclature !=null){
            first.put(0, "03");
            first.put(2, "8");
            //first.put(3, result.getString("lib3"));// numero de l'emmetteur
            first.put(4, "7");
            //first.put(6, result.getString("lib4"));
            if(nomenclature.getLib1() != null && !nomenclature.getLib1().isEmpty()){
                   first.put(7, nomenclature.getLib1());  // reference remise.
                   lib=nomenclature.getLib1();
            }else{
                   first.put(7, "XXXXXXA21XZXTQ");   // reference remise
                   lib="XXXXXXA21XZXTQ";

             }
            first.put(8, "19");
            first.put(10, "5");
            first.put(13, "2");//  est ce qu on laisse vide?? la clé
            first.put(15, "31");
            first.put(17, "6");

            if(nomenclature.getLib2() != null && !nomenclature.getLib2().isEmpty()){
                 first.put(14, nomenclature.getLib2());  // identifiant du DO.
            }else{
                 first.put(14, "XXXXXXA21XZXTQ");   // identifiant du DO.
            }
            if(nomenclature.getLib5() != null && !nomenclature.getLib5().isEmpty()){
//                        detail.put(4, result.getString("lib3"));  // reference interne
                detail.put(14, pnbr+nomenclature.getLib5()); // libelle historique
            }else{
                 detail.put(14, "XXXXXXA21XZXTQ"); // reference interne.
            }
            if(nomenclature.getLib1() != null && !nomenclature.getLib1().isEmpty()){
                detail.put(4, nomenclature.getLib1());  // reference remise
//                        detail.put(14, result.getString("lib3")); // reference interne
            }else{
                 detail.put(4, "XXXXXXA21XZXTQ"); // reference interne.
            }

            if(nomenclature.getLib4() != null && !nomenclature.getLib4().isEmpty()){
                first.put(1, nomenclature.getLib4());  // code operation fichier afb160
                detail.put(1, nomenclature.getLib4());
                foote.put(1,nomenclature.getLib4());
            }else{
                first.put(1, nomenclature.getLib4());  //
                detail.put(1, nomenclature.getLib4());
                foote.put(1,nomenclature.getLib4());
            }
            if(nomenclature.getMnt1() != null ){
                isVirement= nomenclature.getMnt1().intValue(); // isvirement est egal a 1 c est un virement si c est 0 c est un prelevement
            }
            detail.put(0, "06");
            detail.put(2, "8");
           // detail.put(3, result.getString("lib3"));
            detail.put(16,"6");

            foote.put(0,"08");
            foote.put(2,"8");
            //foote.put(3,result.getString("lib3"));
            foote.put(4,"84");
            foote.put(6,"42"); 


        }

        List<Nomenclature> nomenList = nomenclatureRepository.findTabcdAndDel("0012","0");
        for(int i = 0; i<nomenList.size();i++){
            Nomenclature nomen = nomenList.get(i);
                if(nomen != null){

                    if(nomen.getAcscd().equalsIgnoreCase("0007")){
                        if(nomen.getLib2() != null && !nomen.getLib2().isEmpty()){
                            first.put(9, nomen.getLib2());  // monnaie de remise
                        }else{
                            first.put(9, "XXXXXXA21XZXTQ");  // monnaie de remise
                        }
                    }
                    if(nomen.getAcscd().equalsIgnoreCase("0008")){

                        first.put(16, obj.get("bkc").toString());  // code bank
                    }
                    if(nomen.getAcscd().equalsIgnoreCase("0009")){
                        detail.put(6, nomen.getLib2());  // domiciliation en clair
                    }
                    if(nomen.getAcscd().equalsIgnoreCase("0010")){
                        if(nomen.getLib2() != null && !nomen.getLib2().isEmpty()){
                            detail.put(7, nomen.getLib2());  // nature eco N.R
                        }else{
                            detail.put(7, "XXXXXXA21XZXTQ");  // nature eco N.R
                        }
                    }

                    if(nomen.getAcscd().equalsIgnoreCase("0011")){
                        if(nomen.getLib2() != null && !nomen.getLib2().isEmpty()){
                            detail.put(8, nomen.getLib2());  // code pays N.R
                        }else{
                            detail.put(8, "XXXXXXA21XZXTQ");  // code pays N.R
                        }      
                    }
                    if(nomen.getAcscd().equalsIgnoreCase("0012")){
                        if(nomen.getLib2() != null && !nomen.getLib2().isEmpty()){
                            detail.put(9, nomen.getLib2());  // BALANCE PAIEMENT
                        }else{
                            detail.put(9, "XXXXXXA21XZXTQ");  // BALANCE PAIEMENT
                        }  
                    }
                    if(nomen.getAcscd().equalsIgnoreCase("0015")){

                        if(nomen.getLib2() != null && !nomen.getLib2().isEmpty()){
//                            detail.put(15, nomen.getLib2());  // code etablissement
                        }else{
                            detail.put(15, "XXXXXXA21XZXTQ");  // code etablissement
                        } 
                    }

                    /// 0013 CODE REJET MAIS CELA NE NOUS INTERRESSE PAS
                    if(nomen.getAcscd().equalsIgnoreCase("0014")){
                        SFTPWORKINGDIR=nomen.getLib2();  // Le chemin du repsertoire
                    }
                    if(nomen.getAcscd().equalsIgnoreCase("0016")){
                      //  first.put(1, nomen.getLib2()); CODE OPE 
        //                        detail.put(1, nomen.getLib2());
        //                        foote.put(1,nomen.getLib2());
                    }
                    if(nomen.getAcscd().equalsIgnoreCase("0017")){
                        OUT_TXT2=nomen.getLib2();  // le Prefixe du nom du fichier+le corps du nom
                    }
                    if(nomen.getAcscd().equalsIgnoreCase("0018")){
                        first.put(3, nomen.getLib2());  // Numéro de l'emetteur.
                        detail.put(3, nomen.getLib2());
                        foote.put(3,nomen.getLib2());
                    }
                    if(nomen.getAcscd().equalsIgnoreCase("0019")){
                        MachineIP=nomen.getLib2();   //ip adresse
                    }
                     if(nomen.getAcscd().equalsIgnoreCase("0020")){
                        SFTPUSER=nomen.getLib2();  // SFTP USER
                    }

                    if(nomen.getAcscd().equalsIgnoreCase("0022")){
                        SFTPPORT = Integer.parseInt(nomen.getLib2());  //SFTP PORT 
                    }
                    if(nomen.getAcscd().equalsIgnoreCase("0021")){
                        SFTPPASS=nomen.getLib2();  // SFTP PASSWORD
                    }

                    if(nomen.getAcscd().equalsIgnoreCase("0033")){
                        SEC_GAB=Integer.parseInt(nomen.getLib2());  // Le temps d attente avant l'appel de la methode du checstatus 
                    }
                    if(nomen.getAcscd().equalsIgnoreCase("0118")){
                        NBRE=Integer.parseInt(nomen.getLib2()); // Le temps d attente avant l'appel de la methode du checstatus 
                    }

                }
            }
        

        
        //get the SFTPUSER, MachineIP, SFTPPORT from the pwd repository and assign them here
        Pwd pwd = pwdRepository.findByAcscd("0020","0");
        SFTPUSER=pwd.getLogin();
        MachineIP=  pwd.getLib1();
        SFTPPORT = Integer.parseInt(pwd.getLib2());
        
        byte[] decoder = Base64.getDecoder().decode(trim(pwd.getPass()));
        String v = new String(decoder);
        
        SFTPPASS=trim(v);
        
        // SI le processing code est de 21 alors c est une transaction financière.
        // Integer.parseInt(result.getString("mnt1"))
         //recupère le timestamp de la date. formate sur 6 position et repère la chaine et passe la dans removeCharAt qui la fait passer a 5 positions
        //first.put(5,obj.getString("5"));  //DATE DE TRAITEMENT : prend la date de entre 0 et position 6 et en enlève la position 4 qui corespond a 5  c est le time stamps de position 9 a 20 sur la trame. recuperer et traiter recuperer de 9 a 20 sur l iso avant meme de parser
        
        String date = "";
        date = DateVal();
        
        if(date.equalsIgnoreCase("Cannot connect to Oracle Database")){
            return "Cannot connect to Oracle Database";
        }
        prepDate(date);
        first.put(5,prepDate(date));
        first.put(11,obj.getString("age")); // AGENCE prendre field 102 couper pour avoir agence egt compte
        first.put(12, obj.getString("cpt"));// COMPTE  fields 102(agence et compte) 0 et 1  SONT DEUX POSITION SEMBLABLES A LA CLE
        //first.put(6, obj.getString("6")); // Fields 2 raison sociale de l'emeteur. (compte + num compte)
        first.put(6,patner);
        JSONObject o= new JSONObject();
        o.put("agence",   obj.getString("age"));  // ici le nom du client sera l equivalent de son numero de carte.
        o.put("compte",  obj.getString("cpt")); // agence 
        o.put("cle","xxx"); // compte

         OUT_TXT22= OUT_TXT2+AB.randoms();
         agen=obj.getString("age");
         comp= obj.getString("cpt");
         final File file = new File(OUT_TXT22);
         JSch jsch2 = new JSch();
         try {
             session = jsch2.getSession(SFTPUSER, MachineIP, SFTPPORT);
         } catch (JSchException ex) {
             Logger.getLogger(GabDepositImp.class.getName()).log(Level.SEVERE, null, ex);
         }
         session.setPassword(SFTPPASS);
         java.util.Properties config = new java.util.Properties(); 
         config.put("StrictHostKeyChecking", "no");
         session.setConfig(config);
         System.out.println("Config set");
         try {
             session.connect();
//           session.setTimeout();
             channel = session.openChannel("sftp");
             System.out.println("Session connected");
             channel.connect();

             System.out.println("Connection Opened\n");
             channelSftp = (ChannelSftp) channel;
             channelSftp.cd(SFTPWORKINGDIR);
             OutputStream out = channelSftp.put(file.toString());
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
             writer.write(AB.header3(first));
             writer.newLine();
             JSONObject ob= new JSONObject();

             amtTotal=0L;
             for(int i=0;i<arr.length();i++){
                 JSONObject jo= arr.getJSONObject(i);
                 JSONObject o2= new JSONObject();
                 //detail.put(5,jo.getString("5"));  // NOM Client(24 en longueur) (ici c est le numero de reference du gab ( mais equivalent de la reference du gab.(19 en longueur)
                 detail.put(5,pnbr);
                 //detail.put(16, jo.getString("bkc")); //added by yanick
                 detail.put(10,jo.getString("age"));//  agence
                 detail.put(11,jo.getString("cpt"));  // compte
                 detail.put(15, jo.getString("bkc"));
                 /*detail.put(10,line.substring(95, 100));//  agence
                 detail.put(11,line.substring(100, 111));  // compte*/
                 Long temp_amt = Double.valueOf(jo.getString("mnt")).longValue();
                 detail.put(12,temp_amt.toString());
                 //detail.put(12,jo.getString("mnt")); // montant fields 6
                // detail.put(13,jo.getString("13"));// Fields 37 ( champs libélé de la table de paramétrage des opérations.ICI C EST L'EQUIVALENT  de la reference de l'operation A12)
                detail.put(13,jo.getString("desp")); 
//                if(i>0){
                    detail.put(14, pnbr);
//                 }
                 System.out.println(jo.getString("age"));
                 o2.put("agence",   jo.getString("age"));  // ici le nom du client sera l equivalent de son numero de carte.
                 o2.put("compte",  jo.getString("cpt")); // agence 
                 o2.put("cle","xxx"); // compte
                 //o2.put("nom_client",jo.getString("5"));  // montant
                 o2.put("nom_client",patner);//
                 o2.put("montant",jo.get("mnt").toString());  // montant Double.valueOf(jo.getString("mnt")).longValue();
                 o2.put("lib",lib);  // libelle
                 credit_save.put(i, o2);
                 System.out.println("yann see this "+jo.getString("mnt"));
                 System.out.println("yann see this "+Double.valueOf(jo.getString("mnt")).longValue());
                 amtTotal+= Double.valueOf(jo.getString("mnt")).longValue();  //  field 6
                 // foote.put(5,"Montant total des 06 RAOUL");
                 writer.write(AB.details6(detail));
                 writer.newLine();
                 
             }
             foote.put(5,amtTotal.toString());
             writer.write(AB.footer8(foote));
             writer.flush();
             writer.close();        
//                sleep(SEC_GAB);
             o.put("montant",amtTotal+"");  // montant
             debit_save.put(0, o);
             amtTotal=0L;
             //Long id = dat.saveTransaction(access_code_patner, debit_save, credit_save,"xxx","01", OUT_TXT22,iso8583ToMap.get(3),iso8583ToMap.get(37),iso8583ToMap.get(12));
            // dat.save(CODE_NATURE_GAB, agen, comp,arrangeAmount(mont), status.getString("message"));
//                Thread t = new Thread() {
//                    public void run() {
//                        try {
//                            // checquer le statut jusqu'a obetenir 01 ou 100
//                            JSONObject status= dat.checkstatus(OUT_TXT22);
//                            int p = 0;
//                            String val = status.getString("status");
//                            Long id = dat.saveTransaction(CODE_NATURE_GAB, debit_save, credit_save,"xxx",val, OUT_TXT22,iso8583ToMap.get(3),iso8583ToMap.get(37),iso8583ToMap.get(12));
//                            while(val.equalsIgnoreCase(PENDING) || val.equalsIgnoreCase(PENDING1) || val.equalsIgnoreCase(PENDING2)){
//                                if(p>=NBRE && (val.equalsIgnoreCase(PENDING) || val.equalsIgnoreCase(PENDING1) || val.equalsIgnoreCase(PENDING2))){
//                                    val="112";
//                                }else{
//                                   status= dat.checkstatus(OUT_TXT22); 
//                                   val = status.getString("status");
//                                   sleep(SEC_GAB);
//                                }
//                                p++;
//                            }
//                            dat.updateTransaction(val,id);
//                        } catch (ClassNotFoundException ex) {
//                            Logger.getLogger(GabDepositImp.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (SQLException ex) {
//                            Logger.getLogger(GabDepositImp.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (JSchException ex) {
//                            Logger.getLogger(GabDepositImp.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (SftpException ex) {
//                            Logger.getLogger(GabDepositImp.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(GabDepositImp.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (JSONException ex) {
//                            Logger.getLogger(GabDepositImp.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                };
//                t.start();

//                vaal=status.getString("message");
//                dat.save(CODE_NATURE_GAB, agen, comp,arrangeAmount(mont), "01");
            //   dat.save(CODE_NATURE_GAB, agen, com, arrangeAmount(mont), "1000");
             //mettre le MTI de retour de l iso 

 //NOK GENERER LE NOUVEAU BITMAP
             /*BITMAP=ISO8583_DATA.substring(38,70);           // recuperer le bitmap
             BITMAP= new BigInteger(BITMAP, 16).toString(2);  // convertir en binaire
             BITMAP=BITMAP.substring(0,38)+"1"+BITMAP.substring(39,128); // refabriquer le bitmap en binaire
             BigInteger b = new BigInteger(BITMAP, 2);
             BITMAP=b.toString(16);// convertir en hexa decimal.*/
            // refabriquer l iso ok on va recoller la tête et le corps.
           //revoyer vers raoul.  nok         
 // nok                               / Qu est le code de transaction que la bd retrourne. est ce qu il faut faire une requête dans la BD;
 //                               / note= la reponse doit toujours etre OK. meme s il ya echec et que le message tarde.

             } catch (JSONException ex) {
                 Logger.getLogger(GabDepositImp.class.getName()).log(Level.SEVERE, null, ex);
                 return ex.getMessage();
             } catch (JSchException ex) {
                 Logger.getLogger(GabDepositImp.class.getName()).log(Level.SEVERE, null, ex);
                 return ex.getMessage();
             } catch (SftpException ex) {
                 Logger.getLogger(GabDepositImp.class.getName()).log(Level.SEVERE, null, ex);
                 return ex.getMessage();
             }
            return "OK";
//               return vaal;

                          
    }
    
    private void sleep(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException ex) {}
    }
    
    public String  arrangeAmount(String Amt) {
        
        while(Amt.startsWith("0")){
            Amt=Amt.substring(1);
        }
        return Amt;
    }
    
    public void MosaFile(String ret,String wal) throws JSchException, IOException {
        final afb160 AB= new afb160();
        OUT_TXT22= ret+AB.randoms();
        final File file = new File(OUT_TXT22);
        JSch jsch2 = new JSch();
        try {
//            session = jsch2.getSession("iwomi", "195.24.211.121", 50000);
            session = jsch2.getSession("iwomi_bille@intra.ipa", "10.100.20.44", 22);
        } catch (JSchException ex) {
            Logger.getLogger(GabDepositImp.class.getName()).log(Level.SEVERE, null, ex);
        }
//        session.setPassword("Miki!@hirCol2015");
        session.setPassword("iwomi@2020");
        java.util.Properties config = new java.util.Properties(); 
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        System.out.println("Config set");
//        SFTPWORKINGDIR ="/home/iwomi/rec";
        SFTPWORKINGDIR ="/IWOMICORE/POC/";
        try {
            session.connect();
//                session.setTimeout();
            channel = session.openChannel("sftp");
            System.out.println("Session connected");
            channel.connect();

            System.out.println("Connection Opened\n");
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(SFTPWORKINGDIR);
            OutputStream out = channelSftp.put(SFTPWORKINGDIR+file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            System.out.println(ret+"|"+wal);
            writer.write(ret);
            System.out.println("test");
            writer.flush();
            writer.close();
        } catch (JSONException ex) {
            Logger.getLogger(GabDepositImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSchException ex) {
            Logger.getLogger(GabDepositImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SftpException ex) {
            Logger.getLogger(GabDepositImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String prepDate(String s) throws SQLException, ClassNotFoundException{
        s = DateVal();
        return s.substring(0, 4) + s.substring(7,8 );
    
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
            return "Cannot connect to Oracle Database";
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
    
    public String [] getOracleCon() throws SQLException, ClassNotFoundException, JSONException{
        
        //pwd repository and extra url, login and pass****/
        Pwd pwd = pwdRepository.findByAcscd("0043","0");
        
        byte[] decoder = Base64.getDecoder().decode(trim(pwd.getPass().toString()));
        String v = new String(decoder);
        URL_ORACLE = trim(pwd.getLib1());
        LOGIN_ORACLE = trim(pwd.getLogin());
        PASSWORD_ORACLE = trim(v);
        
     
        return new String[]{URL_ORACLE,LOGIN_ORACLE,PASSWORD_ORACLE};

    } 
    
        
    public String getSchema(){
        nomenclature = nomenclatureRepository.findTabcdAndAcsd("0012","0123","0");
        if (nomenclature != null){
            return nomenclature.getLib2();
        }
        return "bank";
    }

}
