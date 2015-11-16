/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.*;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.*;

/**
 *
 * @author Quentin
 */
public class TravelGoodClientTest {
    
    public TravelGoodClientTest() {
    }
    
    @Test
    public void hello() throws DatatypeConfigurationException {
        GetInputType input = new GetInputType();
        HotelRequestType hotelRequest = new HotelRequestType();
        
        GetHotelInputType hotel = CreateGetHotelInputType("Milan");
        hotelRequest.getHotelsList().add(hotel);
        input.getHotelRequests().add(hotelRequest);
        
        GetOutputType output = getFlightsAndHotels(input);
        
        System.out.println("ouput: "+ output.getHotelsList().size());
        System.out.println("ouput 0: "+ output.getHotelsList().get(0).getHotelInformations().size());
        //System.out.println("ouput 1: "+ output.getHotelsList().get(1).getHotelInformations().size());
        String expected = "Milan Hotel"; 
        String result = output.getHotelsList().get(0).getHotelInformations().get(1).getHotel().getName();       
        assertEquals(expected, result);
    }
    
    @Test
    public void Hello2() throws DatatypeConfigurationException{
        System.out.println("Test starts");
        GetInputType input = new GetInputType();
        HotelRequestType hotelRequest = new HotelRequestType();
            
        GetHotelInputType hotel = CreateGetHotelInputType("Milan");
        GetHotelInputType hotel2 = CreateGetHotelInputType("London");
        GetHotelInputType hotel3 = CreateGetHotelInputType("Paris");
        hotelRequest.getHotelsList().add(hotel);
        hotelRequest.getHotelsList().add(hotel2);
        hotelRequest.getHotelsList().add(hotel3);
        input.getHotelRequests().add(hotelRequest); 
        
        // Make the request
        GetOutputType output = getFlightsAndHotels(input);
               
        String expected1 = "Milan Hotel";
        String expected2 = "London Hotel"; 
        System.out.println("GetHotelsList size: " + output.getHotelsList().size());
        System.out.println("GetHotelsInformations.get(0) size: " + output.getHotelsList().get(1).getHotelInformations().size());
        
        String result1 = output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName();
        System.out.println(result1);
         
        assertEquals(expected1, result1);  
              
        System.out.println(output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName());
        System.out.println(output.getHotelsList().get(2).getHotelInformations().get(0).getHotel().getName());
        System.out.println(output.getHotelsList().get(3).getHotelInformations().get(0).getHotel().getName());  
        System.out.println(output.getHotelsList().get(3).getHotelInformations().get(1).getHotel().getName());  
        System.out.println("GetHotelsInformations.get(1) size: " + output.getHotelsList().get(0).getHotelInformations().size());
        String result2 = output.getHotelsList().get(2).getHotelInformations().get(0).getHotel().getName();       
        assertEquals(expected2, result2); 
        
        int result3 = output.getHotelsList().get(3).getHotelInformations().size(); 
        assertEquals(2, result3); 
    }
    
    private GetHotelInputType CreateGetHotelInputType(String city) throws DatatypeConfigurationException{
        GetHotelInputType input = new GetHotelInputType();
        input.setCity(city);
        
        GregorianCalendar _arrivalDate = new GregorianCalendar(2016, 11, 04);
        DatatypeFactory df = DatatypeFactory.newInstance();
        XMLGregorianCalendar arrivalDate = df.newXMLGregorianCalendar(_arrivalDate);
        input.setArrivalDate(arrivalDate);
        
        GregorianCalendar _departureDate = new GregorianCalendar(2016, 11, 14);
        XMLGregorianCalendar departureDate = df.newXMLGregorianCalendar(_departureDate);      
        input.setDepartureDate(departureDate);
        
        return input;
    }
    
    private static GetOutputType getFlightsAndHotels(org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.GetInputType part1) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.getFlightsAndHotels(part1);
    }
}