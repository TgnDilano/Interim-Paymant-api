/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.soapClient;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author fabri
 */
//https://www.alpha.gr/-/media/alphagr/files/files-archive/businessbanking/sepaxmlpaymentfile_customerspecs_v1.pdf
public class Pain001Gen {
    
    
    
    
    
      public static void main(String[] args) 
    {
       
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 'T'HH:mm:ss");
        Date now = new Date();
       System.out.println(sdf.format(now));System.exit(0);
        
        
        
        final String xmlStr = "<employees>" + 
                                "   <employee id=\"101\">" + 
                                "        <name>Lokesh Gupta</name>" + 
                                "       <title>Author</title>" + 
                                "   </employee>" + 
                                "   <employee id=\"102\">" + 
                                "        <name>Brian Lara</name>" + 
                                "       <title>Cricketer</title>" + 
                                "   </employee>" + 
                                "</employees>";
        
        
    
    }
       

    
    public String generatepain001(HeadGen head,PmtInf pm,List<CreditAccountList> cred,DebitAccount deb){
          
             //     String pain001 ="<![CDATA[<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB\"><CstmrCdtTrfInitn><GrpHdr><MsgId>A2017087467738477889970803875</MsgId><CreDtTm>2020-01-14T17:57:29</CreDtTm><NbOfTxs>1</NbOfTxs><CtrlSum>10000.0000</CtrlSum><InitgPty/><DltPrvtData><FlwInd>HOMOLOGATION</FlwInd><DltPrvtDataDtl><PrvtDtInf>CANAL_VRT_WEB_MODIF</PrvtDtInf><Tp><CdOrPrtry><Cd>CHANNEL</Cd></CdOrPrtry></Tp></DltPrvtDataDtl></DltPrvtData></GrpHdr><PmtInf><PmtInfId>A2017087467778957838899975</PmtInfId><PmtMtd>TRF</PmtMtd><BtchBookg>0</BtchBookg><NbOfTxs>1</NbOfTxs><CtrlSum>10000.0000</CtrlSum><DltPrvtData><OrdrPrties><Tp>IMM</Tp><Md>CREATE</Md></OrdrPrties></DltPrvtData><PmtTpInf><InstrPrty>NORM</InstrPrty><SvcLvl><Prtry>INTERNAL</Prtry></SvcLvl></PmtTpInf><ReqdExctnDt>2020-01-21</ReqdExctnDt><Dbtr/><DbtrAcct><Id><Othr><Id>00112676601</Id><SchmeNm><Prtry>BKCOM_ACCOUNT</Prtry></SchmeNm></Othr></Id><Ccy>XAF</Ccy></DbtrAcct><DbtrAgt><FinInstnId><Nm>BANQUE CCA SIEGE</Nm><Othr><Id>10039</Id><SchmeNm><Prtry>ITF_DELTAMOP_IDETAB</Prtry></SchmeNm></Othr></FinInstnId><BrnchId><Id>10007</Id><Nm>Agence</Nm></BrnchId></DbtrAgt><CdtTrfTxInf><PmtId><InstrId>6692486951789999996</InstrId><EndToEndId>6692486788888951996</EndToEndId></PmtId><Amt><InstdAmt Ccy=\"XAF\">10000.0000</InstdAmt></Amt><CdtrAgt><FinInstnId><Nm>BANQUE CCA SIEGE</Nm><Othr><Id>10039</Id><SchmeNm><Prtry>ITF_DELTAMOP_IDETAB</Prtry></SchmeNm></Othr></FinInstnId><BrnchId><Id>10007</Id><Nm>Agence</Nm></BrnchId></CdtrAgt><Cdtr/><CdtrAcct><Id><Othr><Id>00112811901</Id><SchmeNm><Prtry>BKCOM_ACCOUNT</Prtry></SchmeNm></Othr></Id><Ccy>XAF</Ccy></CdtrAcct><RmtInf><Ustrd>TEST MOTIF</Ustrd></RmtInf></CdtTrfTxInf></PmtInf></CstmrCdtTrfInitn></Document>]]>";
                 
                  String ee ="<![CDATA[<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB\">\n" +
"					<CstmrCdtTrfInitn>\n" +
"					<GrpHdr>\n" +
"						<MsgId>"+head.getMsgId()+"</MsgId>\n" +
"						<CreDtTm>"+head.getCreDtTm()+"</CreDtTm>\n" +
"						<NbOfTxs>"+head.getNbOfTxs()+"</NbOfTxs>\n" +
"						<CtrlSum>"+head.getCtrlSum()+"</CtrlSum>\n" +
"						<InitgPty/>\n" +
"						<DltPrvtData>\n" +
"							<FlwInd>"+head.getFlwInd()+"</FlwInd>\n" +
"							<DltPrvtDataDtl>\n" +
"								<PrvtDtInf>"+head.getPrvtDtInf()+"</PrvtDtInf>\n" +
"								<Tp>\n" +
"									<CdOrPrtry>\n" +
"										<Cd>"+head.getCdOrPrtry()+"</Cd>\n" +
"									</CdOrPrtry>\n" +
"								</Tp>\n" +
"							</DltPrvtDataDtl>\n" +
"						</DltPrvtData>\n" +
"					</GrpHdr>\n" +
"					<PmtInf>\n" +
"                                       <PmtInfId>"+pm.getPmtInfId()+"</PmtInfId>\n" +
"					<PmtMtd>"+pm.getPmtMtd()+"</PmtMtd>\n" +
"					<BtchBookg>"+pm.getBtchBookg()+"</BtchBookg>\n" +
"					<NbOfTxs>"+pm.getNbOfTxs2()+"</NbOfTxs>\n" +
"					<CtrlSum>"+pm.getCtrlSum()+"</CtrlSum>\n" +
"					<DltPrvtData>\n" +
"						<OrdrPrties>\n" +
"							<Tp>"+pm.getOrdrTp()+"</Tp>\n" +
"							<Md>"+pm.getOrdrMd()+"</Md>\n" +
"						</OrdrPrties>\n" +
"					</DltPrvtData>\n" +
"					<PmtTpInf>\n" +
"						<InstrPrty>"+pm.getInstrPrty()+"</InstrPrty>\n" +
"						<SvcLvl>\n" +
"							<Prtry>"+pm.getSvcLvlPrty()+"</Prtry>\n" +
"						</SvcLvl>\n" +
"					</PmtTpInf>\n" +
"						<ReqdExctnDt>"+pm.getReqdExctnDt()+"</ReqdExctnDt>\n" +
"						<Dbtr/>\n" +
"							<DbtrAcct>\n" +
"								<Id>\n" +
"									<Othr>\n" +
"										<Id>"+deb.getBkid()+"</Id>\n" +
"										<SchmeNm>    \n" +
"											<Prtry>"+deb.getBkPrtry()+"</Prtry>\n" +
"										</SchmeNm>\n" +
"									</Othr>\n" +
"								</Id>\n" +
"								<Ccy>"+deb.getCcy()+"</Ccy>\n" +
"							</DbtrAcct>\n" +
"							<DbtrAgt>\n" +
"								<FinInstnId>\n" +
"									<Nm>"+deb.getBkname()+"</Nm>\n" +
"									<Othr>\n" +
"										<Id>"+deb.getBkid()+"</Id>\n" +
"										<SchmeNm>\n" +
"											<Prtry>"+deb.getBkPrtry()+"</Prtry>\n" +
"										</SchmeNm>\n" +
"									</Othr>\n" +
"								</FinInstnId>\n" +
"								<BrnchId>\n" +
"									<Id>"+deb.getAgeId()+"</Id>\n" +
"									<Nm>"+deb.getAgeName()+"</Nm>\n" +
"								</BrnchId>\n" +
"							</DbtrAgt>]]>";
                  
                String V="";
                
                for(int i = 0; i<=cred.size()+1;i++){
                      
                    if(i==0){
                        
                        V= "<![CDATA[";

                    }else if( i == cred.size()+1 ){
                         V = V+"</PmtInf></CstmrCdtTrfInitn></Document>]]>";

                    }else{
                    
                            V =V+"<CdtTrfTxInf>\n" +
                            "								<PmtId>\n" +
                            "									<InstrId>"+cred.get(i-1).getInstrId()+"</InstrId>\n" +
                            "									<EndToEndId>"+cred.get(i-1).getEndToEndId()+"</EndToEndId>\n" +
                            "								</PmtId>\n" +
                            "								<Amt>\n" +
                            "									<InstdAmt Ccy="+cred.get(i-1).getCcy()+">"+cred.get(i-1).getInstdAmt()+"</InstdAmt>\n" +
                            "								</Amt>\n" +
                            "								<CdtrAgt>\n" +
                            "									<FinInstnId>\n" +
                            "										<Nm>"+cred.get(i-1).getFinInstnIdNm()+"</Nm>\n" +
                            "										<Othr>\n" +
                            "											<Id>"+cred.get(i-1).getBankId()+"</Id>\n" +
                            "											<SchmeNm>\n" +
                            "												<Prtry>"+cred.get(i-1).getBankSchmeNmPrtry()+"</Prtry>\n" +
                            "											</SchmeNm>\n" +
                            "										</Othr>\n" +
                            "									</FinInstnId>\n" +
                            "									<BrnchId>\n" +
                            "										<Id>"+cred.get(i-1).getBrnchId()+"</Id>\n" +
                            "										<Nm>"+cred.get(i-1).getBrnchIdNm()+"</Nm>\n" +
                            "									</BrnchId>\n" +
                            "								</CdtrAgt>\n" +
                            "								<Cdtr/>\n" +
                            "								<CdtrAcct>\n" +
                            "									<Id>\n" +
                            "										<Othr>\n" +
                            "											<Id>"+cred.get(i-1).getCdtrAcctId()+" 00112811901</Id>\n" +
                            "											<SchmeNm>\n" +
                            "												<Prtry>"+cred.get(i-1).getBankSchmeNmPrtry()+"</Prtry>\n" +
                            "											</SchmeNm>\n" +
                            "										</Othr>\n" +
                            "									</Id>\n" +
                            "									<Ccy>"+cred.get(i-1).getCcy()+"</Ccy>\n" +
                            "								</CdtrAcct>\n" +
                            "								<RmtInf>\n" +
                            "									<Ustrd>"+cred.get(i-1).getUstrd()+"</Ustrd>\n" +
                            "								</RmtInf>\n" +
                            "							</CdtTrfTxInf>";                   
                }      
      }
                
                return ee+V;
    }
  
