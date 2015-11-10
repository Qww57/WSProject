/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.soap.SOAPFaultException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.*;

/**
 *
 * @author Ehsan
 */
public class LameDuckClientTest {
    
    public LameDuckClientTest() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void LameDuckGetFlightTest() throws DatatypeConfigurationException {
        //input info
        String start = "Copenhagen";
        String destination = "London";
        GregorianCalendar gc = new GregorianCalendar(2015, 10, 26); //initialise with any values cause it needs to be rewriten anyways
        DatatypeFactory df = DatatypeFactory.newInstance();
        XMLGregorianCalendar date = df.newXMLGregorianCalendar(gc);
        date.setDay(26);
        date.setMonth(10);
        date.setYear(2015);
        GetFlightsInputType inputFlight = new GetFlightsInputType();
        inputFlight.setStart(start);
        inputFlight.setDestination(destination);
        inputFlight.setDate(date);
        
        //run server operation
        GetFlightsOutputType outputFlightList = getFlights(inputFlight);
        
        //create expected output object
        GetFlightsOutputType expectedFlightList = new GetFlightsOutputType();
        FlightInformationType firstFlight = new FlightInformationType();
        firstFlight.setBookingNumber("19457");
        firstFlight.setPrice(5000);
        firstFlight.setAirlineReservationService("LameDuck");
        FlightType expectedFlight = new FlightType();
        expectedFlight.setStart("Copenhagen");
        expectedFlight.setDestination("London");
        expectedFlight.setStartDateTime(SetGregorianDateTime(14,25,26,10,2015));
        expectedFlight.setDestinationDateTime(SetGregorianDateTime(17,05,26,10,2015));
        expectedFlight.setCarrier("Ryanair");
        firstFlight.setFlight(expectedFlight);
        expectedFlightList.getFlightInformations().add(firstFlight);
        
        //check some fields separatelly
        System.out.println("Comparing size of list");
        assertEquals(outputFlightList.getFlightInformations().size(), expectedFlightList.getFlightInformations().size());
        System.out.println("Comparing Booking Number ");
        assertEquals(outputFlightList.getFlightInformations().get(0).getBookingNumber(), expectedFlightList.getFlightInformations().get(0).getBookingNumber());
        System.out.println("Comparing Start Date and Time (Gregorian Calendar)");
        assertEquals(outputFlightList.getFlightInformations().get(0).getFlight().getStartDateTime(), expectedFlightList.getFlightInformations().get(0).getFlight().getStartDateTime());
        //more fields can be compared
    }
    
    // Booking of an flight with empty object as input
    @Test
    public void bookFlightTestError() throws BookFlightFault{
        try {
            assertTrue(bookFlight(null));
        } catch (BookFlightFault e) {
            System.out.println("Booking of flight with empty input has not passed");
            assertEquals("Empty", e.getMessage());
        } 
    }
    
    @Test
    public void bookFlightTestWithCreditCard() throws  BookFlightFault, DatatypeConfigurationException {
        try {
            // Booking of flight that requires credit card
            BookFlightInputType input = CreateBookFlightInputType("19457", "Tick Joachim", "50408824", 2, 11);
            boolean result = bookFlight(input);     
            assertEquals(true, result);
        } catch (BookFlightFault e) {
            System.out.println("Booking of flight that requires credit card has not passed");
            System.out.println(e.getMessage());
            fail();
        }
    }
    
