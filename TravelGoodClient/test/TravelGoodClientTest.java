/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static ConstructorPackage.Constructors.*;
import static ConstructorPackage.TravelGoodOperations.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.*;
import org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.*;

/**
 * Class composed of unit tests in order to help and test the BPEL implementation. 
 * 
 * @author Quentin, Daniel Sanz, Ali
 */
public class TravelGoodClientTest {
    
     public TravelGoodClientTest() {
        }
   

    /* Create Itinerery */
    
    @Test 
    public void createEmptyItinerary(){
        System.out.println("Test Start");
        
        String receivedItinID = createItinerary();
        
        System.out.println("Itinerary Created - " + receivedItinID );   
        assertTrue(true);
    }
    
    /* Get hotels and flights */
    
    @Test
    public void getOneHotel() throws DatatypeConfigurationException{
        System.out.println("Test starts - GetOneHotel");
         
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
               
        //Get one hotel
        String city = "Milan";
        GetInputType input = CreateGetInputType(city);
        
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        String resultCity = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city, resultCity);
        System.out.println("Getting an hotel from the city: " + resultCity);      
    }
    
    @Test
    public void getOneFlight() throws DatatypeConfigurationException{
        System.out.println("Test starts - GetOneFlight");
         
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Create the input  
        GetInputType input = new GetInputType();
        
        //Create one flight request
        XMLGregorianCalendar date = CreateDate(26, 10, 2015);
        GetFlightsInputType flight = CreateGetFlightsInputType(date, "Copenhagen", "London");
        AddFlight(input, flight);
       System.out.println("we added");
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        String resultStart = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();
        assertEquals("Copenhagen", resultStart);
        System.out.println("Getting a flight from the city: " + resultStart);      
    }
    
    @Test
    public void getOneHotelAndOneFlight() throws DatatypeConfigurationException{
        System.out.println("Test starts - GetOneFlight");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Create the input  
        GetInputType input = new GetInputType();
        
        //Create one flight request
        XMLGregorianCalendar date = CreateDate(26, 10, 2015);
        GetFlightsInputType flight = CreateGetFlightsInputType(date, "Copenhagen", "London");
        AddFlight(input, flight);
        
        // Create one hotel request
        String city = "Milan";
        XMLGregorianCalendar date1 = CreateDate(26, 11, 2015);
        XMLGregorianCalendar date2 = CreateDate(29, 11, 2015);
        GetHotelInputType hotel = CreateGetHotelInputType(city, date1, date2);
        AddHotel(input, hotel);
        
        // Making the request
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        
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
        String receivedItinID = createItinerary();
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
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
               
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
        String receivedItinID = createItinerary();
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
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
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
    
    /* Plan hotels and flights */
    
    @Test 
    public void planOneHotel() throws DatatypeConfigurationException {
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
               
        //Get one hotel
        String city = "Milan";
        GetInputType input = CreateGetInputType(city);
        
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        String resultCity = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city, resultCity);
        System.out.println("Getting an hotel from the city: " + resultCity); 
        
        String bookingNumber = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(bookingNumber);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status);
    }
    
    @Test
    public void planOneFlight() throws DatatypeConfigurationException{
        System.out.println("Test starts");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Creating the request
        GetInputType input = new GetInputType();
        FlightRequestType flightRequest = new FlightRequestType();         
        GetFlightsInputType flight = CreateGetFlightsInputType("Copenhagen", "London", 26, 10, 2015);       
        flightRequest.getFlightsList().add(flight);
        input.getFlightRequests().add(flightRequest); 

        // Make the request
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);        
        String expected1 = "Copenhagen";     
        String result1 = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();      
        assertEquals(expected1, result1);                     
        String bookingNumber = output.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getFlightsBookingNumber().add(bookingNumber);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status);
        assertEquals("unconfirmed", status);    
    }
    
    @Test 
    public void planOneHotelAndOneFlight() throws DatatypeConfigurationException {
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
               
        // Create the input  
        GetInputType input = new GetInputType();
        
        //Create one flight request
        XMLGregorianCalendar date = CreateDate(26, 10, 2015);
        GetFlightsInputType flight = CreateGetFlightsInputType(date, "Copenhagen", "London");
        AddFlight(input, flight);
        
        // Create one hotel request
        String city = "Milan";
        XMLGregorianCalendar date1 = CreateDate(26, 11, 2015);
        XMLGregorianCalendar date2 = CreateDate(29, 11, 2015);
        GetHotelInputType hotel = CreateGetHotelInputType(city, date1, date2);
        AddHotel(input, hotel);
        
        // Making the request
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        
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
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String hotelStatus = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Hotel status: " + hotelStatus);
        assertEquals("unconfirmed", hotelStatus);
        
        String flightStatus = planOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Flight status: " + flightStatus);
        assertEquals("unconfirmed", flightStatus);
    }
    
    @Test 
    public void planManyFlights() throws DatatypeConfigurationException{
        System.out.println("Test starts");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
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
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        System.out.println("Request done");
          
        String expected1 = "Copenhagen";
        String expected2 = "London"; 
        String expected3 = "Copenhagen";       
        
        String result1 = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();      
        assertEquals(expected1, result1);              
        String result2 = output.getFlightsList().get(1).getFlightInformations().get(0).getFlight().getStart();     
        assertEquals(expected2, result2); 
        String result3 = output.getFlightsList().get(2).getFlightInformations().get(0).getFlight().getStart();     
        assertEquals(expected3, result3);
        
        String bookingNumber = output.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        String bookingNumber1 = output.getFlightsList().get(1).getFlightInformations().get(0).getBookingNumber();
        String bookingNumber2 = output.getFlightsList().get(2).getFlightInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getFlightsBookingNumber().add(bookingNumber);
        plan.getFlightsBookingNumber().add(bookingNumber1);
        plan.getFlightsBookingNumber().add(bookingNumber2);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status);
        assertEquals("unconfirmed", status);
        
        String status1 = planOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + status1);
        assertEquals("unconfirmed", status1);
        
        String status2 = planOutput.getFlightsItineraryInformation().get(2).getStatus();
        System.out.println("Get status: " + status2);
        assertEquals("unconfirmed", status2);
    }
    
   @Test 
    public void planManyHotels() throws DatatypeConfigurationException{
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        String city = "Milan";
        String city1 = "London";
                
        //Get one hotel
        GetInputType input = new GetInputType();
        HotelRequestType hotelRequest = new HotelRequestType();
              
        GetHotelInputType hotel = CreateGetHotelInputType("Milan");
        GetHotelInputType hotel2 = CreateGetHotelInputType("London");
        hotelRequest.getHotelsList().add(hotel);
        hotelRequest.getHotelsList().add(hotel2);
        input.getHotelRequests().add(hotelRequest); 
        
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        String resultCity = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city, resultCity);
        String resultCity1 = output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city1, resultCity1);
        System.out.println("Getting an hotel from the city: " + resultCity); 
        System.out.println("Getting an hotel from the city: " + resultCity1); 
        
        String bookingNumber = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        String bookingNumber1 = output.getHotelsList().get(1).getHotelInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(bookingNumber);
        plan.getHotelsBookingNumber().add(bookingNumber1);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status);
        assertEquals("unconfirmed", status);
        
        String status1 = planOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + status1);
        assertEquals("unconfirmed", status1);
    }
    
    /* Get itinerary */
    
    @Test 
    public void getItineraryNotBooked() throws DatatypeConfigurationException{
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
               
        //Get one hotel
        String city = "Milan";
        GetInputType input = CreateGetInputType(city);
        
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        String resultCity = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city, resultCity);
        System.out.println("Getting an hotel from the city: " + resultCity); 
        
        String bookingNumber = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(bookingNumber);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        String booking_number = planOutput.getHotelsItineraryInformation().get(0).getBookingNumber();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status);
        
        // Requesting the itinerary again using the GetItinerary method
        ItineraryListType getInineraryOutput = getItinerary(receivedItinID);
        String status_bis = getInineraryOutput.getHotelsItineraryInformation().get(0).getStatus();
        String booking_number_bis = getInineraryOutput.getHotelsItineraryInformation().get(0).getBookingNumber();
        assertEquals(status, status_bis);
        assertEquals(booking_number, booking_number_bis);
    }
    
    @Test 
    public void getItineraryBooked() throws DatatypeConfigurationException{
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
               
        //Get one hotel
        String city = "Milan";
        GetInputType input = CreateGetInputType(city);
        
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        String resultCity = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city, resultCity);
        System.out.println("Getting an hotel from the city: " + resultCity); 
        
        String bookingNumber = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(bookingNumber);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status);
        
        CreditCardInfoType creditCard = new CreditCardInfoType(); 
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard); 
        status = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        String booking_number = bookOutput.getHotelsItineraryInformation().get(0).getBookingNumber();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("confirmed", status);    
        
        // Requesting the itinerary again using the GetItinerary method
        ItineraryListType getInineraryOutput = getItinerary(receivedItinID);
        String status_bis = getInineraryOutput.getHotelsItineraryInformation().get(0).getStatus();
        String booking_number_bis = getInineraryOutput.getHotelsItineraryInformation().get(0).getBookingNumber();
        assertEquals(status, status_bis);
        assertEquals(booking_number, booking_number_bis);
    }
    
    /* Cancel Plan */
    
    @Test 
    public void cancelPlanWithOneHotelAndOneFlight() throws DatatypeConfigurationException {
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
               
        // Create the input  
        GetInputType input = new GetInputType();
        
        //Create one flight request
        XMLGregorianCalendar date = CreateDate(26, 10, 2015);
        GetFlightsInputType flight = CreateGetFlightsInputType(date, "Copenhagen", "London");
        AddFlight(input, flight);
        
        // Create one hotel request
        String city = "Milan";
        XMLGregorianCalendar date1 = CreateDate(26, 11, 2015);
        XMLGregorianCalendar date2 = CreateDate(29, 11, 2015);
        GetHotelInputType hotel = CreateGetHotelInputType(city, date1, date2);
        AddHotel(input, hotel);
        
        // Making the request
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        
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
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String hotelStatus = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Hotel status: " + hotelStatus);
        assertEquals("unconfirmed", hotelStatus);
        
        String flightStatus = planOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Flight status: " + flightStatus);
        assertEquals("unconfirmed", flightStatus);
        
        // Cancel plan
        boolean cancel = cancelPlanning(receivedItinID);
        assertTrue(cancel);      
    } 
    
    /* Book hotels and flights */
    
    @Test 
    public void bookOneHotel() throws DatatypeConfigurationException{
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
               
        //Get one hotel
        String city = "Milan";
        GetInputType input = CreateGetInputType(city);
        
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        String resultCity = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city, resultCity);
        System.out.println("Getting an hotel from the city: " + resultCity); 
        
        String bookingNumber = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(bookingNumber);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status);
        
        // Booking the hotel
        CreditCardInfoType creditCard = new CreditCardInfoType(); 
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard); 
        status = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("confirmed", status);     
    }
    
   @Test 
    public void bookOneFlight() throws DatatypeConfigurationException{
        System.out.println("Test starts");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Creating the request
        GetInputType input = new GetInputType();
        FlightRequestType flightRequest = new FlightRequestType();
        GetFlightsInputType flight = CreateGetFlightsInputType("Copenhagen", "London", 26, 10, 2015);
        
        flightRequest.getFlightsList().add(flight);
        input.getFlightRequests().add(flightRequest); 

        // Make the request
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);        
        String expected1 = "Copenhagen";     
        String result1 = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();      
        assertEquals(expected1, result1);              
        
        String bookingNumber = output.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getFlightsBookingNumber().add(bookingNumber);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status + "Copenhagen");
        assertEquals("unconfirmed", status);
        
        // Booking the flight
        CreditCardInfoType creditCard = CreateCreditCard("Bruhn Brigitte", "50408821", 2, 10);
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard);
        
        status = bookOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status);
        assertEquals("confirmed", status);    
    }
    
    @Test 
    public void bookOneFlightAndOneHotel() throws DatatypeConfigurationException{
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
               
        // Create the input  
        GetInputType input = new GetInputType();
        
        //Create one flight request
        XMLGregorianCalendar date = CreateDate(26, 10, 2015);
        GetFlightsInputType flight = CreateGetFlightsInputType(date, "Copenhagen", "London");
        AddFlight(input, flight);
        
        // Create one hotel request
        String city = "Milan";
        XMLGregorianCalendar date1 = CreateDate(26, 11, 2015);
        XMLGregorianCalendar date2 = CreateDate(29, 11, 2015);
        GetHotelInputType hotel = CreateGetHotelInputType(city, date1, date2);
        AddHotel(input, hotel);
        
        // Making the request
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        
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
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String hotelStatus = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Hotel status: " + hotelStatus);
        assertEquals("unconfirmed", hotelStatus);
        
        String flightStatus = planOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Flight status: " + flightStatus);
        assertEquals("unconfirmed", flightStatus);
        
        // Booking the hotel
        CreditCardInfoType creditCard = CreateCreditCard("Bruhn Brigitte", "50408821", 2, 10);
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard); 
          
        hotelStatus = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Hotel status: " + hotelStatus);
        assertEquals("confirmed", hotelStatus);    
        
        flightStatus = bookOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Flight status: " + flightStatus);
        assertEquals("confirmed", flightStatus);
        System.out.println("booking passed!");
        
        //cancelling
        ItineraryListType cancelOutput = cancelItinerary(receivedItinID);
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
    public void bookManyFlights() throws DatatypeConfigurationException{
         System.out.println("Test starts");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
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
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        System.out.println("Request done");
          
        String expected1 = "Copenhagen";
        String expected2 = "London"; 
        String expected3 = "Copenhagen";       
        
        String result1 = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();      
        assertEquals(expected1, result1);              
        String result2 = output.getFlightsList().get(1).getFlightInformations().get(0).getFlight().getStart();     
        assertEquals(expected2, result2); 
        String result3 = output.getFlightsList().get(2).getFlightInformations().get(0).getFlight().getStart();     
        assertEquals(expected3, result3);
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        
        String bookingNumber = output.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        String bookingNumber1 = output.getFlightsList().get(1).getFlightInformations().get(0).getBookingNumber();
        String bookingNumber2 = output.getFlightsList().get(2).getFlightInformations().get(0).getBookingNumber();
        
        plan.getFlightsBookingNumber().add(bookingNumber);
        plan.getFlightsBookingNumber().add(bookingNumber1);
        plan.getFlightsBookingNumber().add(bookingNumber2);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status);
        assertEquals("unconfirmed", status);
        
        String status1 = planOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + status1);
        assertEquals("unconfirmed", status1);
        
        String status2 = planOutput.getFlightsItineraryInformation().get(2).getStatus();
        System.out.println("Get status: " + status2);
        assertEquals("unconfirmed", status2);
        
        // Booking the flights
        CreditCardInfoType creditCard = CreateCreditCard("Bruhn Brigitte", "50408821", 2, 10);
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard);
        
        status = bookOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status);
        assertEquals("confirmed", status);
        
        status1 = bookOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + status1);
        assertEquals("confirmed", status1);
        
        status2 = bookOutput.getFlightsItineraryInformation().get(2).getStatus();
        System.out.println("Get status: " + status2);
        assertEquals("confirmed", status2);
    }
    
    @Test 
    public void bookManyHotels() throws DatatypeConfigurationException{
        System.out.println("Test starts - bookManyHotels");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
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
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
               
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
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status);
        String status2 = planOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status2);
        
        // Booking the hotel
        CreditCardInfoType creditCard = new CreditCardInfoType(); 
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard); 
        status = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("confirmed", status);
        
        status2 = bookOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(1).getStatus());
        assertEquals("confirmed", status2);
    }
    
    @Test // One of the scenario:  TODO add one flight
    public void bookManyHotelwithOneFail() throws DatatypeConfigurationException{
        System.out.println("Test starts - bookManyHotels");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
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
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
               
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
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status);
        
        String status2 = planOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status2);
        
        // Booking the hotel but the second one should fail
        CreditCardInfoType creditCard = new CreditCardInfoType(); 
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard);
        status = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("cancelled", status);
        
        status2 = bookOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(1).getStatus());
        assertEquals("unconfirmed", status2);
    }
    
    /*
     * Scenario B:
     * Planing an itinerary with many three booking, getting the their status as unconfirmed
     * booking them. The second one should fail and then we have to check the status.
     */
    
    @Test 
    public void bookManyFlightsAndOneHotelwithOneFlightFail() throws DatatypeConfigurationException{
        System.out.println("Test starts - bookManyFlights");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        //Creating a get request with many flights and one hotel
        GetInputType input = new GetInputType();
        XMLGregorianCalendar date = CreateDate(26, 10, 2015);
        GetFlightsInputType flight1 = CreateGetFlightsInputType(date, "Copenhagen", "London");
        GetFlightsInputType flight2 = CreateGetFlightsInputType(date, "London", "New York");
        AddFlight(input, flight1);
        AddFlight(input, flight2);
       
        String city = "Milan";
        XMLGregorianCalendar date1 = CreateDate(26, 11, 2015);
        XMLGregorianCalendar date2 = CreateDate(29, 11, 2015);
        GetHotelInputType hotel = CreateGetHotelInputType(city, date1, date2);
        AddHotel(input, hotel);
        
        // Making the get request
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        String resultStart = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();
        assertEquals("Copenhagen", resultStart);
        System.out.println(resultStart);
        String resultStart1 = output.getFlightsList().get(1).getFlightInformations().get(0).getFlight().getStart();
        assertEquals("London", resultStart1);
        System.out.println(resultStart1);      
        String resultCity = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getAddress();
        assertEquals(city, resultCity);
        System.out.println(resultCity);
        
        String flightBookingNumber = output.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        String flightBookingNumber1 = output.getFlightsList().get(1).getFlightInformations().get(0).getBookingNumber();
        String hotelBookingNumber = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
               
        // Plan itinerary using the ID number with a wrong booking number for the second flight
        PlanInputType plan = new PlanInputType();
        plan.getFlightsBookingNumber().add(flightBookingNumber);
        plan.getFlightsBookingNumber().add("hello");
        plan.getHotelsBookingNumber().add(hotelBookingNumber);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        System.out.println("Planning: ");
        String status = planOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Flight status: " + status);
        assertEquals("unconfirmed", status);
        
        String status1 = planOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("2nd Flight status: " + status1);
        assertEquals("unconfirmed", status1);
        
        String hotelStatus = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Hotel status: " + hotelStatus);
        assertEquals("unconfirmed", hotelStatus);
        
        // Booking the flight but the second one should fail
        System.out.println("Booking: ");
        CreditCardInfoType creditCard = CreateCreditCard("Bruhn Brigitte", "50408821", 2, 10);
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard);
        status = bookOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Flight status: " + status);
        assertEquals("cancelled", status);
        
        status1 = bookOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Failed book flight status: " + status1);
        assertEquals("unconfirmed", status1);         
    }
    
    /* Cancel itinerary hotels and flights */
    
    @Test 
    public void cancelOneHotel() throws DatatypeConfigurationException{
        System.out.println("Test starts - bookManyHotels");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Creating the request      
        GetInputType input = new GetInputType();
        HotelRequestType hotelRequest = new HotelRequestType();
            
        GetHotelInputType hotel = CreateGetHotelInputType("Milan");
        hotelRequest.getHotelsList().add(hotel);
        input.getHotelRequests().add(hotelRequest); 
        
        // Make the request
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
               
        String expected1 = "Milan Hotel";
        System.out.println("GetHotelsList size: " + output.getHotelsList().size());
       
        String result1 = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName();
        System.out.println(result1);
         
        assertEquals(expected1, result1);  
        
        String bookingNumber1 = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
      
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(bookingNumber1);        
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
               
        // Getting the status as unconfirmed
        String status = planOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + planOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("unconfirmed", status);
        
        // Booking the hotels
        CreditCardInfoType creditCard = CreateCreditCard("Bruhn Brigitte", "50408821", 2, 10);
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard); 
        
        status = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("confirmed", status);
        
        
        // Cancelling of the hotel
        ItineraryListType cancelOutput = cancelItinerary(receivedItinID);            
            
        status = cancelOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + cancelOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("cancelled", status);
    }
    
    @Test 
    public void cancelManyHotel() throws DatatypeConfigurationException{
                System.out.println("Test starts - bookManyHotels");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
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
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
               
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
        
        String bookingNumber1 = output.getHotelsList().get(0).getHotelInformations().get(0).getBookingNumber();
        String bookingNumber2 = output.getHotelsList().get(1).getHotelInformations().get(0).getBookingNumber();
        String bookingNumber3 = output.getHotelsList().get(2).getHotelInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getHotelsBookingNumber().add(bookingNumber1);        
        plan.getHotelsBookingNumber().add(bookingNumber2);
        plan.getHotelsBookingNumber().add(bookingNumber3);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
               
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
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard); // Fails here because BPEL doesn't check that the list of flights is null and try to assigned the value
        
        status = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("confirmed", status);
        
        status2 = bookOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(1).getStatus());
        assertEquals("confirmed", status2);
        
        status3 = bookOutput.getHotelsItineraryInformation().get(2).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(1).getStatus());
        assertEquals("confirmed", status3);
        
        System.out.println("booking done");
        
        // Cancelling of the hotel
        ItineraryListType cancelOutput = cancelItinerary(receivedItinID);            
            
        status = cancelOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status);
        assertEquals("cancelled", status);

        status2 = cancelOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " +status2);
        assertEquals("cancelled", status2);
            
        status3 = cancelOutput.getHotelsItineraryInformation().get(2).getStatus();
        System.out.println("Get status: " + status3);
        assertEquals("cancelled", status3);
    }
    
    @Test // One of the scenario: P2
    public void cancelManyHotelWithOneFail() throws DatatypeConfigurationException{
        System.out.println("Test starts - bookManyHotels");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
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
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
               
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
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
               
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
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard); // Fails here because BPEL doesn't check that the list of flights is null and try to assigned the value
        
        status = bookOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(0).getStatus());
        assertEquals("confirmed", status);
        
        status2 = bookOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(1).getStatus());
        assertEquals("confirmed", status2);
        
        status3 = bookOutput.getHotelsItineraryInformation().get(2).getStatus();
        System.out.println("Get status: " + bookOutput.getHotelsItineraryInformation().get(1).getStatus());
        assertEquals("confirmed", status3);
        
        System.out.println("booking done");
        
        // Cancelling of the hotel
        ItineraryListType cancelOutput = cancelItinerary(receivedItinID);            
            
        status = cancelOutput.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status);
        assertEquals("cancelled", status);

        status2 = cancelOutput.getHotelsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " +status2);
        assertEquals("confirmed", status2);
            
        status3 = cancelOutput.getHotelsItineraryInformation().get(2).getStatus();
        System.out.println("Get status: " + status3);
        assertEquals("cancelled", status3);
    }
    
    @Test 
    public void cancelOneFlight() throws DatatypeConfigurationException{
        System.out.println("Test starts");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
        // Creating the request
        GetInputType input = new GetInputType();
        FlightRequestType flightRequest = new FlightRequestType();
        GetFlightsInputType flight = CreateGetFlightsInputType("Copenhagen", "London", 26, 10, 2015);
        
        flightRequest.getFlightsList().add(flight);
        input.getFlightRequests().add(flightRequest); 

        // Make the request
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);        
        String expected1 = "Copenhagen";     
        String result1 = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();      
        assertEquals(expected1, result1);              
        
        String bookingNumber = output.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        plan.getFlightsBookingNumber().add(bookingNumber);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status + "Copenhagen");
        assertEquals("unconfirmed", status);
        
        // Booking the flight
        CreditCardInfoType creditCard = CreateCreditCard("Bruhn Brigitte", "50408821", 2, 10);
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard);
        
        status = bookOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status);
        assertEquals("confirmed", status);
        
        
        // Cancelling of the flight
        ItineraryListType cancelOutput = cancelItinerary(receivedItinID);            
            
        status = cancelOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + cancelOutput.getFlightsItineraryInformation().get(0).getStatus());
        assertEquals("cancelled", status);
        
    }
    
    @Test 
    public void cancelManyFlight() throws DatatypeConfigurationException{
        System.out.println("Test starts");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
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
        GetOutputType output = getFlightsAndHotels(input, receivedItinID);
        System.out.println("Request done");
          
        String expected1 = "Copenhagen";
        String expected2 = "London"; 
        String expected3 = "Copenhagen";       
        
        String result1 = output.getFlightsList().get(0).getFlightInformations().get(0).getFlight().getStart();      
        assertEquals(expected1, result1);              
        String result2 = output.getFlightsList().get(1).getFlightInformations().get(0).getFlight().getStart();     
        assertEquals(expected2, result2); 
        String result3 = output.getFlightsList().get(2).getFlightInformations().get(0).getFlight().getStart();     
        assertEquals(expected3, result3);
        
        // Plan itinerary using the ID number
        PlanInputType plan = new PlanInputType();
        
        String bookingNumber = output.getFlightsList().get(0).getFlightInformations().get(0).getBookingNumber();
        String bookingNumber1 = output.getFlightsList().get(1).getFlightInformations().get(0).getBookingNumber();
        String bookingNumber2 = output.getFlightsList().get(2).getFlightInformations().get(0).getBookingNumber();
        
        plan.getFlightsBookingNumber().add(bookingNumber);
        plan.getFlightsBookingNumber().add(bookingNumber1);
        plan.getFlightsBookingNumber().add(bookingNumber2);
        ItineraryListType planOutput = planFlightsAndHotels(plan, receivedItinID);
        
        // Getting the status as unconfirmed
        String status = planOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status);
        assertEquals("unconfirmed", status);
        
        String status1 = planOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + status1);
        assertEquals("unconfirmed", status1);
        
        String status2 = planOutput.getFlightsItineraryInformation().get(2).getStatus();
        System.out.println("Get status: " + status2);
        assertEquals("unconfirmed", status2);
        
        // Booking the flight
        CreditCardInfoType creditCard = CreateCreditCard("Bruhn Brigitte", "50408821", 2, 10);
        ItineraryListType bookOutput = bookItinerary(receivedItinID, creditCard);
        
        status = bookOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + status);
        assertEquals("confirmed", status);
        
        status1 = bookOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + status1);
        assertEquals("confirmed", status1);
        
        status2 = bookOutput.getFlightsItineraryInformation().get(2).getStatus();
        System.out.println("Get status: " + status2);
        assertEquals("confirmed", status2);
        
        ItineraryListType cancelOutput = cancelItinerary(receivedItinID);            
            
        status = cancelOutput.getFlightsItineraryInformation().get(0).getStatus();
        System.out.println("Get status: " + cancelOutput.getFlightsItineraryInformation().get(0).getStatus());
        assertEquals("cancelled", status);
        
        status1 = cancelOutput.getFlightsItineraryInformation().get(1).getStatus();
        System.out.println("Get status: " + cancelOutput.getFlightsItineraryInformation().get(0).getStatus());
        assertEquals("cancelled", status1);
        
        status2 = cancelOutput.getFlightsItineraryInformation().get(2).getStatus();
        System.out.println("Get status: " + cancelOutput.getFlightsItineraryInformation().get(0).getStatus());
        assertEquals("cancelled", status2);
    }
    
    @Test
    public void flightsAndHotels() throws DatatypeConfigurationException{
        
        System.out.println("Test starts");
        
        //Create the itinerary 
        String receivedItinID = createItinerary();
        System.out.println("Itinerary created with following ID: " + receivedItinID);
        
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
        
        System.out.println("Inputs created");
        
        // Make the request
        GetOutputType output = getFlightsAndHotels(inputData, receivedItinID);
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
}