    public String generatepain001_ConstTest(){
          
               //   String pain001 ="<![CDATA[<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB\"><CstmrCdtTrfInitn><GrpHdr><MsgId>A2017087467738477889970803875</MsgId><CreDtTm>2020-01-14T17:57:29</CreDtTm><NbOfTxs>1</NbOfTxs><CtrlSum>10000.0000</CtrlSum><InitgPty/><DltPrvtData><FlwInd>HOMOLOGATION</FlwInd><DltPrvtDataDtl><PrvtDtInf>CANAL_VRT_WEB_MODIF</PrvtDtInf><Tp><CdOrPrtry><Cd>CHANNEL</Cd></CdOrPrtry></Tp></DltPrvtDataDtl></DltPrvtData></GrpHdr><PmtInf><PmtInfId>A2017087467778957838899975</PmtInfId><PmtMtd>TRF</PmtMtd><BtchBookg>0</BtchBookg><NbOfTxs>1</NbOfTxs><CtrlSum>10000.0000</CtrlSum><DltPrvtData><OrdrPrties><Tp>IMM</Tp><Md>CREATE</Md></OrdrPrties></DltPrvtData><PmtTpInf><InstrPrty>NORM</InstrPrty><SvcLvl><Prtry>INTERNAL</Prtry></SvcLvl></PmtTpInf><ReqdExctnDt>2020-01-21</ReqdExctnDt><Dbtr/><DbtrAcct><Id><Othr><Id>00112676601</Id><SchmeNm><Prtry>BKCOM_ACCOUNT</Prtry></SchmeNm></Othr></Id><Ccy>XAF</Ccy></DbtrAcct><DbtrAgt><FinInstnId><Nm>BANQUE CCA SIEGE</Nm><Othr><Id>10039</Id><SchmeNm><Prtry>ITF_DELTAMOP_IDETAB</Prtry></SchmeNm></Othr></FinInstnId><BrnchId><Id>10007</Id><Nm>Agence</Nm></BrnchId></DbtrAgt><CdtTrfTxInf><PmtId><InstrId>6692486951789999996</InstrId><EndToEndId>6692486788888951996</EndToEndId></PmtId><Amt><InstdAmt Ccy=\"XAF\">10000.0000</InstdAmt></Amt><CdtrAgt><FinInstnId><Nm>BANQUE CCA SIEGE</Nm><Othr><Id>10039</Id><SchmeNm><Prtry>ITF_DELTAMOP_IDETAB</Prtry></SchmeNm></Othr></FinInstnId><BrnchId><Id>10007</Id><Nm>Agence</Nm></BrnchId></CdtrAgt><Cdtr/><CdtrAcct><Id><Othr><Id>00112811901</Id><SchmeNm><Prtry>BKCOM_ACCOUNT</Prtry></SchmeNm></Othr></Id><Ccy>XAF</Ccy></CdtrAcct><RmtInf><Ustrd>TEST MOTIF</Ustrd></RmtInf></CdtTrfTxInf></PmtInf></CstmrCdtTrfInitn></Document>]]>";
                 
        return "<![CDATA[<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB\">\n" +
"					<CstmrCdtTrfInitn>\n" +
"					<GrpHdr>\n" +
"						<MsgId>A2017087467738477889970803875</MsgId>\n" +
"						<CreDtTm>2020-01-14T17:57:29</CreDtTm>\n" +
"						<NbOfTxs>1</NbOfTxs>\n" +
"						<CtrlSum>10000.0000</CtrlSum>\n" +
"						<InitgPty/>\n" +
"						<DltPrvtData>\n" +
"							<FlwInd>HOMOLOGATION</FlwInd>\n" +
"							<DltPrvtDataDtl>\n" +
"								<PrvtDtInf>CANAL_VRT_WEB_MODIF</PrvtDtInf>\n" +
"								<Tp>\n" +
"									<CdOrPrtry>\n" +
"										<Cd>CHANNEL</Cd>\n" +
"									</CdOrPrtry>\n" +
"								</Tp>\n" +
"							</DltPrvtDataDtl>\n" +
"						</DltPrvtData>\n" +
"					</GrpHdr>\n" +
"					<PmtInf>\n" +
"					<PmtInfId>A2017087467778957838899975</PmtInfId>\n" +
"					<PmtMtd>TRF</PmtMtd>\n" +
"					<BtchBookg>0</BtchBookg>\n" +
"					<NbOfTxs>1</NbOfTxs>\n" +
"					<CtrlSum>10000.0000</CtrlSum>\n" +
"					<DltPrvtData>\n" +
"						<OrdrPrties>\n" +
"							<Tp>IMM</Tp>\n" +
"							<Md>CREATE</Md>\n" +
"						</OrdrPrties>\n" +
"					</DltPrvtData>\n" +
"					<PmtTpInf>\n" +
"						<InstrPrty>NORM</InstrPrty>\n" +
"						<SvcLvl>\n" +
"							<Prtry>INTERNAL</Prtry>\n" +
"						</SvcLvl>\n" +
"					</PmtTpInf>\n" +
"						<ReqdExctnDt>2020-01-21</ReqdExctnDt>\n" +
"						<Dbtr/>\n" +
"							<DbtrAcct>\n" +
"								<Id>\n" +
"									<Othr>\n" +
"										<Id>00112676601</Id>\n" +
"										<SchmeNm>    \n" +
"											<Prtry>BKCOM_ACCOUNT</Prtry>\n" +
"										</SchmeNm>\n" +
"									</Othr>\n" +
"								</Id>\n" +
"								<Ccy>XAF</Ccy>\n" +
"							</DbtrAcct>\n" +
"							<DbtrAgt>\n" +
"								<FinInstnId>\n" +
"									<Nm>BANQUE CCA SIEGE</Nm>\n" +
"									<Othr>\n" +
"										<Id>10039</Id>\n" +
"										<SchmeNm>\n" +
"											<Prtry>ITF_DELTAMOP_IDETAB</Prtry>\n" +
"										</SchmeNm>\n" +
"									</Othr>\n" +
"								</FinInstnId>\n" +
"								<BrnchId>\n" +
"									<Id>10007</Id>\n" +
"									<Nm>Agence</Nm>\n" +
"								</BrnchId>\n" +
"							</DbtrAgt>\n" +
"							<CdtTrfTxInf>\n" +
"								<PmtId>\n" +
"									<InstrId>6692486951789999996</InstrId>\n" +
"									<EndToEndId>6692486788888951996</EndToEndId>\n" +
"								</PmtId>\n" +
"								<Amt>\n" +
"									<InstdAmt Ccy=\"XAF\">10000.0000</InstdAmt>\n" +
"								</Amt>\n" +
"								<CdtrAgt>\n" +
"									<FinInstnId>\n" +
"										<Nm>BANQUE CCA SIEGE</Nm>\n" +
"										<Othr>\n" +
"											<Id>10039</Id>\n" +
"											<SchmeNm>\n" +
"												<Prtry>ITF_DELTAMOP_IDETAB</Prtry>\n" +
"											</SchmeNm>\n" +
"										</Othr>\n" +
"									</FinInstnId>\n" +
"									<BrnchId>\n" +
"										<Id>10007</Id>\n" +
"										<Nm>Agence</Nm>\n" +
"									</BrnchId>\n" +
"								</CdtrAgt>\n" +
"								<Cdtr/>\n" +
"								<CdtrAcct>\n" +
"									<Id>\n" +
"										<Othr>\n" +
"											<Id>00112811901</Id>\n" +
"											<SchmeNm>\n" +
"												<Prtry>BKCOM_ACCOUNT</Prtry>\n" +
"											</SchmeNm>\n" +
"										</Othr>\n" +
"									</Id>\n" +
"									<Ccy>XAF</Ccy>\n" +
"								</CdtrAcct>\n" +
"								<RmtInf>\n" +
"									<Ustrd>TEST MOTIF</Ustrd>\n" +
"								</RmtInf>\n" +
"							</CdtTrfTxInf></PmtInf></CstmrCdtTrfInitn></Document>]]>";
            
      
      }
  
      
      
    
    
    
    
    
    
     private static Document convertStringToXMLDocument(String xmlString) 
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            //Parse the content to Document object
            Document doc = (Document) builder.parse(new InputSource(new StringReader(xmlString)));
           
            return doc;
        } 
        catch (ParserConfigurationException | SAXException | IOException e) 
        {
        }
        return null;
    }
    
     
    public String getMsgIdPmtInfId(){
        
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 9;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
          .limit(targetStringLength)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
//        System.out.println(generatedString);
        String v= "";
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date now = new Date();
        String strDate = "CCA"+sdfDate.format(now)+"IWCORE"+generatedString;
        
        return  strDate;
    } 
      
    public String getReqId(){
        
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 9;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
          .limit(targetStringLength)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
//        System.out.println(generatedString);
        String v= "";
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date now = new Date();
        String strDate = "CCArqid"+sdfDate.format(now)+"IWCORE"+generatedString;
        
        return  strDate;
    } 
    
        
    public String ReqdExctnDt(){
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        return sdf.format(now);
        
    } 
    public String getCreDtTm(){
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date now = new Date();
        return sdf.format(now);
        
    } 
    
    
    
}
