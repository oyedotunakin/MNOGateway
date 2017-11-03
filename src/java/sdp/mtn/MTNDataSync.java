/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdp.mtn;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import util.EndPoints;

/**
 *
 * @author User-PC
 */
@WebService(serviceName = "DataSyncService", portName = "DataSyncPort", endpointInterface = "org.csapi.wsdl.parlayx.data.sync.v1_0._interface.DataSync", targetNamespace = "http://www.csapi.org/wsdl/parlayx/data/sync/v1_0/interface", wsdlLocation = "WEB-INF/wsdl/MTNDataSync/sag_data_sync_interface_1_0Wrapper.wsdl")
public class MTNDataSync {

    public int syncSubscriptionData(java.lang.String msisdn, java.lang.String serviceId, java.lang.String productId, int updateType, org.csapi.schema.parlayx.data.v1_0.ProductDetail productDetail) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int changeMSISDN(java.lang.String msisdn, java.lang.String newMSISDN, java.lang.String timeStamp) {
        String r;
        org.json.simple.JSONObject jo = new org.json.simple.JSONObject();

        jo.put("Msisdn", msisdn);
        jo.put("NewMsisdn", newMSISDN);
        jo.put("TimeStamp", timeStamp);
//        jo.put("ProductId", productID);
//        jo.put("ServiceId", serviceID);
//        jo.put("UpdateType", updateType);
//        jo.put("EffectiveTime", effectiveTime);
//        jo.put("StartTime", starttime);
//        jo.put("ExpiryTime", expiryTime);
//        jo.put("UpdateDescription", updateDesc);
//        jo.put("UpdateTime", updateTime);
        HashMap h = new HashMap();
        h.put("Data", jo.toJSONString());
        try {
            r = util.Util.postTrxn(h, EndPoints.mtnApp, 1);
        } catch (IOException ex) {
            Logger.getLogger(MTNDataSync.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }

    public void syncOrderRelation(org.csapi.schema.parlayx.data.v1_0.UserID userID, java.lang.String spID, java.lang.String productID, java.lang.String serviceID, java.lang.String serviceList, java.lang.String starttime, int updateType, java.lang.String updateTime, java.lang.String updateDesc, java.lang.String effectiveTime, java.lang.String expiryTime, javax.xml.ws.Holder<org.csapi.schema.parlayx.data.v1_0.NamedParameterList> extensionInfo, javax.xml.ws.Holder<Integer> result, javax.xml.ws.Holder<java.lang.String> resultDescription) {
        String r;
        org.json.simple.JSONObject jo = new org.json.simple.JSONObject();

        jo.put("UserId", userID.getID());
        jo.put("UserType", userID.getType());
        jo.put("ServiceProviderId", spID);
        jo.put("ProductId", productID);
        jo.put("ServiceId", serviceID);
        jo.put("UpdateType", updateType);
        jo.put("EffectiveTime", effectiveTime);
        jo.put("StartTime", starttime);
        jo.put("ExpiryTime", expiryTime);
        jo.put("ServiceList", serviceList);
        jo.put("UpdateDescription", updateDesc);

        HashMap h = new HashMap();

        try {
            //save in db cascade exception
            DbUtil.saveDataSyncPayload(jo.toJSONString());
            boolean eligibleForReward = DbUtil.confirmEligibility(userID);
            if (eligibleForReward) {
                String vendAirtime = util.Util.vendAirtime("mtn", userID.getID().trim(), 100);
                System.out.println("vend result "+userID.getID()+"/"+vendAirtime);
                DbUtil.saveReward(userID, "mtn", 100);
            }
            jo.put("UpdateTime", updateTime);
            h.put("Data", jo.toJSONString());
            r = util.Util.postTrxn(h, EndPoints.mtnApp, 1);
        } catch (IOException ex) {
            Logger.getLogger(MTNDataSync.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("Duplicate record " + jo.toJSONString());
            Logger.getLogger(MTNDataSync.class.getName()).log(Level.SEVERE, null, ex);
        }
        resultDescription.value = "Successful";
        result.value = 5;
    }

    public void syncMSISDNChange(java.lang.String msisdn, java.lang.String newMSISDN, javax.xml.ws.Holder<org.csapi.schema.parlayx.data.v1_0.NamedParameterList> extensionInfo, javax.xml.ws.Holder<Integer> result, javax.xml.ws.Holder<java.lang.String> resultDescription) {
        //TODO implement this method
        String r;
        org.json.simple.JSONObject jo = new org.json.simple.JSONObject();

        jo.put("Msisdn", msisdn);
        jo.put("NewMsisdn", newMSISDN);
//        jo.put("ServiceProviderId", spID);
//        jo.put("ProductId", productID);
//        jo.put("ServiceId", serviceID);
//        jo.put("UpdateType", updateType);
//        jo.put("EffectiveTime", effectiveTime);
//        jo.put("StartTime", starttime);
//        jo.put("ExpiryTime", expiryTime);
//        jo.put("UpdateDescription", updateDesc);
//        jo.put("UpdateTime", updateTime);
        HashMap h = new HashMap();
        h.put("Data", jo.toJSONString());
        try {
            r = util.Util.postTrxn(h, EndPoints.mtnApp, 1);
        } catch (IOException ex) {
            Logger.getLogger(MTNDataSync.class.getName()).log(Level.SEVERE, null, ex);
        }
        resultDescription.value = "Successful";
        result.value = 5;
    }

    public void syncSubscriptionActive(org.csapi.schema.parlayx.data.v1_0.UserID userID, java.lang.String spID, java.lang.String productID, java.lang.String serviceID, javax.xml.ws.Holder<org.csapi.schema.parlayx.data.v1_0.NamedParameterList> extensionInfo, javax.xml.ws.Holder<Integer> result, javax.xml.ws.Holder<java.lang.String> resultDescription) {
        String r;
        org.json.simple.JSONObject jo = new org.json.simple.JSONObject();
        jo.put("UserId", userID.getID());
        jo.put("UserType", userID.getType());
        jo.put("ServiceProviderId", spID);
        jo.put("ProductId", productID);
        jo.put("ServiceId", serviceID);
        HashMap h = new HashMap();
        h.put("Data", jo.toJSONString());
        try {
            r = util.Util.postTrxn(h, EndPoints.mtnApp, 1);
        } catch (IOException ex) {
            Logger.getLogger(MTNDataSync.class.getName()).log(Level.SEVERE, null, ex);
        }
        resultDescription.value = "Successful";
        result.value = 5;
    }

    private String pushToApp(org.json.simple.JSONObject jo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
