
package billing;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import util.Glo;
import util.QuickUtils;


@WebServlet(name = "Charge", urlPatterns = {"/Charge"})
public class Charge extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String network = request.getParameter("Network");
            String amount = request.getParameter("Amount");
            String msisdn = request.getParameter("Msisdn");
            String subId = request.getParameter("ServiceId");
            network = network.toLowerCase();
            switch (network) {
                case "glo":                    
                    BigInteger refId = BigInteger.valueOf(Calendar.getInstance().getTimeInMillis() / 1000l);
//                    String subId = "43727";
                    String chargeGlo = GloHttp.chargeGlo(refId, subId, msisdn,amount);
                    out.print(QuickUtils.convert(chargeGlo));
//                   Glo.charge(amount, msisdn, String.valueOf(System.currentTimeMillis()));
//                   Glo.charge(amount, msisdn, String.valueOf(System.currentTimeMillis()));
                
            }
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
