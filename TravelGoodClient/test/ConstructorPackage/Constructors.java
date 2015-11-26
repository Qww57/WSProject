package ConstructorPackage;

import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.CreditCardInfoType;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.GetFlightsInputType;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.GetHotelInputType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.FlightRequestType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.GetInputType;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.HotelRequestType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quentin
 */
public class Constructors {
    public static CreditCardInfoType CreateCreditCard(String name, String number, int month, int year){
        CreditCardInfoType creditCard = new CreditCardInfoType();
        creditCard.setName(name);
        creditCard.setNumber(number);
        CreditCardInfoType.ExpirationDate date = new CreditCardInfoType.ExpirationDate();
        date.setMonth(month);
        date.setYear(year);
        creditCard.setExpirationDate(date);
        return creditCard;
    }
    
    public static GetInputType CreateGetInputType(String city) throws DatatypeConfigurationException{    
        GetInputType input = new GetInputType();
        
        // Adding one hotel to the request
        HotelRequestType hotelRequest = new HotelRequestType();
        GetHotelInputType hotel = CreateGetHotelInputType(city);
        hotelRequest.getHotelsList().add(hotel);
        input.getHotelRequests().add(hotelRequest);
        
        return input;
    }
    
    public static GetHotelInputType CreateGetHotelInputType(String place, XMLGregorianCalendar arrival, XMLGregorianCalendar deperture) throws DatatypeConfigurationException{
        GetHotelInputType output = new GetHotelInputType();
          
        output.setDepartureDate(arrival);
        output.setArrivalDate(deperture);
        output.setCity(place);
        
        return output;
    }
    
    public static GetFlightsInputType CreateGetFlightsInputType(XMLGregorianCalendar date, String start, String destination) throws DatatypeConfigurationException{
        GetFlightsInputType output = new GetFlightsInputType();
          
        output.setDate(date);
        output.setStart(start);
        output.setDestination(destination);
        
        return output;
    }
    
    public static void AddFlight(GetInputType request, GetFlightsInputType flight){
        if (request.getFlightRequests().isEmpty()){
            FlightRequestType flightRequest = new FlightRequestType();
            flightRequest.getFlightsList().add(flight);
            request.getFlightRequests().add(flightRequest);
        } else {
            request.getFlightRequests().get(0).getFlightsList().add(flight);
        }
    }
    
    public static void AddHotel(GetInputType request, GetHotelInputType hotel){
        if (request.getHotelRequests().isEmpty()){
            HotelRequestType hotelRequest = new HotelRequestType();
            hotelRequest.getHotelsList().add(hotel);
            request.getHotelRequests().add(hotelRequest);
        } else {
            request.getHotelRequests().get(0).getHotelsList().add(hotel);
        }
    }
    
    public static XMLGregorianCalendar CreateDate(int day, int month, int year) throws DatatypeConfigurationException{
        XMLGregorianCalendar date;
        
        GregorianCalendar gc = new GregorianCalendar(year, month, day);
        DatatypeFactory df = DatatypeFactory.newInstance();
        date = df.newXMLGregorianCalendar(gc);
        date.setDay(26);
        date.setMonth(10);
        date.setYear(2015);
        
        return date;
    }
       
    public static GetHotelInputType CreateGetHotelInputType(String city) throws DatatypeConfigurationException{
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
    
    public static GetFlightsInputType CreateGetFlightsInputType(String start, String destination, Integer day, Integer month, Integer year) throws DatatypeConfigurationException{
        GetFlightsInputType input = new GetFlightsInputType();
        input.setDestination(destination);
        input.setStart(start);
        
        DatatypeFactory df = DatatypeFactory.newInstance();
        GregorianCalendar _departureDate = new GregorianCalendar(2016, 11, 14);
        XMLGregorianCalendar departureDate = df.newXMLGregorianCalendar(_departureDate);
        departureDate.setDay(day);
        departureDate.setMonth(month);
        departureDate.setYear(year);
        input.setDate(departureDate);
        
        return input;
    } 
}
