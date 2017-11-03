/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sdp.mtn.PreparedSoap;
import util.Util;

/**
 *
 * @author akin
 */
@WebServlet(name = "SendSMS", urlPatterns = {"/SendSMS"})
public class SendSMS extends HttpServlet {

    final static String kannelBase = "http://localhost:13013/cgi-bin/sendsms?username=tester&password=foobar";
    Util u;

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
            /* TODO output your page here. You may use following sample code. */
            String network = request.getParameter("Network");
            String text, phone, header,serviceId;

            text = request.getParameter("Text");
            phone = request.getParameter("Msisdn");
            header = request.getParameter("Header");
            HashMap msgMap = new HashMap();
            msgMap.put("to", phone);
            msgMap.put("text", text);
            msgMap.put("from", header);
            System.out.println("Received " + phone + "/" + header + "/" + text);

            if ("MTN".equalsIgnoreCase(network)) {
                serviceId = request.getParameter("ServiceId");
                msgMap.put("serviceId", serviceId);
                out.print(PreparedSoap.postSendSMS(msgMap));
            } else {
                out.print(Util.postTrxn(msgMap, kannelBase, 0));
            }

        }
    }

    @Override
    public void init() {
//        try {
//            u = new Util();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            Logger.getLogger(SendSMS.class.getName()).log(Level.SEVERE, null, ex);
//        }
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
