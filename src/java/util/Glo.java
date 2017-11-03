package util;

import com.example.contentcharge.ContentChargeFault;
import com.example.contentcharge.InputType;
import com.example.contentcharge.ObjectFactory;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import ucapns.APartyType;
import ucapns.AmountType;
import ucapns.CommodityType;
import ucapns.RequestType;
import ucapns.TimeValueType;
import ucapns.TransactionHeaderType;
import ucapns.VolumeType;

public class Glo {

    public static void main(String[] args) {
        ObjectFactory obf1 = new ObjectFactory();
        InputType iType = obf1.createInputType();
        ucapns.ObjectFactory obf2 = new ucapns.ObjectFactory();
        RequestType rqType = obf2.createRequestType();
        //Start Header
        TransactionHeaderType header = obf2.createTransactionHeaderType();
//        header.setResponseCode(BigInteger.ZERO);
        header.setRetry((byte) 0);
        header.setTransactionID("67656");
        //End Header
        APartyType aParty = obf2.createAPartyType();
        aParty.setMSISDN("08053060595");
        aParty.setMSC("08053060595");

        CommodityType commodity = obf2.createCommodityType();
        commodity.setDescription("Infotainment");
        commodity.setID((byte) 3);
        commodity.setSubID("64010");

//        CurrencyAmountType currencyAmount = obf2.createCurrencyAmountType();
        TimeValueType timeValue = obf2.createTimeValueType();
        timeValue.setTime(BigInteger.valueOf(Calendar.getInstance().getTimeInMillis() / 1000l));
        timeValue.setTimeOffset(new BigInteger("-2"));
        //Begin Volume
        VolumeType volume = obf2.createVolumeType();
        volume.setUnit("10002");
        AmountType amount = obf2.createAmountType();
        amount.setExponent(BigInteger.ZERO);
        amount.setSign((byte) 0);
        amount.setValue(11l);
        volume.setAmount(amount);
        //End Volume
        rqType.setCommodity(commodity);
        rqType.setAParty(aParty);
        rqType.setContentID(null);
        rqType.setDomain(null);
        rqType.setMerchantName("DML");
        rqType.setOperationCode((byte) 0);
        rqType.setRated((byte) 1);
        rqType.setTimeOfEvent(timeValue);
        rqType.setTrafficID(BigInteger.ONE);
        rqType.setTransactionHeader(header);
        rqType.setURL(null);
        rqType.setValidityInterval(24);
        rqType.setVolume(volume);
        rqType.setContentID("1");
        iType.setInput(rqType);
        try {
//            System.out.println(rqType.toString());
            contentCharge(iType);
        } catch (ContentChargeFault ex) {
            Logger.getLogger(Glo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Glo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void charge(String amount, String msisdn, String ref) {
        ObjectFactory obf1 = new ObjectFactory();
        InputType iType = obf1.createInputType();
        ucapns.ObjectFactory obf2 = new ucapns.ObjectFactory();
        RequestType rqType = obf2.createRequestType();
        //Start Header
        TransactionHeaderType header = obf2.createTransactionHeaderType();
//        header.setResponseCode(BigInteger.ZERO);
        header.setRetry((byte) 0);
        header.setTransactionID(ref);
        //End Header
        APartyType aParty = obf2.createAPartyType();
        aParty.setMSISDN(msisdn);
        aParty.setMSC(msisdn);

        CommodityType commodity = obf2.createCommodityType();
        commodity.setDescription("Infotainment");
        commodity.setID((byte) 3);
        commodity.setSubID("64010");

//        CurrencyAmountType currencyAmount = obf2.createCurrencyAmountType();
        TimeValueType timeValue = obf2.createTimeValueType();
        timeValue.setTime(BigInteger.valueOf(Calendar.getInstance().getTimeInMillis() / 1000l));
        timeValue.setTimeOffset(new BigInteger("-2"));
        //Begin Volume
        VolumeType volume = obf2.createVolumeType();
        volume.setUnit("10002");
        AmountType amountType = obf2.createAmountType();
        amountType.setExponent(BigInteger.ZERO);
        amountType.setSign((byte) 0);
        amountType.setValue(Integer.parseInt(amount));
        volume.setAmount(amountType);
        //End Volume
        rqType.setCommodity(commodity);
        rqType.setAParty(aParty);
        rqType.setContentID(null);
        rqType.setDomain(null);
        rqType.setMerchantName("DML");
        rqType.setOperationCode((byte) 0);
        rqType.setRated((byte) 1);
        rqType.setTimeOfEvent(timeValue);
        rqType.setTrafficID(BigInteger.ONE);
        rqType.setTransactionHeader(header);
        rqType.setURL(null);
        rqType.setValidityInterval(24);
        rqType.setVolume(volume);
        rqType.setContentID("1");
        iType.setInput(rqType);
        try {
//            System.out.println(rqType.toString());
            contentCharge(iType);
        } catch (ContentChargeFault ex) {
            Logger.getLogger(Glo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Glo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static com.example.contentcharge.OutputType contentCharge(com.example.contentcharge.InputType request) throws com.example.contentcharge.ContentChargeFault, MalformedURLException {

//        URL url = new URL("file:/C:/Users/akin/Documents/NetBeansProjects/MNOGateway/web/WEB-INF/glogateway.wsdl");
        URL url = new URL("http://41.203.65.164:6028");
        com.example.contentcharge.ContentCharge service = new com.example.contentcharge.ContentCharge(url);
        com.example.contentcharge.ContentChargePortType port = service.getContentCharge();
        return port.contentCharge(request);
    }

}
