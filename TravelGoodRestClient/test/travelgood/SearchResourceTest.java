/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package travelgood;

import java.util.List;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import travelgood.objects.Itinerary;
import travelgood.representations.*;

/**
 *
 * @author Dani Sanz
 */
public class SearchResourceTest {
    
    @Test
    public void searchOneHotelandOneFLight()  throws DatatypeConfigurationException{
        //input stuff for searching flights and hotels
        SearchInputRepresentation mySearch = new SearchInputRepresentation();
        SearchInputRepresentation.SearchHotelInputRepresentation myHotel = new SearchInputRepresentation.SearchHotelInputRepresentation();
        myHotel.setArrivalDate(CreateDate(26, 10, 2015));
        myHotel.setDepartureDate(CreateDate(29, 10, 2015));
        myHotel.setCity("Milan");
        mySearch.hotelsList.add(myHotel);
        SearchInputRepresentation.SearchFlightInputRepresentation myFlight = new SearchInputRepresentation.SearchFlightInputRepresentation();
        myFlight.setDate(CreateDate(26, 10, 2015));
        myFlight.setStart("Copenhagen");
        myFlight.setDestination("London");
        mySearch.flightsList.add(myFlight);
        //build client and make post request
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target("http://localhost:8080/ws/webresources/search");
        Response searchResult = resource.request().post(Entity.entity(mySearch, MediaType.APPLICATION_XML), Response.class);
        SearchOutputRepresentation searchResultEntity = searchResult.readEntity(SearchOutputRepresentation.class);
        //hotel result
        System.out.println("hotel name: " + searchResultEntity.hotelsList.get(0).gethotel().getName());
        System.out.println("hotel price: " + searchResultEntity.hotelsList.get(0).getPrice());
        System.out.println("hotel booking number: " + searchResultEntity.hotelsList.get(0).getBookingNumber());
        //flight result
        System.out.println("flight booking number: " + searchResultEntity.flightsList.get(0).getBookingNumber());
        
    }
    
    public static XMLGregorianCalendar CreateDate(int day, int month, int year) throws DatatypeConfigurationException{
        XMLGregorianCalendar date;
        
        GregorianCalendar gc = new GregorianCalendar(year, month, day);
        DatatypeFactory df = DatatypeFactory.newInstance();
        date = df.newXMLGregorianCalendar(gc);
        date.setDay(day);
        date.setMonth(month);
        date.setYear(year);
        
        return date;
    }
      
    
}
