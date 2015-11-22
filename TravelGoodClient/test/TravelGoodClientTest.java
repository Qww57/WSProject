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
    public void testCreateItinerary(){
        System.out.println("Test Start");
        
        int myIntID = 1208;
        ItineraryResponseType itineraryCreation = createItinerary(myIntID);
        
        System.out.println("Itinerary Created");
        Boolean receivedItineraryCreated = itineraryCreation.isItineraryCreated();
        int receivedItinID = itineraryCreation.getItineraryID();
        
        assertEquals(myIntID, receivedItinID);     
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
        Integer myItinID = 1442;
        System.out.println("Ready to call operations");
        ItineraryResponseType itineraryCreation = createItinerary(myItinID);
        Boolean expectedItineraryCreated = true;
        Boolean receivedItineraryCreated = itineraryCreation.isItineraryCreated();
        Integer expectedItinID = myItinID;
        Integer receivedItinID = itineraryCreation.getItineraryID();
        assertEquals(receivedItinID, expectedItinID);
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
        System.out.println("Booked Itinerary length: " + AnnesBookedItinerary);
        Integer expectedBookedItinerarySize = 3;
        assertEquals(bookedItinerarySize, expectedBookedItinerarySize);
        expectedStatus2 = "confirmed";
        resultBookingNumber1 = AnnesBookedItinerary.getHotelsItineraryInformation().get(0).getBookingNumber();
        System.out.println("After Booking: Resulting booking number 1: " + resultBookingNumber1);
        resultStatus2 = AnnesBookedItinerary.getHotelsItineraryInformation().get(0).getStatus();
        System.out.println("After Booking: Resulting status 1: " + resultStatus1);
        assertEquals(expectedBookingNumber1, resultBookingNumber1);
        assertEquals(expectedStatus2, resultStatus2);
        
        
        
        
        
        
    }
    
    //@Test
    public void threeHotels() throws DatatypeConfigurationException{
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
        
        Integer myItinID = 1443;
        
        // Make the request
        GetOutputType output = getFlightsAndHotels(input, myItinID);
               
        String expected1 = "Milan Hotel";
        String expected2 = "London Hotel"; 
        System.out.println("GetHotelsList size: " + output.getHotelsList().size());
        System.out.println("GetHotelsInformations.get() size: " + output.getHotelsList().get(1).getHotelInformations().size());
        
        String result1 = output.getHotelsList().get(0).getHotelInformations().get(0).getHotel().getName();
        System.out.println(result1);
         
        assertEquals(expected1, result1);  
              
        System.out.println(output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName());
        System.out.println(output.getHotelsList().get(2).getHotelInformations().get(0).getHotel().getName());  
        System.out.println(output.getHotelsList().get(2).getHotelInformations().get(1).getHotel().getName());  
        //System.out.println("GetHotelsInformations.get(1) size: " + output.getHotelsList().get(0).getHotelInformations().size());
        String result2 = output.getHotelsList().get(1).getHotelInformations().get(0).getHotel().getName();       
        assertEquals(expected2, result2); 
        
        int result3 = output.getHotelsList().get(2).getHotelInformations().size(); 
        assertEquals(2, result3); 
        
    }
    
   // @Test
    public void threeFlights() throws DatatypeConfigurationException{
        System.out.println("Test starts");
        GetInputType input = new GetInputType();
        FlightRequestType flightRequest = new FlightRequestType();
            
        GetFlightsInputType flight = CreateGetFlightsInputType("Copenhagen", "London", 26, 10, 2015);
        GetFlightsInputType flight2 = CreateGetFlightsInputType("London", "New York", 26,10,2015);
        GetFlightsInputType flight3 = CreateGetFlightsInputType("Copenhagen", "Kuala Lumpur", 26,2,2016);
        
        System.out.println("Inputs created");
        flightRequest.getFlightsList().add(flight);
        flightRequest.getFlightsList().add(flight2);
        flightRequest.getFlightsList().add(flight3);
        input.getFlightRequests().add(flightRequest); 
        System.out.println("Inputs created bis");
          
        Integer myItinID = 1444;
        
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

    private static GetOutputType getFlightsAndHotels(org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.GetInputType part1, int part2) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.getFlightsAndHotels(part1, part2);
    }

    private static ItineraryResponseType createItinerary(int part1) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.createItinerary(part1);
    }

    private static ItineraryListType planFlightsAndHotels(org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.PlanInputType part1, int part2) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.planFlightsAndHotels(part1, part2);
    }

    private static ItineraryListType bookItinerary(int part1, org.netbeans.j2ee.wsdl.niceview.java.niceview.CreditCardInfoType part2) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.bookItinerary(part1, part2);
    }

    private static boolean cancelPlanning(int part1) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.cancelPlanning(part1);
    }

    private static ItineraryListType cancelItinerary(int part1, org.netbeans.j2ee.wsdl.niceview.java.niceview.CreditCardInfoType part2, org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.FlightPriceListType part3) {
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService service = new org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLService();
        org.netbeans.j2ee.wsdl.travelgoodbpel.src.travelgoodwsdl.TravelGoodWSDLPortType port = service.getTravelGoodWSDLPortTypeBindingPort();
        return port.cancelItinerary(part1, part2, part3);
    }
  
}
