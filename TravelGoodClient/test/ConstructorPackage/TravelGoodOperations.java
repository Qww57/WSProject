/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConstructorPackage;

import org.netbeans.j2ee.wsdl.niceview.java.niceview.CreditCardInfoType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.GetOutputType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.ItineraryListType;

/**
 *
 * @author Quentin
 */
public class TravelGoodOperations {
     public static ItineraryListType bookItinerary(java.lang.String part1, CreditCardInfoType part2) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.bookItinerary(part1, part2);
    }

    public static GetOutputType getFlightsAndHotels(org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.GetInputType part1, java.lang.String part2) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.getFlightsAndHotels(part1, part2);
    }

    public static ItineraryListType getItinerary(java.lang.String part1) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.getItinerary(part1);
    }

    public static ItineraryListType planFlightsAndHotels(org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.PlanInputType part1, java.lang.String part2) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.planFlightsAndHotels(part1, part2);
    }

    public static ItineraryListType cancelItinerary(java.lang.String part1) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.cancelItinerary(part1);
    }

    public static boolean cancelPlanning(java.lang.String part1) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.cancelPlanning(part1);
    }

    public static String createItinerary() {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.createItinerary();
    }
}
