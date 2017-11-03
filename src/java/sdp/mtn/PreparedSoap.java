/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdp.mtn;

/**
 *
 * @author User-PC
 */
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.stream.StreamSource;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;

import javax.xml.transform.stream.StreamResult;

public class PreparedSoap {

    static final String DATE_TEMPLATE = "yyyyMMddHHmmss";
    static final SimpleDateFormat sf = new SimpleDateFormat(DATE_TEMPLATE);

    public static String postSendSMS(HashMap requestMap) {
        String to = (String) requestMap.get("to");
        String text = (String) requestMap.get("text");
        String from = (String) requestMap.get("from");
        String serviceId = (String) requestMap.get("serviceId");
        String rs = null, smsg
                = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v2=\"http://www.huawei.com.cn/schema/common/v2_1\" xmlns:loc=\"http://www.csapi.org/schema/parlayx/sms/send/v2_2/local\">\n"
                + "    <soapenv:Header>\n"
                + "        <v2:RequestSOAPHeader>\n"
                + "            <v2:spId>2340110004739</v2:spId>\n"
                + "            <v2:spPassword>" + getAuthKey("2340110004739" + "bmeB500" + sf.format(new Date())) + "</v2:spPassword>\n"
                + "            <v2:bundleID>23401220000013000</v2:bundleID>\n"
                + "            <v2:serviceId>" + serviceId + "</v2:serviceId>\n"
                + "            <v2:timeStamp>" + sf.format(new Date()) + "</v2:timeStamp>\n"
                + "            <v2:OA>" + to + "</v2:OA>\n"
                + "            <v2:FA>" + to + "</v2:FA>\n"
                + "        </v2:RequestSOAPHeader>\n"
                + "    </soapenv:Header>\n"
                + "    <soapenv:Body>\n"
                + "        <loc:sendSms>\n"
                + "            <loc:addresses>tel:" + to + "</loc:addresses>\n"
                + "            <loc:senderName>" + from + "</loc:senderName>\n"
                + "            <loc:message>" + text + "</loc:message>\n"
                + "            <loc:receiptRequest>\n"
                + "                <endpoint>http://77.75.122.3:8080/MNOGateway/MTNOpss</endpoint>\n"
                + "                <interfaceName>SmsNotification</interfaceName>\n"
                + "                <correlator>00001</correlator>\n"
                + "            </loc:receiptRequest>\n"
                + "        </loc:sendSms>\n"
                + "    </soapenv:Body>\n"
                + "</soapenv:Envelope>";

        try {
            // Create the connection
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            SOAPConnection conn = scf.createConnection();

            // Create message
            MessageFactory mf = MessageFactory.newInstance();
            SOAPMessage msg = mf.createMessage();

            // Object for message parts
            SOAPPart sp = msg.getSOAPPart();
            StringReader srd = new StringReader(smsg);
            StreamSource prepMsg = new StreamSource(srd);
            sp.setContent(prepMsg);

            // Save message
            msg.saveChanges();

            // View input
            System.out.println("\n Soap request:\n");
            msg.writeTo(System.out);
            System.out.println();

            // Send
            String urlval = "http://41.206.4.162:8310/SendSmsService/services/SendSms/";
            SOAPMessage rp = conn.call(msg, urlval);

            // View the output
            System.out.println("XML response\n");

            // Create transformer
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();

            // Get reply content
            Source sc = rp.getSOAPPart().getContent();

            // Set output transformation
            StreamResult result = new StreamResult(System.out);
            rs = result.toString();
            tf.transform(sc, result);
            System.out.println();

            // Close connection
            conn.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    public static String postMTNSubscribe(HashMap requestMap) {
        String phone = (String) requestMap.get("phone");
        String productId = (String) requestMap.get("productId");
        String rs = null, smsg
                = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v2=\"http://www.huawei.com.cn/schema/common/v2_1\" xmlns:loc=\"http://www.csapi.org/schema/parlayx/sms/send/v2_2/local\">\n"
                + "    <soapenv:Header>\n"
                + "        <v2:RequestSOAPHeader>\n"
                + "            <v2:spId>2340110004739</v2:spId>\n"
                + "            <v2:spPassword>" + getAuthKey("2340110004739" + "bmeB500" + sf.format(new Date())) + "</v2:spPassword>\n"
                //                + "            <v2:bundleID>23401220000013000</v2:bundleID>\n"
                //                + "            <v2:serviceId>" + serviceId + "</v2:serviceId>\n"
                + "            <v2:timeStamp>" + sf.format(new Date()) + "</v2:timeStamp>\n"
                + "            <oauth_token></oauth_token>\n"
                + "        </v2:RequestSOAPHeader>\n"
                + "    </soapenv:Header>\n"
                + "    <soapenv:Body>\n"
                + "   <loc:subscribeProductRequest>\n"
                + "        <loc:subscribeProductReq>\n"
                + "            <userID>\n"
                + "                <ID>" + phone + "</ID>\n"
                + "                <type>0</type>\n"
                + "            </userID>\n"
                + "            <subInfo>\n"
                + "                <productID>" + productId + "</productID>\n"
                + "                <operCode>zh</operCode>\n"
                + "                <isAutoExtend>1</isAutoExtend>\n"
                + "                <channelID>100</channelID>\n"
                + "            </subInfo>\n"
                + "        </loc:subscribeProductReq>\n"
                + "    </loc:subscribeProductRequest> "
                + "    </soapenv:Body>\n"
                + "</soapenv:Envelope>";

        try {
            // Create the connection
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            SOAPConnection conn = scf.createConnection();

            // Create message
            MessageFactory mf = MessageFactory.newInstance();
            SOAPMessage msg = mf.createMessage();

            // Object for message parts
            SOAPPart sp = msg.getSOAPPart();
            StringReader srd = new StringReader(smsg);
            StreamSource prepMsg = new StreamSource(srd);
            sp.setContent(prepMsg);

            // Save message
            msg.saveChanges();

            // View input
            System.out.println("\n Soap request:\n");
            msg.writeTo(System.out);
            System.out.println();

            // Send
            String urlval = "http://41.206.4.162:8310/SubscribeManageService/services/SubscribeManage/";
            SOAPMessage rp = conn.call(msg, urlval);

            // View the output
            System.out.println("XML response\n");

            // Create transformer
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();
            StringWriter writer = new StringWriter();
            // Get reply content
            Source sc = rp.getSOAPPart().getContent();
            // Set output transformation
            StreamResult result = new StreamResult(writer);
            tf.transform(sc, result);
            rs = writer.toString();
            System.out.println(rs);

            // Close connection
            conn.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    private static String getAuthKey(String text) {
        String hashtext = null;
        try {
//            String plaintext = "your text here";
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(text.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hashtext = bigInt.toString(16);
// Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PreparedSoap.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hashtext;
    }
}
