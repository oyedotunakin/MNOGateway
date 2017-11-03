/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdp.sms.mtn;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import sdp.mtn.MTNDataSync;
import util.EndPoints;

/**
 *
 * @author User-PC
 */
@WebService(serviceName = "SmsNotificationService", portName = "SmsNotificationPort", endpointInterface = "org.csapi.wsdl.parlayx.sms.notification.v2_2._interface.SmsNotification", targetNamespace = "http://www.csapi.org/wsdl/parlayx/sms/notification/v2_2/interface", wsdlLocation = "WEB-INF/wsdl/MTNSMSNotification/parlayx_sms_notification_interface_2_2Wrapper.wsdl")
public class MTNSMSNotification {

    public void notifySmsReception(java.lang.String correlator, org.csapi.schema.parlayx.sms.v2_2.SmsMessage message) {
        String r;
        org.json.simple.JSONObject jo = new org.json.simple.JSONObject();
        jo.put("Correlator", correlator);
        jo.put("MessageTime", message.getDateTime().toString());
        jo.put("Text", message.getMessage());
        jo.put("Sender", message.getSenderAddress());
        jo.put("SMSActivationNumber", message.getSmsServiceActivationNumber());
        HashMap h = new HashMap();
        h.put("Data", jo.toJSONString());
        try {
            r = util.Util.postTrxn(h, EndPoints.mtnApp, 1);
        } catch (IOException ex) {
            Logger.getLogger(MTNDataSync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void notifySmsDeliveryReceipt(java.lang.String correlator, org.csapi.schema.parlayx.sms.v2_2.DeliveryInformation deliveryStatus) {
        //TODO implement this method
        //Pls implement client code
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
