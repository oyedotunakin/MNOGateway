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
import java.io.PrintStream;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/*
 * This simple SOAPHandler will output the contents of incoming
 * and outgoing messages.
 */
public class SOAPSendSMSHandler implements SOAPHandler<SOAPMessageContext> {

    // change this to redirect output if desired
    private static PrintStream out = System.out;

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext smc) {
        try {
            logToSystemOut(smc);
        } catch (SOAPException ex) {
            Logger.getLogger(SOAPSendSMSHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext smc) {
        try {
            logToSystemOut(smc);

        } catch (SOAPException ex) {
            Logger.getLogger(SOAPSendSMSHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    // nothing to clean up
    @Override
    public void close(MessageContext messageContext) {
    }

    /*
     * Check the MESSAGE_OUTBOUND_PROPERTY in the context
     * to see if this is an outgoing or incoming message.
     * Write a brief message to the print stream and
     * output the message. The writeTo() method can throw
     * SOAPException or IOException
     */
    private void logToSystemOut(SOAPMessageContext smc) throws SOAPException {
        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        SOAPMessage message = smc.getMessage();
        if (outboundProperty) {
            //set the headers here
            out.println("\nOutbound message:");

            SOAPEnvelope envelope = smc.getMessage().getSOAPPart().getEnvelope();
            SOAPHeader header;

            header = envelope.getHeader();
            if (header == null) {
                header = envelope.addHeader();
            }

            SOAPElement requestHdr
                    = header.addChildElement("RequestSOAPHeader", "v2", "http://www.huawei.com.cn/schema/common/v2_1");

            SOAPElement spId
                    = requestHdr.addChildElement("spId", "v2");
            spId.addTextNode("234323433");

            SOAPElement spPassword
                    = requestHdr.addChildElement("spPassword", "v2");
            spPassword.addTextNode("wrqrq3r3rfew234323433");

            SOAPElement bundleID
                    = requestHdr.addChildElement("bundleID", "v2");
            bundleID.addTextNode("2232");

            SOAPElement serviceId
                    = requestHdr.addChildElement("serviceId", "v2");
            serviceId.addTextNode("5552232");

            SOAPElement timeStamp
                    = requestHdr.addChildElement("timeStamp", "v2");
            timeStamp.addTextNode("2039382222");

            SOAPElement OA
                    = requestHdr.addChildElement("OA", "v2");
            OA.addTextNode("5353582222");

            SOAPElement fA
                    = requestHdr.addChildElement("FA", "v2");
            fA.addTextNode("fsdfda");

            SOAPElement lId
                    = requestHdr.addChildElement("linkid", "v2");
            lId.addTextNode("12345678901111");
            SOAPElement presentid
                    = requestHdr.addChildElement("presentid", "v2");
            presentid.addTextNode("12345678901111");
            message.saveChanges();

//            spId.addAttribute(new QName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
//            SOAPElement username
//                    = spId.addChildElement("Username", "wsse");
//            username.addTextNode("test");
//
//            SOAPElement password
//                    = spId.addChildElement("Password", "wsse");
//            password.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
//            password.addTextNode("test321");
        } else {
            out.println("\nInbound message:");
        }

        try {
            message.writeTo(out);
            out.println("");   // just to add a newline
        } catch (Exception e) {
            out.println("Exception in handler: " + e);
        }
    }
}
