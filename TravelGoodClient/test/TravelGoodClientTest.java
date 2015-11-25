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
import static org.junit.Assert.assertTrue;
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
    public void createEmptyItinerary(){
        System.out.println("Test Start");
        
        int myIntID = 15;
        ItineraryResponseType itineraryCreation = createItinerary(myIntID);
        
        System.out.println("Itinerary Created");
        Boolean receivedItineraryCreated = itineraryCreation.isItineraryCreated();
        int receivedItinID = itineraryCreation.getItineraryID();
        
        assertTrue(receivedItineraryCreated);
        assertEquals(myIntID, receivedItinID);     
    }
    
    //@Test 
    public void cancelEmptyItinerary(){
        System.out.println("Test Start");
        
        int myIntID = 19;
        ItineraryResponseType itineraryCreation = createItinerary(myIntID);
        
        System.out.println("Itinerary Created");
        Boolean receivedItineraryCreated = itineraryCreation.isItineraryCreated();
        int receivedItinID = itineraryCreation.getItineraryID();
        
        assertTrue(receivedItineraryCreated);
        assertEquals(myIntID, receivedItinID);  
        
        // TODO cancelling it
    }
    
    @Test
    public void getOneHotel() throws DatatypeConfigurationException{
        System.out.println("Test starts - GetOneHotel");
         
        //Create the itinerary 
        Integer myItinID = 1890;
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(myItinID, receivedItinID);
        System.out.println("Itinerary created with following ID: " + receivedItinID);
               
        //Get one hotel
        String city = "Milan";
        GetInputType input = CreateGetInputType(city);
        
        GetOutputType output = getFlightsAndHotels(input, myItinID);
        String resultCity = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city, resultCity);
        System.out.println("Getting an hotel from the city: " + resultCity);      
    }
    
    @Test
    public void getOneFlight() throws DatatypeConfigurationException{
        System.out.println("Test starts - GetOneFlight");
         
        //Create the itinerary 
        Integer myItinID = 1888;
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(myItinID, receivedItinID);
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Create the input  
        GetInputType input = new GetInputType();
        
        //Create one flight request
        XMLGregorianCalendar date = CreateDate(26, 11, 2015);
        GetFlightsInputType flight = CreateGetFlightsInputType(date, "Copenhagen", "London");
        AddFlight(input, flight);
       
        GetOutputType output = getFlightsAndHotels(input, myItinID);
        String resultStart = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();
        assertEquals("Copenhagen", resultStart);
        System.out.println("Getting a flight from the city: " + resultStart);      
    }
    
    @Test
    public void getOneHotelAndOneFlight() throws DatatypeConfigurationException{
        System.out.println("Test starts - GetOneFlight");
        
        //Create the itinerary 
        Integer myItinID = 1878;
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(myItinID, receivedItinID);
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Create the input  
        GetInputType input = new GetInputType();
        
        //Create one flight request
        XMLGregorianCalendar date = CreateDate(26, 11, 2015);
        GetFlightsInputType flight = CreateGetFlightsInputType(date, "Copenhagen", "London");
        AddFlight(input, flight);
        
        // Create one hotel request
        String city = "Milan";
        XMLGregorianCalendar date1 = CreateDate(26, 11, 2015);
        XMLGregorianCalendar date2 = CreateDate(29, 11, 2015);
        GetHotelInputType hotel = CreateGetHotelInputType(city, date1, date2);
        AddHotel(input, hotel);
        
        // Making the request
        GetOutputType output = getFlightsAndHotels(input, myItinID);
        
        // Checking the flight result
        String resultStart = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();
        assertEquals("Copenhagen", resultStart);
        System.out.println("Getting a flight from the city: " + resultStart);
        
        // Checking the hotel result
        String resultCity = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city, resultCity);
        System.out.println("Getting an hotel from the city: " + resultCity);    
    }
    
    @Test
    public void getThreeHotels() throws DatatypeConfigurationException{
        System.out.println("Test starts - getManyHotels");
        
        //Create the itinerary 
        Integer myItinID = 1410;
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(myItinID, receivedItinID);
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Creating the request      
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
        GetOutputType output = getFlightsAndHotels(input, myItinID);
               
        String expected1 = "Milan Hotel";
        String expected2 = "London Hotel"; 
        System.out.println("GetHotelsList size: " + output.getHotelsList().size());
        System.out.println("GetHotelsInformations.get() size: " + output.getHotelsList().get(1).getHotelInformations().size());
        
        String result1 = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName();
        System.out.println(result1);
         
        assertEquals(expected1, result1);  
              
        System.out.println(output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName());
        System.out.println(output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName());  
        System.out.println(output.getHotelsList().get(2).getHotelInformations().get(1).getHotel().getName());  
        
        String result2 = output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName();       
        assertEquals(expected2, result2); 
        
        int result3 = output.getHotelsList().get(2).getHotelInformations().size(); 
        assertEquals(2, result3);      
    }
    
    @Test
    public void getThreeFlights() throws DatatypeConfigurationException{
        System.out.println("Test starts");
        
        //Create the itinerary 
        Integer myItinID = 14555;
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(myItinID, receivedItinID);
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Creating the request
        GetInputType input = new GetInputType();
        FlightRequestType flightRequest = new FlightRequestType();
            
        GetFlightsInputType flight = CreateGetFlightsInputType("Copenhagen", "London", 26, 10, 2015);
        GetFlightsInputType flight2 = CreateGetFlightsInputType("London", "New York", 26,10,2015);
        GetFlightsInputType flight3 = CreateGetFlightsInputType("Copenhagen", "Kuala Lumpur", 26,2,2016);
        
        flightRequest.getFlightsList().add(flight);
        flightRequest.getFlightsList().add(flight2);
        flightRequest.getFlightsList().add(flight3);
        input.getFlightRequests().add(flightRequest); 

        // Make the request
        GetOutputType output = getFlightsAndHotels(input, myItinID);
        System.out.println("Request done");
          
        String expected1 = "Copenhagen";
        String expected2 = "London"; 
        String expected3 = "Copenhagen";
        
        System.out.println("getFlightsList size: " + output.getFlightsList().size());
        System.out.println("getFlightsList.get() size: " + output.getFlightsList().get(0).getFlightInformations().size());
        
        String result1 = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();
        System.out.println(result1);
         
        assertEquals(expected1, result1);  
             
        String result2 = output.getFlightsList().get(1).getFlightInformations().get(0).getFlight().getStart();     
        assertEquals(expected2, result2); 
        String result3 = output.getFlightsList().get(2).getFlightInformations().get(0).getFlight().getStart();     
        assertEquals(expected3, result3);
    }
    
    @Test 
    public void planOneHotel() throws DatatypeConfigurationException {
        //Create the itinerary 
        Integer myItinID = 7555;
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(myItinID, receivedItinID);
        System.out.println("Itinerary created with following ID: " + receivedItinID);
               
        //Get one hotel
        String city = "Milan";
        GetInputType input = CreateGetInputType(city);
        
        GetOutputType output = getFlightsAndHotels(input, myItinID);
        String resultCity = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city, resultCity);
        System.out.println("Getting an hotel from the city: " + resultCity); 
        
        String bookingNumber = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(bookingNumber);
        ItineraryListType planOutput = planFlightsAndHotels(plan, myItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status);
    }
    
    @Test 
    public void planOneHotelAndOneFlight() throws DatatypeConfigurationException {
        //Create the itinerary 
        Integer myItinID = 7520;
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(myItinID, receivedItinID);
        System.out.println("Itinerary created with following ID: " + receivedItinID);
               
        // Create the input  
        GetInputType input = new GetInputType();
        
        //Create one flight request
        XMLGregorianCalendar date = CreateDate(26, 11, 2015);
        GetFlightsInputType flight = CreateGetFlightsInputType(date, "Copenhagen", "London");
        AddFlight(input, flight);
        
        // Create one hotel request
        String city = "Milan";
        XMLGregorianCalendar date1 = CreateDate(26, 11, 2015);
        XMLGregorianCalendar date2 = CreateDate(29, 11, 2015);
        GetHotelInputType hotel = CreateGetHotelInputType(city, date1, date2);
        AddHotel(input, hotel);
        
        // Making the request
        GetOutputType output = getFlightsAndHotels(input, myItinID);
        
        // Checking the flight result
        String resultStart = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();
        assertEquals("Copenhagen", resultStart);
        
        // Checking the hotel result
        String resultCity = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city, resultCity);  
        
        String hotelBookingNumber = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        String flightBookingNumber = output.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(hotelBookingNumber);
        plan.getFlightsBookingNumber().add(flightBookingNumber);
        ItineraryListType planOutput = planFlightsAndHotels(plan, myItinID);
        
        // Getting the status as unconfirmed
        String hotelStatus = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Hotel status: " + hotelStatus);
        assertEquals("unconfirmed", hotelStatus);
        
        String flightStatus = planOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Flight status: " + flightStatus);
        assertEquals("unconfirmed", flightStatus);
    }
    
    @Test
    public void planManyFlights(){
        // TODO
    }
    
    @Test
    public void planManyHotels(){
        // TODO
    }
    
    @Test 
    public void bookOneHotel() throws DatatypeConfigurationException{
        //Create the itinerary 
        Integer myItinID = 77996;
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(myItinID, receivedItinID);
        System.out.println("Itinerary created with following ID: " + receivedItinID);
               
        //Get one hotel
        String city = "Milan";
        GetInputType input = CreateGetInputType(city);
        
        GetOutputType output = getFlightsAndHotels(input, myItinID);
        String resultCity = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city, resultCity);
        System.out.println("Getting an hotel from the city: " + resultCity); 
        
        String bookingNumber = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(bookingNumber);
        ItineraryListType planOutput = planFlightsAndHotels(plan, myItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status);
        
        // Booking the hotel
        CreditCardInfoType creditCard = new CreditCardInfoType(); 
        ItineraryListType bookOutput = bookItinerary(myItinID, creditCard); // Fails here because BPEL doesn't check that the list of flights is null and try to assigned the value
        status = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("confirmed", status);     
    }
    
    @Test 
    public void bookOneFlight(){
        // TODO
    }
    
    @Test 
    public void bookOneFlightAndOneHotel() throws DatatypeConfigurationException{
        //Create the itinerary 
        Integer myItinID = 7010;
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(myItinID, receivedItinID);
        System.out.println("Itinerary created with following ID: " + receivedItinID);
               
        // Create the input  
        GetInputType input = new GetInputType();
        
        //Create one flight request
        XMLGregorianCalendar date = CreateDate(26, 11, 2015);
        GetFlightsInputType flight = CreateGetFlightsInputType(date, "Copenhagen", "London");
        AddFlight(input, flight);
        
        // Create one hotel request
        String city = "Milan";
        XMLGregorianCalendar date1 = CreateDate(26, 11, 2015);
        XMLGregorianCalendar date2 = CreateDate(29, 11, 2015);
        GetHotelInputType hotel = CreateGetHotelInputType(city, date1, date2);
        AddHotel(input, hotel);
        
        // Making the request
        GetOutputType output = getFlightsAndHotels(input, myItinID);
        
        // Checking the flight result
        String resultStart = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();
        assertEquals("Copenhagen", resultStart);
        
        // Checking the hotel result
        String resultCity = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city, resultCity);  
        
        String hotelBookingNumber = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        String flightBookingNumber = output.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(hotelBookingNumber);
        plan.getFlightsBookingNumber().add(flightBookingNumber);
        ItineraryListType planOutput = planFlightsAndHotels(plan, myItinID);
        
        // Getting the status as unconfirmed
        String hotelStatus = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Hotel status: " + hotelStatus);
        assertEquals("unconfirmed", hotelStatus);
        
        String flightStatus = planOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Flight status: " + flightStatus);
        assertEquals("unconfirmed", flightStatus);
        
        // Booking the hotel
        CreditCardInfoType creditCard = CreateCreditCard("Bruhn Brigitte", "50408821", 2, 10);
        ItineraryListType bookOutput = bookItinerary(myItinID, creditCard); 
          
        hotelStatus = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Hotel status: " + hotelStatus);
        assertEquals("confirmed", hotelStatus);    
        
        flightStatus = bookOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Flight status: " + flightStatus);
        assertEquals("confirmed", flightStatus);
        System.out.println("booking passed!");
        
        //cancelling
        ItineraryListType cancelOutput = cancelItinerary(myItinID, creditCard);
        System.out.println("cancelling performed!");
        //hotel testing
        String cancelOutputHotelBookingNumber = cancelOutput.getHotelsItineraryInformation().get(0).getBookingNumber();
        System.out.println("cancelled hotel: " + cancelOutputHotelBookingNumber);
        String cancelOutputHotelStatus = cancelOutput.getHotelsItineraryInformation().get(0).getStatus();
        String expectedCancelOutputHotelStatus = "cancelled";
        System.out.println("cancelled hotel status: " + cancelOutputHotelStatus);
        assertEquals(cancelOutputHotelStatus, expectedCancelOutputHotelStatus);
        //flight testing
        String cancelOutputFlightBookingNumber = cancelOutput.getFlightsItineraryInformation().get(0).getBookingNumber();
        System.out.println("cancelled flight: " + cancelOutputFlightBookingNumber);
        String cancelOutputFlightStatus = cancelOutput.getFlightsItineraryInformation().get(0).getStatus();
        String expectedCancelOutputFlightStatus = "cancelled";
        System.out.println("cancelled flight status: " + cancelOutputFlightStatus);
        assertEquals(cancelOutputFlightStatus, expectedCancelOutputFlightStatus);
        
        
    }
      
    @Test 
    public void bookManyHotels() throws DatatypeConfigurationException{
        System.out.println("Test starts - bookManyHotels");
        
        //Create the itinerary 
        Integer myItinID = 12825535;
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(myItinID, receivedItinID);
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Creating the request      
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
        GetOutputType output = getFlightsAndHotels(input, myItinID);
               
        String expected1 = "Milan Hotel";
        String expected2 = "London Hotel"; 
        System.out.println("GetHotelsList size: " + output.getHotelsList().size());
        System.out.println("GetHotelsInformations.get() size: " + output.getHotelsList().get(1).getHotelInformations().size());
        
        String result1 = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName();
        System.out.println(result1);
         
        assertEquals(expected1, result1);  
              
        System.out.println(output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName());
        System.out.println(output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName());  
        System.out.println(output.getHotelsList().get(2).getHotelInformations().get(1).getHotel().getName());  
        
        String result2 = output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName();       
        assertEquals(expected2, result2); 
        
        int result3 = output.getHotelsList().get(2).getHotelInformations().size(); 
        assertEquals(2, result3);
        
        String bookingNumber = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        String bookingNumber2 = output.getHotelsList().get(1).getHotelInformations().get(0).getBookingNumber();

        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(bookingNumber);
        plan.getHotelsBookingNumber().add(bookingNumber2);
        ItineraryListType planOutput = planFlightsAndHotels(plan, myItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status);
        String status2 = planOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status2);
        
        // Booking the hotel
        CreditCardInfoType creditCard = new CreditCardInfoType(); 
        ItineraryListType bookOutput = bookItinerary(myItinID, creditCard); // Fails here because BPEL doesn't check that the list of flights is null and try to assigned the value
        status = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("confirmed", status);
        
        status2 = bookOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(1).getStatus());
        assertEquals("confirmed", status2);
    }
    
    @Test // One of the scenario: B 
    public void bookManyHotelwithOneFail() throws DatatypeConfigurationException{
        System.out.println("Test starts - bookManyHotels");
        
        //Create the itinerary 
        Integer myItinID = 128253;
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(myItinID, receivedItinID);
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Creating the request      
        GetInputType input = new GetInputType();
        HotelRequestType hotelRequest = new HotelRequestType();
            
        GetHotelInputType hotel = CreateGetHotelInputType("Milan");
        GetHotelInputType hotel2 = CreateGetHotelInputType("Madrid");
        GetHotelInputType hotel3 = CreateGetHotelInputType("Paris");
        hotelRequest.getHotelsList().add(hotel);
        hotelRequest.getHotelsList().add(hotel2);
        hotelRequest.getHotelsList().add(hotel3);
        input.getHotelRequests().add(hotelRequest); 
        
        // Make the request
        GetOutputType output = getFlightsAndHotels(input, myItinID);
               
        String expected1 = "Milan Hotel";
        String expected2 = "Hotel Madrid"; 
        System.out.println("GetHotelsList size: " + output.getHotelsList().size());
        System.out.println("GetHotelsInformations.get() size: " + output.getHotelsList().get(1).getHotelInformations().size());
        
        String result1 = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName();
        System.out.println(result1);
         
        assertEquals(expected1, result1);  
              
        System.out.println(output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName());
        System.out.println(output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName());  
        System.out.println(output.getHotelsList().get(2).getHotelInformations().get(1).getHotel().getName());  
        
        String result2 = output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName();       
        assertEquals(expected2, result2); 
        
        int result3 = output.getHotelsList().get(2).getHotelInformations().size(); 
        assertEquals(2, result3);
        
        String bookingNumber = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        String bookingNumber2 = output.getHotelsList().get(1).getHotelInformations().get(0).getBookingNumber();

        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(bookingNumber);
        plan.getHotelsBookingNumber().add(bookingNumber2);
        ItineraryListType planOutput = planFlightsAndHotels(plan, myItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status);
        String status2 = planOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status2);
        
        // Booking the hotel but the second one should fail
        CreditCardInfoType creditCard = new CreditCardInfoType(); 
        ItineraryListType bookOutput = bookItinerary(myItinID, creditCard); // Fails here because BPEL doesn't check that the list of flights is null and try to assigned the value
        status = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("cancelled", status);
        
        status2 = bookOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(1).getStatus());
        assertEquals("unconfirmed", status2);
    }
    
    @Test 
    public void cancelOneHotel() throws DatatypeConfigurationException{
        System.out.println("Test starts - bookManyHotels");
        
        //Create the itinerary 
        Integer myItinID = 145545682;
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(myItinID, receivedItinID);
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Creating the request      
        GetInputType input = new GetInputType();
        HotelRequestType hotelRequest = new HotelRequestType();
            
        GetHotelInputType hotel = CreateGetHotelInputType("Milan");
        hotelRequest.getHotelsList().add(hotel);
        input.getHotelRequests().add(hotelRequest); 
        
        // Make the request
        GetOutputType output = getFlightsAndHotels(input, myItinID);
               
        String expected1 = "Milan Hotel";
        System.out.println("GetHotelsList size: " + output.getHotelsList().size());
       
        String result1 = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName();
        System.out.println(result1);
         
        assertEquals(expected1, result1);  
        
        String bookingNumber1 = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
      
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(bookingNumber1);        
        ItineraryListType planOutput = planFlightsAndHotels(plan, myItinID);
               
        // Getting the status as unconfirmed
        String status = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status);
        
        // Booking the hotels
        CreditCardInfoType creditCard = CreateCreditCard("Bruhn Brigitte", "50408821", 2, 10);
        ItineraryListType bookOutput = bookItinerary(myItinID, creditCard); 
        
        status = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("confirmed", status);
        
        
        // Cancelling of the hotel
        ItineraryListType cancelOutput = cancelItinerary(myItinID, creditCard);            
            
        status = cancelOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + cancelOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("cancelled", status);
    }
    
    @Test // One of the scenario: P2
    public void cancelHotelFail() throws DatatypeConfigurationException{
        System.out.println("Test starts - bookManyHotels");
        
        //Create the itinerary 
        Integer myItinID = 145685582;
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(myItinID, receivedItinID);
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Creating the request      
        GetInputType input = new GetInputType();
        HotelRequestType hotelRequest = new HotelRequestType();
            
        GetHotelInputType hotel = CreateGetHotelInputType("Milan");
        GetHotelInputType hotel2 = CreateGetHotelInputType("Error city");
        GetHotelInputType hotel3 = CreateGetHotelInputType("Paris");
        hotelRequest.getHotelsList().add(hotel);
        hotelRequest.getHotelsList().add(hotel2);
        hotelRequest.getHotelsList().add(hotel3);
        input.getHotelRequests().add(hotelRequest); 
        
        // Make the request
        GetOutputType output = getFlightsAndHotels(input, myItinID);
               
        String expected1 = "Milan Hotel";
        String expected2 = "Error hotel"; 
        System.out.println("GetHotelsList size: " + output.getHotelsList().size());
        System.out.println("GetHotelsInformations.get() size: " + output.getHotelsList().get(1).getHotelInformations().size());
        
        String result1 = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName();
        System.out.println(result1);
         
        assertEquals(expected1, result1);  
              
        System.out.println(output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName());
        System.out.println(output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName());  
        System.out.println(output.getHotelsList().get(2).getHotelInformations().get(1).getHotel().getName());  
        
        String result2 = output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName();       
        assertEquals(expected2, result2); 
        
        int result3 = output.getHotelsList().get(2).getHotelInformations().size(); 
        assertEquals(2, result3);
        
        String bookingNumber1 = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        String bookingNumber2 = output.getHotelsList().get(1).getHotelInformations().get(0).getBookingNumber();
        String bookingNumber3 = output.getHotelsList().get(2).getHotelInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(bookingNumber1);        
        plan.getHotelsBookingNumber().add(bookingNumber2);
        plan.getHotelsBookingNumber().add(bookingNumber3);
        ItineraryListType planOutput = planFlightsAndHotels(plan, myItinID);
               
        // Getting the status as unconfirmed
        String status = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status);
        String status2 = planOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status2);
        String status3 = planOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status3);
        
        // Booking the hotels
        CreditCardInfoType creditCard = CreateCreditCard("Bruhn Brigitte", "50408821", 2, 10);
        ItineraryListType bookOutput = bookItinerary(myItinID, creditCard); // Fails here because BPEL doesn't check that the list of flights is null and try to assigned the value
        
        status = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("confirmed", status);
        
        status2 = bookOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(1).getStatus());
        assertEquals("confirmed", status2);
        
        status3 = bookOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(1).getStatus());
        assertEquals("confirmed", status3);
        
        System.out.println("booking done");
        
        // Cancelling of the hotel
        ItineraryListType cancelOutput = cancelItinerary(myItinID, creditCard);            
            
        status = cancelOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + cancelOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("cancelled", status);

        status2 = cancelOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + cancelOutput.getHotelsItineraryInformation().get(1).getStatus());
        //assertEquals("confirmed", status2);
            
        status3 = cancelOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + cancelOutput.getHotelsItineraryInformation().get(1).getStatus());
        assertEquals("cancelled", status3);
    }
      
    @Test 
    public void bookManyFlights(){
        // TODO
    }
    
    @Test
    public void gettingAndPlanningHotels() throws DatatypeConfigurationException {
        System.out.println("Test starts");
        //get one hotel
        GetInputType input = new GetInputType();
        HotelRequestType hotelRequest = new HotelRequestType();
        GetHotelInputType hotel = CreateGetHotelInputType("Milan");
        hotelRequest.getHotelsList().add(hotel);
        input.getHotelRequests().add(hotelRequest);
        
        //itinerary ID
        Integer myItinID = 1448;
        System.out.println("Ready to call operations");
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Boolean expectedItineraryCreated = true;
        Boolean receivedItineraryCreated = itineraryCreation.isItineraryCreated();
        Integer expectedItinID = myItinID;
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(expectedItinID, receivedItinID);
        System.out.println("Itinerary Created!!");
        GetOutputType output = getFlightsAndHotels(input, myItinID);
        //check results
        System.out.println("ouput: "+ output.getHotelsList().size());
        System.out.println("ouput 0: "+ output.getHotelsList().get(0).getHotelInformations().size());
        //System.out.println("ouput 1: "+ output.getHotelsList().get(1).getHotelInformations().size());
        String expected = "Milan Hotel"; 
        String result = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName();       
        assertEquals(expected, result);
        
        //stuff about planning
        String bookingNumber1 = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        System.out.println(bookingNumber1);
        PlanInputType inputBookingNumberList = new PlanInputType();
        inputBookingNumberList.getHotelsBookingNumber().add(bookingNumber1);
        System.out.println("planning operation to be called");
        ItineraryListType outputItinerary = planFlightsAndHotels(inputBookingNumberList, myItinID);
        System.out.println("operation performed correctly");
        String expectedBookingNumber = bookingNumber1;
        String expectedStatus = "unconfirmed";
        String resultBookingNumber = outputItinerary.getHotelsItineraryInformation().get(0).getBookingNumber();
        System.out.println("Resulting booking number: " + resultBookingNumber);
        String resultStatus = outputItinerary.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Resulting status: " + resultStatus);
        assertEquals(expectedBookingNumber, resultBookingNumber);
        assertEquals(expectedStatus, resultStatus);
        
        //getting more hotels and planning again
        System.out.println("Getting more hotels");
        GetInputType input2 = new GetInputType();
        HotelRequestType hotelRequest2 = new HotelRequestType();
        GetHotelInputType hotel2 = CreateGetHotelInputType("Paris");
        hotelRequest2.getHotelsList().add(hotel2);
        input2.getHotelRequests().add(hotelRequest2);
        GetOutputType output2 = getFlightsAndHotels(input2, myItinID);
        System.out.println("It did it again!!!");
        //check results
        System.out.println("ouput2: "+ output2.getHotelsList().size());
        System.out.println("ouput2 0: "+ output2.getHotelsList().get(0).getHotelInformations().size());
        String expected2_1 = "Hotel de France"; 
        String result2_1 = output2.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName();       
        assertEquals(expected2_1, result2_1);
        String expected2_2 = "NY Hotel"; 
        String result2_2 = output2.getHotelsList().get(0).getHotelInformations().get(1).getHotel().getName();       
        assertEquals(expected2_2, result2_2);
        
        //add 2nd hotel to plan
        String bookingNumber2 = output2.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        System.out.println(bookingNumber2);
        String bookingNumber3 = output2.getHotelsList().get(0).getHotelInformations().get(1).getBookingNumber();
        System.out.println(bookingNumber3);
        PlanInputType inputBookingNumberList2 = new PlanInputType();
        inputBookingNumberList2.getHotelsBookingNumber().add(bookingNumber2);
        inputBookingNumberList2.getHotelsBookingNumber().add(bookingNumber3);
        System.out.println("planning operation to be called");
        ItineraryListType outputItinerary2 = planFlightsAndHotels(inputBookingNumberList2, myItinID);
        System.out.println("operation performed correctly");
        //check itinerary length
        Integer resultItinerarySize = outputItinerary2.getHotelsItineraryInformation().size();
        System.out.println("Itinerary length: " + resultItinerarySize);
        Integer expectedItinerarySize = 3;
        assertEquals(resultItinerarySize, expectedItinerarySize);
        String expectedBookingNumber1 = bookingNumber1;
        String expectedStatus2 = "unconfirmed";
        String resultBookingNumber1 = outputItinerary2.getHotelsItineraryInformation().get(0).getBookingNumber();
        System.out.println("Resulting booking number 1: " + resultBookingNumber1);
        String resultStatus1 = outputItinerary2.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Resulting status 1: " + resultStatus1);
        assertEquals(expectedBookingNumber1, resultBookingNumber1);
        assertEquals(expectedStatus2, resultStatus1);
        String expectedBookingNumber2 = bookingNumber2;
        String resultBookingNumber2 = outputItinerary2.getHotelsItineraryInformation().get(1).getBookingNumber();
        System.out.println("Resulting booking number 2: " + resultBookingNumber2);
        String resultStatus2 = outputItinerary2.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Resulting status 2: " + resultStatus2);
        assertEquals(expectedBookingNumber2, resultBookingNumber2);
        assertEquals(expectedStatus2, resultStatus2);
        String expectedBookingNumber3 = bookingNumber3;
        String resultBookingNumber3 = outputItinerary2.getHotelsItineraryInformation().get(2).getBookingNumber();
        System.out.println("Resulting booking number 3: " + resultBookingNumber3);
        String resultStatus3 = outputItinerary2.getHotelsItineraryInformation().get(2).getStatus();
        System.out.println("Resulting status 3: " + resultStatus3);
        assertEquals(expectedBookingNumber3, resultBookingNumber3);
        assertEquals(expectedStatus2, resultStatus3);
        
        /*
        //cancel Itinerary
        System.out.println("Cancel itinerary starts");
        Boolean itineraryCancelledResult = cancelPlanning(myItinID);
        System.out.println("Itinerary cancelled for: " + myItinID);
        Boolean itineraryCancelledExpected = true;
        assertEquals(itineraryCancelledResult, itineraryCancelledExpected);
        */
        
        //book Itinerary
        CreditCardInfoType AnnesCreditCardInfo = new CreditCardInfoType();
        //ExpirationDate AnnesExpirationDate = new ExpirationDate();
        CreditCardInfoType.ExpirationDate AnnesExpirationDate = new CreditCardInfoType.ExpirationDate();
        AnnesExpirationDate.setMonth(5);
        AnnesExpirationDate.setYear(9);
        AnnesCreditCardInfo.setExpirationDate(AnnesExpirationDate);
        AnnesCreditCardInfo.setName("Anne Strandberg");
        AnnesCreditCardInfo.setNumber("50408816");
        //book itinerary
        System.out.println("Itinerary to be booked");
        ItineraryListType AnnesBookedItinerary = bookItinerary(myItinID, AnnesCreditCardInfo);
        System.out.println("Booking performed correctly");
        //check itinerary length
        Integer bookedItinerarySize = AnnesBookedItinerary.getHotelsItineraryInformation().size();
        System.out.println("Booked Itinerary length: " + bookedItinerarySize);
        Integer expectedBookedItinerarySize = 3;
        assertEquals(bookedItinerarySize, expectedBookedItinerarySize);
        expectedBookingNumber1 = bookingNumber1;
        String expectedStatus1 = "confirmed";
        resultBookingNumber1 = AnnesBookedItinerary.getHotelsItineraryInformation().get(0).getBookingNumber();
        System.out.println("After Booking: Resulting booking number 1: " + resultBookingNumber1);
        resultStatus1 = AnnesBookedItinerary.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("After Booking: Resulting status 1: " + resultStatus1);
        assertEquals(expectedBookingNumber1, resultBookingNumber1);
        assertEquals(expectedStatus1, resultStatus1);
        expectedBookingNumber2 = bookingNumber2;
        expectedStatus2 = "confirmed";
        resultBookingNumber2 = AnnesBookedItinerary.getHotelsItineraryInformation().get(1).getBookingNumber();
        System.out.println("After Booking: Resulting booking number 2: " + resultBookingNumber2);
        resultStatus2 = AnnesBookedItinerary.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("After Booking: Resulting status 2: " + resultStatus2);
        assertEquals(expectedBookingNumber2, resultBookingNumber2);
        assertEquals(expectedStatus2, resultStatus2);
        expectedBookingNumber3 = bookingNumber3;
        String expectedStatus3 = "confirmed";
        resultBookingNumber3 = AnnesBookedItinerary.getHotelsItineraryInformation().get(2).getBookingNumber();
        System.out.println("After Booking: Resulting booking number 3: " + resultBookingNumber3);
        resultStatus3 = AnnesBookedItinerary.getHotelsItineraryInformation().get(2).getStatus();
        System.out.println("After Booking: Resulting status 3: " + resultStatus3);
        assertEquals(expectedBookingNumber3, resultBookingNumber3);
        assertEquals(expectedStatus3, resultStatus3); 
        System.out.println("booking passed!");
        
        //cancelling stuff
        ItineraryListType cancelledItinerary = cancelItinerary(myItinID, AnnesCreditCardInfo);
        System.out.println("Cancelling performed correctly");
        //check itinerary length
        Integer cancelledItinerarySize = cancelledItinerary.getHotelsItineraryInformation().size();
        System.out.println("Cancelled Itinerary length: " + cancelledItinerarySize);
        Integer expectedCancelledItinerarySize = 3;
        assertEquals(cancelledItinerarySize, expectedCancelledItinerarySize);
        expectedBookingNumber1 = bookingNumber1;
        expectedStatus1 = "cancelled";
        resultBookingNumber1 = cancelledItinerary.getHotelsItineraryInformation().get(0).getBookingNumber();
        System.out.println("After Cancelling: Resulting booking number 1: " + resultBookingNumber1);
        resultStatus1 = cancelledItinerary.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("After Cancelling: Resulting status 1: " + resultStatus1);
        assertEquals(expectedBookingNumber1, resultBookingNumber1);
        assertEquals(expectedStatus1, resultStatus1);
        expectedBookingNumber2 = bookingNumber2;
        expectedStatus2 = "cancelled";
        resultBookingNumber2 = cancelledItinerary.getHotelsItineraryInformation().get(1).getBookingNumber();
        System.out.println("After Cancelling: Resulting booking number 2: " + resultBookingNumber2);
        resultStatus2 = cancelledItinerary.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("After Cancelling: Resulting status 2: " + resultStatus2);
        assertEquals(expectedBookingNumber2, resultBookingNumber2);
        assertEquals(expectedStatus2, resultStatus2);
        expectedBookingNumber3 = bookingNumber3;
        expectedStatus3 = "cancelled";
        resultBookingNumber3 = cancelledItinerary.getHotelsItineraryInformation().get(2).getBookingNumber();
        System.out.println("After Cancelling: Resulting booking number 3: " + resultBookingNumber3);
        resultStatus3 = cancelledItinerary.getHotelsItineraryInformation().get(2).getStatus();
        System.out.println("After Cancelling: Resulting status 3: " + resultStatus3);
        assertEquals(expectedBookingNumber3, resultBookingNumber3);
        assertEquals(expectedStatus3, resultStatus3); 
        System.out.println("cancelling finished!");
    }
    
    //@Test
    public void flightsAndHotels() throws DatatypeConfigurationException{
        
        System.out.println("Test starts");
        
        GetInputType inputData = new GetInputType();
        //flights
        FlightRequestType flightRequest = new FlightRequestType();        
        GetFlightsInputType flight = CreateGetFlightsInputType("Copenhagen", "London", 26, 10, 2015);
        GetFlightsInputType flight2 = CreateGetFlightsInputType("London", "New York", 26,10,2015);
        GetFlightsInputType flight3 = CreateGetFlightsInputType("Copenhagen", "Kuala Lumpur", 26,2,2016);
        flightRequest.getFlightsList().add(flight);
        flightRequest.getFlightsList().add(flight2);
        flightRequest.getFlightsList().add(flight3);
        inputData.getFlightRequests().add(flightRequest);
        //hotels
        HotelRequestType hotelRequest = new HotelRequestType();    
        GetHotelInputType hotel1 = CreateGetHotelInputType("Madrid");
        GetHotelInputType hotel2 = CreateGetHotelInputType("Paris");
        hotelRequest.getHotelsList().add(hotel1);
        hotelRequest.getHotelsList().add(hotel2);
        inputData.getHotelRequests().add(hotelRequest); 
        
        Integer myItinID = 1445;
        
        System.out.println("Inputs created");
        // Make the request
        GetOutputType output = getFlightsAndHotels(inputData, myItinID);
        System.out.println("Request done");
        
        System.out.println("output.getFlightsList() size: " + output.getFlightsList().size());
        System.out.println("output.getHotelsList() size: " + output.getHotelsList().size());
        
        System.out.println("Checking Flights");
        String expectedFlight1 = "Copenhagen";
        String expectedFlight2 = "London"; 
        String expectedFlight3 = "Copenhagen";
        String resultFlight1 = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();
        System.out.println("1st flight start: " + resultFlight1);
        String resultFlight2 = output.getFlightsList().get(1).getFlightInformations().get(0).getFlight().getStart();
        String resultFlight3 = output.getFlightsList().get(2).getFlightInformations().get(0).getFlight().getStart(); 
        assertEquals(expectedFlight1, resultFlight1); 
        assertEquals(expectedFlight2, resultFlight2);
        assertEquals(expectedFlight3, resultFlight3);
        
        System.out.println("Checking Hotels");
        String expected1 = "Hotel Madrid"; 
        System.out.println("GetHotelsList size: " + output.getHotelsList().size());
        System.out.println("GetHotelsInformations.getHotelsList().get(0).getHotelInformations() size: " + output.getHotelsList().get(0).getHotelInformations().size());
        System.out.println("2nd city 1st Hotel name: " + output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName());
        System.out.println("2nd city 2nd Hotel name: " + output.getHotelsList().get(1).getHotelInformations().get(1).getHotel().getName());
        
        String result1 = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName();
        System.out.println("1st Hotel name: " + result1);
        assertEquals(expected1, result1); 
        int result2 = output.getHotelsList().get(1).getHotelInformations().size(); 
        System.out.println("2nd city hotel amount: " + result2);
        assertEquals(2, result2);
        
    }
    
    private CreditCardInfoType CreateCreditCard(String name, String number, int month, int year){
        CreditCardInfoType creditCard = new CreditCardInfoType();
        creditCard.setName(name);
        creditCard.setNumber(number);
        CreditCardInfoType.ExpirationDate date = new CreditCardInfoType.ExpirationDate();
        date.setMonth(month);
        date.setYear(year);
        creditCard.setExpirationDate(date);
        return creditCard;
    }
    
    private GetInputType CreateGetInputType(String city) throws DatatypeConfigurationException{    
        GetInputType input = new GetInputType();
        
        // Adding one hotel to the request
        HotelRequestType hotelRequest = new HotelRequestType();
        GetHotelInputType hotel = CreateGetHotelInputType(city);
        hotelRequest.getHotelsList().add(hotel);
        input.getHotelRequests().add(hotelRequest);
        
        return input;
    }
    
    private GetHotelInputType CreateGetHotelInputType(String place, XMLGregorianCalendar arrival, XMLGregorianCalendar deperture) throws DatatypeConfigurationException{
        GetHotelInputType output = new GetHotelInputType();
          
        output.setDepartureDate(arrival);
        output.setArrivalDate(deperture);
        output.setCity(place);
        
        return output;
    }
    
    private GetFlightsInputType CreateGetFlightsInputType(XMLGregorianCalendar date, String start, String destination) throws DatatypeConfigurationException{
        GetFlightsInputType output = new GetFlightsInputType();
          
        output.setDate(date);
        output.setStart(start);
        output.setDestination(destination);
        
        return output;
    }
    
    private void AddFlight(GetInputType request, GetFlightsInputType flight){
        if (request.getFlightRequests().isEmpty()){
            FlightRequestType flightRequest = new FlightRequestType();
            flightRequest.getFlightsList().add(flight);
            request.getFlightRequests().add(flightRequest);
        } else {
            request.getFlightRequests().get(0).getFlightsList().add(flight);
        }
    }
    
    private void AddHotel(GetInputType request, GetHotelInputType hotel){
        if (request.getHotelRequests().isEmpty()){
            HotelRequestType hotelRequest = new HotelRequestType();
            hotelRequest.getHotelsList().add(hotel);
            request.getHotelRequests().add(hotelRequest);
        } else {
            request.getHotelRequests().get(0).getHotelsList().add(hotel);
        }
    }
    
    private XMLGregorianCalendar CreateDate(int day, int month, int year) throws DatatypeConfigurationException{
        XMLGregorianCalendar date;
        
        GregorianCalendar gc = new GregorianCalendar(year, month, day);
        DatatypeFactory df = DatatypeFactory.newInstance();
        date = df.newXMLGregorianCalendar(gc);
        date.setDay(26);
        date.setMonth(10);
        date.setYear(2015);
        
        return date;
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
    
    private GetFlightsInputType CreateGetFlightsInputType(String start, String destination, Integer day, Integer month, Integer year) throws DatatypeConfigurationException{
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

    private static GetOutputType getFlightsAndHotels(GetInputType part1, int part2) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.getFlightsAndHotels(part1, part2);
    }

    private static ItineraryResponseType createItinerary(int part1) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.createItinerary(part1);
    }

    private static ItineraryListType planFlightsAndHotels(PlanInputType part1, int part2) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.planFlightsAndHotels(part1, part2);
    }

    private static ItineraryListType bookItinerary(int part1, CreditCardInfoType part2) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.bookItinerary(part1, part2);
    }

    private static boolean cancelPlanning(int part1) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.cancelPlanning(part1);
    }

    private static ItineraryListType cancelItinerary(int part1, org.netbeans.j2ee.wsdl.niceview.java.niceview.CreditCardInfoType part2) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.cancelItinerary(part1, part2);
    }




}
