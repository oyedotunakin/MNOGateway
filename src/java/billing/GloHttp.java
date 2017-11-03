
package billing;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class GloHttp {

    public static void main(String[] args) {

        try {
            URL url = new URL("http://41.203.65.164:6028/");
            String encoding = "DigitalM:D1g1t3l";

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            InputStream content = (InputStream) connection.getInputStream();
            BufferedReader in
                    = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
//    private HttpMethod doREST(String request, RequestEntity entity) throws Exception {
//        String uri = request;
//        HttpMethod method;
//        if ( entity != null ){
//            method = new PostMethod(uri);
//            ((PostMethod) method).setRequestEntity(entity);
//
//        } else {
//            method = new GetMethod(uri);
//        }
//        configureHttpMethod(method);
//        HttpClient client = getHttpClient();
//        client.getParams().setParameter(HttpClientParams.SO_TIMEOUT, timeoutLength);
//        client.executeMethod(method);
//        return method;
//    }

    public static String chargeGlo(BigInteger refId,String subId,String msisdn,String amount) {
        String body = null;
        try {

            String data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                    + "<ChargeRequest xmlns=\"UCAPNS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"UCAPNS file:///C:/UCAPSchema.xsd\">\n"
                    + "    <TransactionHeader>\n"
                    + "        <TransactionID>"+refId+"</TransactionID>\n"
                    + "        <Retry>0</Retry>\n"
                    + "    </TransactionHeader>\n"
                    + "    <OperationCode>0</OperationCode>\n"
                    + "    <Rated>1</Rated>\n"
                    + "    <MerchantName>JokeService</MerchantName>\n"
                    + "    <Commodity>\n"
                    + "        <ID>3</ID>\n"
                    + "        <SubID>"+subId+"</SubID>\n"
                    + "        <Description>Picture</Description>\n"
                    + "    </Commodity>\n"
                    + "    <Volume>\n"
                    + "        <Amount>\n"
                    + "            <Sign>0</Sign>\n"
                    + "            <Value>"+amount+"</Value>\n"
                    + "            <Exponent>0</Exponent>\n"
                    + "        </Amount>\n"
                    + "        <Unit>10002</Unit>\n"
                    + "    </Volume>\n"
                    + "    <TimeOfEvent>\n"
                    + "        <Time>"+BigInteger.valueOf(Calendar.getInstance().getTimeInMillis()/1000l)+"</Time>\n"
                    + "        <TimeOffset>-2</TimeOffset>\n"
                    + "    </TimeOfEvent>\n"
                    + "    <TrafficID>1</TrafficID>\n"
                    + "    <ValidityInterval>24</ValidityInterval>\n"
                    + "    <AParty>\n"
                    + "        <MSISDN>"+msisdn+"</MSISDN>\n"
                    + "        <IMSI>"+msisdn+"</IMSI>\n"
                    + "        <MSC></MSC>\n"
                    + "    </AParty>\n"
                    + "    <URL></URL>\n"
                    + "    <Domain></Domain>\n"
                    + "    <ContentID>text/html</ContentID>\n"
                    + "</ChargeRequest>";
            DefaultHttpClient http = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://41.203.65.164:6028/");
            UsernamePasswordCredentials creds = new UsernamePasswordCredentials("digitalm", "digitalm");
            post.addHeader(BasicScheme.authenticate(creds, "US-ASCII", false));
            StringEntity entity = new StringEntity(data);
            entity.setContentType("text/xml");
            post.setEntity(entity);
            org.apache.http.HttpResponse response = http.execute(post);
            ResponseHandler<String> handler = new BasicResponseHandler();
            body = handler.handleResponse(response);
            int code = response.getStatusLine().getStatusCode();
//            response.getEntity();
            System.out.println("Response body " + body);
            System.out.println("Code " + code);
        } catch (IOException ex) {
            Logger.getLogger(GloHttp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return body;
    }

}
