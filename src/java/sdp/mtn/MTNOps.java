/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdp.mtn;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sdp.sms.mtn.out.SendSmsService;

/**
 *
 * @author User-PC
 */
@WebServlet(name = "MTNOps", urlPatterns = {"/MTNOps"})
public class MTNOps extends HttpServlet {

//    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/MNOGateway/SendSmsService.wsdl")
    private static final String MTN_ENDPOINT = "http://41.206.4.219:8310/SendSmsService/services/SendSms";
//    private static final String MTN_ENDPOINT = "http://41.206.4.162:8310/SendSmsService/services/SendSms";
    private SendSmsService service;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            java.util.List<java.lang.String> addresses;
            String phone = request.getParameter("Phone");
            java.lang.String senderName = request.getParameter("SenderName");
            sdp.sms.mtn.out.ChargingInformation charging = null;
            java.lang.String message = request.getParameter("Text");
            sdp.sms.mtn.out.SimpleReference receiptRequest = null;
            java.lang.String encode = "UTF-8";
            java.lang.Integer sourceport = 0;
            java.lang.Integer destinationport = 0;
            java.lang.Integer esmClass = 0;
            java.lang.Integer dataCoding = 0;
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            addresses = new LinkedList<>();
            addresses.add("tel:" + phone);

//            URL u = new URL(MTN_ENDPOINT);
//            service = new SendSmsService(u);
//            SendSms sp = service.getSendSmsPort();
            //==============Adding header info==================
//            Map<String, Object> req_ctx = ((BindingProvider) sp).getRequestContext();
//            req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MTN_ENDPOINT);
//
//            Map<String, List<String>> headers = new HashMap<>();
//            headers.put("spId", Collections.singletonList(""));
//            headers.put("spPassword", Collections.singletonList("password"));
////            headers.put("bundleID", Collections.singletonList(""));
//            headers.put("serviceId", Collections.singletonList("234012000011232"));
//            headers.put("timestamp", Collections.singletonList(timeStamp));
//            headers.put("QA", Collections.singletonList(phone));
//            headers.put("FA", Collections.singletonList(phone));
//            headers.put("linkid", Collections.singletonList(""));
//            headers.put("presentid", Collections.singletonList(""));
//            req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);

//            WSBindingProvider bp = (WSBindingProvider) sp;
//            
//            QName qname = new QName("http://www.huawei.com.cn/schema/common/v2_1", "spId");
//            
//            Header hd = Headers.create(qname,"value");
//            Header hd2 = Headers.create(qname,"va2");
//            List<Header> l = new LinkedList<>();
//            l.add(hd);
//            l.add(hd2);
//                    
//            bp.setOutboundHeaders(l);

            //=============End adding header info================
            try {
//                org.csapi.schema.parlayx.sms.send.v2_2.local.ObjectFactory mFactory = new org.csapi.schema.parlayx.sms.send.v2_2.local.ObjectFactory();
////                charging = mFactory.createChargingInformation();
////                receiptRequest = mFactory.createSimpleReference();
//
////                Binding binding = ((BindingProvider) sp).getBinding();
//
////                List<Handler> handlerList = binding.getHandlerChain();
////                handlerList.add(new SOAPSendSMSHandler());
////                binding.setHandlerChain(handlerList);
//
////                String res = sp.sendSms(addresses, senderName, charging, message, receiptRequest, encode, sourceport, destinationport, esmClass, dataCoding);
//                //============= Direct push
////                SendSms_Type st = mFactory.createSendSms_Type();
//                org.csapi.schema.parlayx.sms.send.v2_2.local.SendSms ssms = mFactory.createSendSms();
//                String res = util.QuickUtils.postSoap(ssms);
//                //=============
//                System.out.println(res);
                PreparedSoap.postSendSMS(null);

            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(MTNOps.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
