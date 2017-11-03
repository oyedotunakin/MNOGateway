package util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class QuickUtils {

    public static int PRETTY_PRINT_INDENT_FACTOR = 4;
    public static X509TrustManager s_x509TrustManager = null;
    public static SSLSocketFactory s_sslSocketFactory = null;

    static {
        s_x509TrustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public boolean isClientTrusted(X509Certificate[] chain) {
                return true;
            }

            public boolean isServerTrusted(X509Certificate[] chain) {
                return true;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] xcs, String string) {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] xcs, String string) {

            }
        };

        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{s_x509TrustManager}, null);
            s_sslSocketFactory = context.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String convert(String XML_STRING) {
        String jsonPrettyPrintString = null;
//        JSONObject header = null;
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(XML_STRING);
//            JSONObject chResp = (JSONObject) xmlJSONObj.get("ns2:ChargeResponse");
//             header =  (JSONObject) chResp.get("ns2:TransactionHeader");
            jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
//            System.out.println(jsonPrettyPrintString);
        } catch (JSONException je) {
            System.out.println(je.toString());
        }
        return jsonPrettyPrintString;
    }

    public static void main(String[] args) {
        String stuff = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<ns2:ChargeResponse xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ns2=\"UCAPNS\" xmlns:ns1=\"http://example.com/ContentCharge.wsdl\">\n"
                + "        <ns2:TransactionHeader>\n"
                + "                <ns2:TransactionID>1437811729</ns2:TransactionID>\n"
                + "                <ns2:Retry>0</ns2:Retry>\n"
                + "                <ns2:ResponseCode>0</ns2:ResponseCode>\n"
                + "        </ns2:TransactionHeader>\n"
                + "        <ns2:CurrencyAmount>\n"
                + "                <ns2:DebitIndicator>0</ns2:DebitIndicator>\n"
                + "                <ns2:CurrencyCode>NGN</ns2:CurrencyCode>\n"
                + "                <ns2:ScallingFactor>10000</ns2:ScallingFactor>\n"
                + "                <ns2:AmountValue>20000</ns2:AmountValue>\n"
                + "        </ns2:CurrencyAmount>\n"
                + "</ns2:ChargeResponse>";
        convert(stuff);
    }

    public static String postSoap(org.csapi.schema.parlayx.sms.send.v2_2.local.SendSms ssms) {

        //Code to make a webservice HTTP request
        String responseString = "";
        String outputString = "";
        StringBuilder sb = new StringBuilder();
        try {
            String wsURL = "http://41.206.4.162:8310/SendSmsService/services/SendSms/";
            URL url = new URL(wsURL);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v2=\"http://www.huawei.com.cn/schema/common/v2_1\"xmlns:loc=\"http://www.csapi.org/schema/parlayx/sms/send/v2_2/local\">\n"
                    + "    <soapenv:Header>\n"
                    + "        <v2:RequestSOAPHeader>\n"
                    + "            <v2:spId>000201</v2:spId>\n"
                    + "            <v2:spPassword>e6434ef249df55c7a21a0b45758a39bb</v2:spPassword>\n"
                    + "            <v2:bundleID>256000039</v2:bundleID>\n"
                    + "            <v2:serviceId>35000001000001</v2:serviceId>\n"
                    + "            <v2:timeStamp>20100731064245</v2:timeStamp>\n"
                    + "            <v2:OA>8612312345678</v2:OA>\n"
                    + "            <v2:FA>8612312345678</v2:FA>\n"
                    + "            <v2:linkid>12345678901111</v2:linkid>\n"
                    + "            <v2:presentid>22345678901113</v2:presentid>\n"
                    + "        </v2:RequestSOAPHeader>\n"
                    + "    </soapenv:Header>\n"
                    + "    <soapenv:Body>\n"
                    + "        <loc:sendSms>\n"
                    + "            <loc:addresses>tel:8612312345678</loc:addresses>\n"
                    + "            <loc:senderName>321123</loc:senderName>\n"
                    + "            <loc:message>Hello world.</loc:message>\n"
                    + "            <loc:receiptRequest>\n"
                    + "                <endpoint>http://10.138.38.139:9080/notify</endpoint>\n"
                    + "                <interfaceName>SmsNotification</interfaceName>\n"
                    + "                <correlator>00001</correlator>\n"
                    + "            </loc:receiptRequest>\n"
                    + "        </loc:sendSms>\n"
                    + "    </soapenv:Body>\n"
                    + "</soapenv:Envelope>";

            byte[] buffer;// = new byte[xmlInput.length()];
            buffer = xmlInput.getBytes(StandardCharsets.UTF_8);
            bout.write(buffer);
            byte[] b = bout.toByteArray();
//            String SOAPAction
//                    = "http://41.206.4.162:8310/SendSmsService/services/SendSms";
// Set the appropriate HTTP parameters.
            httpConn.setRequestProperty("Content-Length",
                    String.valueOf(b.length));
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
//            httpConn.setRequestProperty("SOAPAction", "");
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            OutputStream out = httpConn.getOutputStream();
//Write the content of the request to the outputstream of the HTTP Connection.
            out.write(b);
            out.close();
            try (InputStreamReader isr
                    = new InputStreamReader(httpConn.getInputStream());
                    BufferedReader in = new BufferedReader(isr)) {
                while ((responseString = in.readLine()) != null) {
                    sb.append(responseString);
//                    outputString = outputString + responseString;
                }
            }
            outputString = sb.toString();
//            System.out.println(outputString);

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
//        String r = outputString.substring((outputString.indexOf("<getTransactionDataResult>") + 26), outputString.indexOf("</getTransactionDataResult"));
        return outputString;
    }

    public static String doPost(String host, String data,
            boolean useProxy, String proxyHost, int proxyPort) throws IOException {

        InputStream is;
        OutputStream os;
        int vpc_Port = 443;
        String fileName = "";
        boolean useSSL = false;

        // determine if SSL encryption is being used
        if (host.substring(0, 8).equalsIgnoreCase("HTTPS://")) {
            useSSL = true;
            // remove 'HTTPS://' from host URL
            host = host.substring(8);
            // get the filename from the last section of vpc_URL
            fileName = host.substring(host.lastIndexOf("/"));
            // get the IP address of the VPC machine
            host = host.substring(0, host.lastIndexOf("/"));
        }

        // use the next block of code if using a proxy server
        if (useProxy) {
            Socket s = new Socket(proxyHost, proxyPort);
            os = s.getOutputStream();
            is = s.getInputStream();
            // use next block of code if using SSL encryption
            if (useSSL) {
                String msg = "CONNECT " + host + ":" + vpc_Port + " HTTP/1.0\r\n" + "User-Agent: HTTP Client\r\n\r\n";
                os.write(msg.getBytes());
                byte[] buf = new byte[4096];
                int len = is.read(buf);
                String res = new String(buf, 0, len);

                // check if a successful HTTP connection
                if (!res.contains("200")) {
                    throw new IOException("Proxy would now allow connection - " + res);
                }

                // write output to VPC
                SSLSocket ssl = (SSLSocket) s_sslSocketFactory.createSocket(s, host, vpc_Port, true);
                ssl.startHandshake();
                os = ssl.getOutputStream();
                // get response data from VPC
                is = ssl.getInputStream();
                // use the next block of code if NOT using SSL encryption
            } else {
                fileName = host;
            }
            // use the next block of code if NOT using a proxy server
        } else {
            // use next block of code if using SSL encryption
            if (useSSL) {
                Socket s = s_sslSocketFactory.createSocket(host, vpc_Port);
                os = s.getOutputStream();
                is = s.getInputStream();
                // use next block of code if NOT using SSL encryption
            } else {
                Socket s = new Socket(host, vpc_Port);
                os = s.getOutputStream();
                is = s.getInputStream();
            }
        }

        String req = "POST " + fileName + " HTTP/1.0\r\n"
                + "User-Agent: HTTP Client\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "Content-Length: " + data.length() + "\r\n\r\n"
                + data;
        System.out.println("========= \n" + req);
        os.write(req.getBytes());
        String res = new String(readAll(is));
        System.out.println("Result:");
        System.out.println(res);
        // check if a successful connection
        if (!res.contains("200")) {
            throw new IOException("Connection Refused - " + res);
        }

        if (res.indexOf("404 Not Found") > 0) {
            throw new IOException("File Not Found Error - " + res);
        }

        int resIndex = res.indexOf("\r\n\r\n");
        String body = res.substring(resIndex + 4, res.length());
        return body;
    }

    private static byte[] readAll(InputStream is) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];

        while (true) {
            int len = is.read(buf);
            if (len < 0) {
                break;
            }
            baos.write(buf, 0, len);
        }
        return baos.toByteArray();
    }
}
