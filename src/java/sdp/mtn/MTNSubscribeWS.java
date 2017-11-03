/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdp.mtn;

import javax.jws.WebService;

/**
 *
 * @author User-PC
 */
@WebService(serviceName = "SubscribeManageService", portName = "SubscribeManagePort", endpointInterface = "org.csapi.wsdl.parlayx.subscribe.manage.v1_0._interface.SubscribeManage", targetNamespace = "http://www.csapi.org/wsdl/parlayx/subscribe/manage/v1_0/interface", wsdlLocation = "WEB-INF/wsdl/MTNSubscribe/sag_subscribe_interface_1_0Wrapper.wsdl")
public class MTNSubscribeWS {

    public org.csapi.schema.parlayx.subscribe.manage.v1_0.local.SubscribeServiceResponse subscribeService(org.csapi.schema.parlayx.subscribe.manage.v1_0.local.SubscribeServiceRequest parameters) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.csapi.schema.parlayx.subscribe.manage.v1_0.local.UnSubscribeServiceResponse unSubscribeService(org.csapi.schema.parlayx.subscribe.manage.v1_0.local.UnSubscribeServiceRequest parameters) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.csapi.schema.parlayx.subscribe.manage.v1_0.local.SubscribeProductResponse subscribeProduct(org.csapi.schema.parlayx.subscribe.manage.v1_0.local.SubscribeProductRequest parameters) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.csapi.schema.parlayx.subscribe.manage.v1_0.local.UnSubscribeProductResponse unSubscribeProduct(org.csapi.schema.parlayx.subscribe.manage.v1_0.local.UnSubscribeProductRequest parameters) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
