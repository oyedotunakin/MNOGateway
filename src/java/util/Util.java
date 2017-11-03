package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import static util.QuickUtils.s_sslSocketFactory;
import static util.QuickUtils.s_x509TrustManager;

public class Util {

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

    public static final char[] HEX_CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    public static final char[] SECURE_CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
        'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '=',
        '!', '"', '#', '¤', '%', '&', '/', '(', ')'};

    public static void checkNullValues(HashMap<String, String> hashMap) throws Exception {
        Iterator<String> hashEntry = hashMap.keySet().iterator();
        while (hashEntry.hasNext()) {
            String key = (String) hashEntry.next();
            String value = hashMap.get(key);
            if (value == null || value.isEmpty()) {
                throw new Exception("Value not supplied for " + key);
            }
        }
    }

    public static HashMap serializeMapStringToMap(String map) {
        HashMap<String, String> data = new HashMap();
        Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
        String[] split = p.split(map);
        for (int i = 1; i + 2 <= split.length; i += 2) {
            data.put(split[i], split[i + 1]);
        }
        return data;
    }

    public static String[] deriveArrayFromString(String sa) {
        sa = sa.replace("[", "").replace("]", "");
        String[] parts = sa.split(",");
        return parts;
    }

    public static String getPayloadItem(String payload, String tagName) throws ParserConfigurationException, SAXException, IOException {
        {
            String textContent = null;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(payload)));
            doc.getDocumentElement().normalize();
            NodeList allElems = doc.getFirstChild().getChildNodes();

            for (int temp = 0; temp < allElems.getLength(); temp++) {
                Node nNode = allElems.item(temp);
                if (nNode.getNodeType() == 1) {
                    Element eElement = (Element) nNode;

                    if (eElement.getTagName().equals(tagName)) {
                        textContent = eElement.getTextContent();
                    }
                }
            }
            return textContent;
        }
    }

    public static String generatePIN() {
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        long l = r.nextLong();
        return String.valueOf(l).substring(1, 5);
    }

    public static String paramerize(HashMap requestMap) {
        StringBuilder param = new StringBuilder();
        Iterator hashEntry = requestMap.keySet().iterator();
        while (hashEntry.hasNext()) {
            try {

                String key = (String) hashEntry.next();
                if (requestMap.get(key) == null) {
                    continue;
                }
                param.append("&").append(key).append("=").append(URLEncoder.encode((String) requestMap.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println(param.toString());
        return param.toString();
    }

    public static HashMap sanitizeMap(HashMap payloadMap, String spc, String undsc) {
        Iterator hashEntry = payloadMap.keySet().iterator();
        while (hashEntry.hasNext()) {
            String key = (String) hashEntry.next();
            String val = (String) payloadMap.get(key);
            if (val.contains(spc)) {
                payloadMap.replace(key, val.replace(spc, undsc));
            }
        }
        return payloadMap;
    }

    public static String serializeMapToXml(HashMap dataMap, String root) {
        StringBuilder sb = new StringBuilder();
        dataMap.keySet();
        Iterator hashEntry = dataMap.keySet().iterator();
        sb.append("<").append(root).append(">");
        while (hashEntry.hasNext()) {
            String key = (String) hashEntry.next();
            sb.append("<").append(key).append(">").append(dataMap.get(key)).append("</").append(key).append(">");
        }
        sb.append("</").append(root).append(">");
        return sb.toString();
    }

    private Properties colmap;
    private Properties server;
    //private Properties tabprop;

    public Util() throws FileNotFoundException, IOException {
        String baseconfigdir = "/etc/apps/";
        //   String baseconfigdir = "C://Nibbs//";
//        this.colmap = new Properties();
//        this.server = new Properties();
//        this.colmap.load(new FileInputStream(baseconfigdir + "batchcolumns.conf"));
//        this.server.load(new FileInputStream(baseconfigdir + "server.conf"));
    }

    public synchronized static String getQTTrxnID() {
        String sessionId = "";
        try {
            String date;
            String mds = getRandomNumber(4);
            Date d = new Date();

            SimpleDateFormat sd = new SimpleDateFormat("yyMMddHHmmss");
            date = sd.format(d);
            sessionId = date.substring(6) + mds;

        } catch (Exception ex) {
            ex.printStackTrace();
            //  Log.l.errorLog.error(ex);
        }
        return sessionId;
    }

    public String[] getDataNamesByAction(String action) {
        String cols = colmap.getProperty(action);
        String[] parts = cols.split(",");
        String[] names = new String[parts.length];
        int i = 0;
        for (String cm : parts) {
            names[i] = cm.split("/")[0];
            i++;
        }
        return names;
    }

    public HashMap<String, String> getColumnsMapByAction(String action) {
        HashMap<String, String> colMap = new HashMap();
        String cols = colmap.getProperty(action);
        String[] parts = cols.split(",");
        for (String cm : parts) {
            colMap.put(cm.split("/")[0], cm.split("/")[1]);
        }
//        System.out.println(colMap.toString());
        return colMap;
    }

    public String getServerValue(String key) {
        return server.getProperty(key);
    }

    public static String getRandomNumber(int digCount) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(digCount);
        for (int i = 0; i < digCount; i++) {
            sb.append((char) ('0' + rnd.nextInt(10)));
        }
        return sb.toString();
    }

    public static String postTrxn(HashMap requestMap, String baseUrl, int style) throws MalformedURLException, IOException {
        System.out.println("Did get Here " + baseUrl + requestMap);
        System.out.println("data " + requestMap.toString());

        String responseMessage;
        String inputLine = null;
        int responseCode;
        URL myUrl = new URL(composeTrxnURL(requestMap, baseUrl, style));
        System.out.println("Opening connection " + myUrl.toString());
        HttpURLConnection response = (HttpURLConnection) myUrl.openConnection();

        responseCode = response.getResponseCode();
        System.out.println("HTTP result " + response.getResponseMessage() + " code " + response.getResponseCode());
        responseMessage = response.getResponseMessage();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        response.getInputStream()));

        while (in.ready()) {
            inputLine = in.readLine();
            System.out.println(inputLine);
        }

        return inputLine;
    }

    public static String composeTrxnURL(HashMap requestMap, String baseUrl, int style) {
        StringBuilder param = new StringBuilder();
        param.append(baseUrl);
        Iterator hashEntry = requestMap.keySet().iterator();
        while (hashEntry.hasNext()) {
            try {
                String key = (String) hashEntry.next();
                if (0 == style) {
                    param.append("&").append(key).append("=").append(URLEncoder.encode((String) requestMap.get(key), "UTF-8"));
                } else {
                    param.append("/").append(URLEncoder.encode((String) requestMap.get(key), "UTF-8"));
                }
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println(param.toString());
        return param.toString();
    }

    private static String createPostDataFromMap(Map fields) {
        StringBuilder buf = new StringBuilder();

        String ampersand = "";

        // append all fields in a data string
        for (Iterator i = fields.keySet().iterator(); i.hasNext();) {

            String key = (String) i.next();
            String value = (String) fields.get(key);

            if ((value != null) && (value.length() > 0)) {
                // append the parameters
                buf.append(ampersand);
                buf.append(URLEncoder.encode(key));
                buf.append('=');
                buf.append(URLEncoder.encode(value));
            }
            ampersand = "&";
        }

        // return string 
        return buf.toString();
    }

    public static String generatePassword(int numchars) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numchars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        String p = sb.toString().substring(0, numchars);
        System.out.println(p);
        return p;
    }

    public static void main(String[] args) {
//        String regex = "^2.*\\|.*";
//        String s = "2015-08-03|2015-08-2";
//        System.out.println(s.matches(regex));
        vendAirtime("mtn", "08162612749", 50);

    }

    public static <T> void writeCsv(List<List<T>> csv, char separator, OutputStream output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
        for (List<T> row : csv) {
            for (Iterator<T> iter = row.iterator(); iter.hasNext();) {
                String field = String.valueOf(iter.next()).replace("\"", "\"\"");
                if (field.indexOf(separator) > -1 || field.indexOf('"') > -1) {
                    field = '"' + field + '"';
                }
                writer.append(field);
                if (iter.hasNext()) {
                    writer.append(separator);
                }
            }
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }

    public static String vendAirtime(String network, String phone, int value) {

        String merchId, plainKey, merchRef, token, date, serviceId;
        switch (network) {
            case "glo":
                serviceId = "A03E";
                break;
            case "mtn":
                serviceId = "A04E";
                break;
            case "airtel":
                serviceId = "A01E";
                break;
            case "etisalat":
                serviceId = "A02E";
                break;
            default:
                serviceId = "000";
        }
        if (phone.startsWith("234")) {
            phone = phone.replaceFirst("234", "0");
        }
        String status = "failed";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            String host = "https://www.creditswitch.com/mercht/trans/";

            merchId = "14801";
            plainKey = "ElNaKMep39Wl";
            merchRef = String.valueOf(System.currentTimeMillis());
            Date d = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yyyy HH:mm z");
            sf.setTimeZone(TimeZone.getTimeZone("GMT"));
            date = sf.format(d);
            token = "Z01aRDhGfmJ5WlM4dTttMnJKUFo4Ki01LDJLMEx6fSRWJCx6Zig7U14qaE00cUJHMSljI3tB";
            byte[] hash = digest.digest(plainKey.getBytes(StandardCharsets.UTF_8));
            String merchKey = Hex.encodeHexString(hash);
            String s = String.valueOf(merchId + merchRef) + Integer.toString(value) + phone + merchKey + date;
            System.out.println(s);
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(host);

// Request parameters and other properties.
            List<NameValuePair> params = new ArrayList<>(2);
            params.add(new BasicNameValuePair("serviceId", serviceId));
//            params.add(new BasicNameValuePair("merchRef", merchRef));
            params.add(new BasicNameValuePair("merchKey", merchKey));
            params.add(new BasicNameValuePair("merchRef", merchRef));
            params.add(new BasicNameValuePair("merchId", merchId));
            params.add(new BasicNameValuePair("date", date)); // date));
            params.add(new BasicNameValuePair("amount", String.valueOf(value)));
            params.add(new BasicNameValuePair("recipient", phone));
            params.add(new BasicNameValuePair("signature", createSignature(s, token)));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            System.out.println(params);
            System.out.println(httppost.toString());
            System.out.println(params.toString());

//Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            String r_s = EntityUtils.toString(entity, "UTF-8");
            status = r_s;
            System.out.println(r_s);

        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;

    }

    public static String creditSwitchVendAirtime(String network, String phone, int value) {

        String merchId, plainKey, merchRef, token, date, serviceId, loginId, result = null;

        loginId = "14801";
        String privateKey = "150866ac1db3e3fd4e91f754c538d2adc2ec3e27dd1716fe1807473b25ce4341a860462135c2977a84a4e9b4a8e4a52f8d62b7d98088135dc9ccf0b2d4d1aacc";
        String publicKey = "ba46bae59cfe557f76e6dec10cfb17033759ba5fb2a03f48126a26979c6ae74ea6598aa40c62e78f55fcadb50c608ea9ad928b2ad97fa6f84879062ee0750bb6";

        switch (network) {
            case "glo":
                serviceId = "A03E";
                break;
            case "mtn":
                serviceId = "A04E";
                break;
            case "airtel":
                serviceId = "A01E";
                break;
            case "etisalat":
                serviceId = "A02E";
                break;
            default:
                serviceId = "000";
        }
        if (phone.startsWith("234")) {
            phone = phone.replaceFirst("234", "0");
        }
        String host = "https://creditswitch.net/api/v1/mvend";

//            merchId = "14801";
//        plainKey = "ElNaKMep39Wl";
        merchRef = String.valueOf(System.currentTimeMillis());
        Date d = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yyyy HH:mm z");
        sf.setTimeZone(TimeZone.getTimeZone("GMT"));
        date = sf.format(d);

        HttpClient client = getNewHttpClient();
        HttpPost httpPost = new HttpPost(host);

        Map<String, String> map = new HashMap<>();
        map.put("loginId", loginId);
        map.put("key", publicKey);
        map.put("requestId", merchRef);
        map.put("serviceId", serviceId);
        map.put("amount", String.valueOf(value));
        map.put("recipient", phone);
        map.put("date", date);
        map.put("checksum", getChecksumAirtimeData(loginId, merchRef, serviceId, privateKey, phone, value));
        StringEntity entity = null;
        System.out.println(map);
        String json = new JSONObject(map).toString();
        System.out.println(json);
        HttpResponse response;
        try {
            entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            response = client.execute(httpPost);

            result = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println(result);
            client.getConnectionManager().shutdown();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(result);
        return result;

    }

    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    static String getChecksumAirtimeData(String loginId, String requestId, String serviceId, String privateKey, String recipient, int requestAmount) {

        String concatString = "" + loginId + "|" + requestId + "|" + serviceId + "|" + requestAmount + "|" + privateKey + "|" + recipient + "";
        byte[] message = BCrypt.hashpw(concatString, BCrypt.gensalt()).getBytes(StandardCharsets.UTF_8);
        String checksum = Base64.encodeBase64String(message);
        return checksum;
    }

    static String getChecksumMertDetail(String loginId, String privateKey) {
        String concatString = "" + loginId + "|" + privateKey;
        byte[] message = BCrypt.hashpw(concatString, BCrypt.gensalt()).getBytes(StandardCharsets.UTF_8);
        String checksum = Base64.encodeBase64String(message);
        return checksum;
    }

    public static String hash(String plain_text, String secret) {
        String hash = null;
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

            sha256_HMAC.init(secret_key);
            hash = Base64.encodeBase64String(sha256_HMAC.doFinal(plain_text.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            ex.printStackTrace();

        }
        return hash;
    }

    private static String createSignature(String plain_text, String secret) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        String hex_plain = Hex.encodeHexString(digest.digest(plain_text.getBytes()));
//        System.out.println(plain_text);
        byte[] decoded = Base64.decodeBase64(secret);
        String ss = new String(decoded, "UTF-8");
        System.out.println(ss);
//        String hex_plain = Hex.encodeHexString(digest.digest(plain_text.getBytes()));
//        System.out.println(hex_plain);
        String string2 = Base64.encodeBase64String(digest.digest(plain_text.getBytes(StandardCharsets.UTF_8)));
        System.out.println(string2);
//        byte[] decodeBase64 = Base64.decodeBase64(secret);
        Mac sha1_HMAC = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret_key = new SecretKeySpec(decoded, "HmacSHA1");
        sha1_HMAC.init(secret_key);

        String signature = Base64.encodeBase64String(sha1_HMAC.doFinal(string2.getBytes(StandardCharsets.UTF_8)));
        System.out.println(signature);
        return signature;
    }
}
