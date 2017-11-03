/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdp.sms.mtn;

import javax.jws.WebService;
import org.csapi.wsdl.parlayx.sms.receive.v2_2._interface.PolicyException;
import org.csapi.wsdl.parlayx.sms.receive.v2_2._interface.ServiceException;

/**
 *
 * @author User-PC
 */
@WebService(serviceName = "ReceiveSmsService", portName = "ReceiveSmsPort", endpointInterface = "org.csapi.wsdl.parlayx.sms.receive.v2_2._interface.ReceiveSms", targetNamespace = "http://www.csapi.org/wsdl/parlayx/sms/receive/v2_2/interface", wsdlLocation = "WEB-INF/wsdl/MTNReceiveSMS/parlayx_sms_receive_interface_2_2Wrapper.wsdl")
public class MTNReceiveSMS {

    public java.util.List<org.csapi.schema.parlayx.sms.v2_2.SmsMessage> getReceivedSms(java.lang.String registrationIdentifier) throws ServiceException, PolicyException {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