    // Booking of an flight with unvalid booking number
    @Test 
     public void bookFlightTestError1() throws DatatypeConfigurationException, BookFlightFault {    
        BookFlightInputType input = CreateBookFlightInputType("19457", "Tick Joachim", "50408824", 2, 11);
        try {
            assertTrue(bookFlight(input));
        }
        catch (BookFlightFault ex) {
            System.out.println("Booking of a flight with invalid booking number has not passed");
            assertEquals("The booking number you provided was not linked to any flight",ex.getMessage());
        } 
    }
     // Booking of an hotel with unvalid card information
     @Test
    public void bookFlightTestError2() throws BookFlightFault, DatatypeConfigurationException{    
        BookFlightInputType input = CreateBookFlightInputType("19457", "Tick Joachim", "50408824", 2, 11);
        try {
            assertTrue(bookFlight(input));
        } catch (BookFlightFault e) {
            System.out.println("Booking of a flight with invalid card information has not passed");
            assertEquals("Month must be between 1 and 12",e.getMessage()); 
        }
    }
    
    // Booking of flight with not enough money on the bank account (price of flight is 5000)
    @Test
    public void bookFlightTestError3() throws BookFlightFault, DatatypeConfigurationException{    
        BookFlightInputType input = CreateBookFlightInputType("19457", "Tick Joachim", "50408824", 2, 11);
        try {
            assertTrue(bookFlight(input));
        } catch (BookFlightFault e) {
            System.out.println("Booking of a flight with insufficient money has not passed");
            assertEquals("The account has not enough money",e.getMessage());
        }       
    }
    
    @Test
    public void cancelFlightTestError1() throws DatatypeConfigurationException{    
        CancelFlightInputType input = CreateCancelFlightInputType("19457", "Tick Joachim", "5040824", 2, 11, 5000);
        try {
            cancelFlight(input);
        } catch (CancelFlightFault e) {
            System.out.println("Cancelling has not passed");
            assertEquals("An error occured while refunding flight",e.getMessage());
        }       
    }

    //XML Gregorian Date and Time Setter
    private XMLGregorianCalendar SetGregorianDateTime(Integer hours, Integer minutes, Integer day, Integer month, Integer year) throws DatatypeConfigurationException {
        GregorianCalendar gc = new GregorianCalendar(year, month, day);
        DatatypeFactory df = DatatypeFactory.newInstance();
        XMLGregorianCalendar gregorianDate = df.newXMLGregorianCalendar(gc);
        gregorianDate.setMinute(minutes);
        gregorianDate.setHour(hours);
        gregorianDate.setDay(day);
        gregorianDate.setMonth(month);
        gregorianDate.setYear(year);
        
        return gregorianDate;
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
    private static GetFlightsOutputType getFlights(GetFlightsInputType getFlightsInput) {
        LameDuckService service = new LameDuckService();
        LameDuckPortType port = service.getLameDuckBindingPort();
        return port.getFlights(getFlightsInput);
    }
    
    private static boolean bookFlight(BookFlightInputType bookFlightInput) throws BookFlightFault {
        LameDuckService service = new LameDuckService();
        LameDuckPortType port = service.getLameDuckBindingPort();
        return port.bookFlight(bookFlightInput);
    }

    private static boolean cancelFlight(org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.CancelFlightInputType cancelFlightInput) throws CancelFlightFault {
        org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.LameDuckService service = new org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.LameDuckService();
        org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.LameDuckPortType port = service.getLameDuckBindingPort();
        return port.cancelFlight(cancelFlightInput);
    }

    private BookFlightInputType CreateBookFlightInputType(String bookingNumber, String name, String cardNumber, int month, int year) throws DatatypeConfigurationException{
        BookFlightInputType input = new BookFlightInputType();
        input.setBookingNumber(bookingNumber);
        CreditCardInfoType creditCard = CreateCreditCard(name, cardNumber, month, year);
        input.setCreditCard(creditCard);
        return input;
    }
    
    private CancelFlightInputType CreateCancelFlightInputType(String bookingNumber, String name, String cardNumber, int month, int year, int price) throws DatatypeConfigurationException{
        CancelFlightInputType input = new CancelFlightInputType();
        input.setBookingNumber(bookingNumber);
        CreditCardInfoType creditCard = CreateCreditCard(name, cardNumber, month, year);
        input.setCreditCard(creditCard);
        input.setPrice(price);
        return input;
    }

    
}
