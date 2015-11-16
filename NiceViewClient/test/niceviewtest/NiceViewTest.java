/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package niceviewtest;

import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType.ExpirationDate;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.soap.SOAPFaultException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.*;

/**
 *
 * @author Quentin
 */
public class NiceViewTest {
    
    // TESTS FOR THE FUNCTION GETHOTELS    
    
    @Test
    public void getHotelsOneAnswer() throws DatatypeConfigurationException {
        System.out.println("getHotelsOneAnswer - START"); 
        String expected = "London Hotel";    
        GetHotelInputType input = CreateGetHotelInputType("London");
                
        GetHotelsOutputType result = getHotels(input);
        if (result.getHotelInformations().size() != 1){
            System.out.println("Wrong size");
            assertEquals(false, true);
        }
        else 
        {
            System.out.println("Right size");
            String resultName = result.getHotelInformations().get(0).getHotel().getName();
            System.out.println("Result name: " + resultName);
            assertEquals(expected, resultName);  
        }
    }
    
    @Test
    public void priceComputationCheck() throws DatatypeConfigurationException{
        System.out.println("priceComputationCheck - START");             
        int expected = 4000;      
        GetHotelInputType input = CreateGetHotelInputType("London");        
        GetHotelsOutputType result = getHotels(input);
        if (result.getHotelInformations().isEmpty()){
            System.out.println("Empty result");
            assertEquals(false, true);
        }
        else {
            int resultPrice = result.getHotelInformations().get(0).getPrice();
            System.out.println("Expected price: 4000");
            System.out.println("Result price: " + resultPrice);
            assertEquals(expected, resultPrice);  
        }    
    }
    
    @Test
    public void getHotelsTwoAnswers() throws DatatypeConfigurationException {
        System.out.println("getHotelsTwoAnswers - START");       
        String expected1 = "Hotel de France";
        String expected2 = "NY Hotel";    
        GetHotelInputType input = CreateGetHotelInputType("Paris");        
        GetHotelsOutputType result = getHotels(input);
        if (result.getHotelInformations().size() != 2){
            System.out.println("Wrong size");
            assertEquals(false, true);
        }
        else {
            System.out.println("Right size");
            String resultName1 = result.getHotelInformations().get(0).getHotel().getName();
            System.out.println("Result name: " + resultName1);
            assertEquals(expected1, resultName1);  
            String resultName2 = result.getHotelInformations().get(1).getHotel().getName();
            System.out.println("Result name: " + resultName2);
            assertEquals(expected2, resultName2);  
        }
    }
    
    @Test
    public void getHotelsNoAnswer() throws DatatypeConfigurationException {
        System.out.println("getHotelsNoAnswer - START");      
        boolean expected = true;  
        GetHotelInputType input = CreateGetHotelInputType("Helsinki");
        GetHotelsOutputType output = getHotels(input);
        assertEquals(expected, output.getHotelInformations().isEmpty()); 
    } 
    
    /// TESTS FOR THE BOOK FUNCTION
    
    //@Test
    public void bookHotelTest() throws BookHotelFault, DatatypeConfigurationException{
        // Booking of an hotel that doesn't require credit card 
        BookHotelInputType input = CreateBookHotelInputType("booking_Hotel_4", "Tick Joachim", "50408824", 2, 11);
        boolean result = bookHotel(input);
        assertEquals(true, result);     
    }
    
    @Test //TODO: pb because getting "Account doesn't exist" as error from the bank ws
    public void bookHotelTestWithCreditCard() throws BookHotelFault, DatatypeConfigurationException{
        // Booking of an hotel that requires credit card 
        try{
            BookHotelInputType input = CreateBookHotelInputType("booking_Hotel_2", "Thor-Jensen Claus", "50408825", 5, 9);
            boolean result = bookHotel(input);
            assertEquals(true, result);  
        } catch(SOAPFaultException e){
            System.out.println(e.getFault().getFaultString());
            fail();
        }
    }
    
     // Booking of an hotel with empty object as input
    @Test
    public void bookHotelTestError() throws BookHotelFault{
        try {
            assertTrue(bookHotel(null));
        } catch (SOAPFaultException e) {
            System.out.println("SOAPFaultException");
            assertEquals("Empty", e.getFault().getFaultString());
        } 
        /* catch (BookHotelFault f){
            System.out.println("BookHotelFault");
            assertEquals("Empty", f.getMessage());
        }*/
    }
    
    // Booking of an hotel with unvalid booking number
    @Test 
    public void bookHotelTestError1() throws DatatypeConfigurationException, BookHotelFault {    
        BookHotelInputType input = CreateBookHotelInputType("Hello you", "Tick Joachim", "50408824", 2, 11);
        try {
            assertTrue(bookHotel(input));
        } catch (SOAPFaultException e) {
            assertEquals("The booking number you provided was not linked to any hotel", e.getFault().getFaultString());
        } 
    }
    
    // Booking of an hotel with unvalid card information
    @Test
    public void bookHotelTestError2() throws BookHotelFault, DatatypeConfigurationException{    
        BookHotelInputType input = CreateBookHotelInputType("booking_Hotel_1", "Tick Joachim", "00000000", 0, 9);
        try {
            assertTrue(bookHotel(input));
        } catch (SOAPFaultException e) {
            assertEquals("Month must be between 1 and 12",e.getFault().getFaultString());
        }       
    }
    
    // Booking of an hotel with not enough money on the bank account (price of the hotel is 1500 and account limit is 1000)
    @Test
    public void bookHotelTestError3() throws BookHotelFault, DatatypeConfigurationException{    
        BookHotelInputType input = CreateBookHotelInputType("booking_Hotel_1", "Tobiasen Inge", "50408823", 9, 10);
        try {
            assertTrue(bookHotel(input));
        } catch (SOAPFaultException e) {
            assertEquals("The account has not enough money",e.getFault().getFaultString());
        }       
    }
    
    /*
    * There are two hotel in Paris, so we try to book one and then to make a get request to see if the booking worked
    */
    @Test 
    public void bookHotelScenarioTest() throws BookHotelFault, DatatypeConfigurationException, CancelHotelFault{
        // Booking of an hotel in Paris
        BookHotelInputType input = CreateBookHotelInputType("booking_Hotel_4", "Tick Joachim", "50408824", 2, 11);
        boolean result = bookHotel(input);
        assertEquals(true, result);
        System.out.println("first test result: " + result); 
        
        if(result == true){
            // Trying to get the list of hotel from Paris and we should have only one hotel because the NY hotel is already booked
            GetHotelInputType getInput = CreateGetHotelInputType("Paris");
            GetHotelsOutputType getOutput = getHotels(getInput);
            int expectedNbHotels = 1;
            int resultNbHotels = 0;
            if (getOutput.getHotelInformations().isEmpty() == false){
                resultNbHotels = getOutput.getHotelInformations().size();
            }  
            cancelHotel("booking_Hotel_4");
            assertEquals(expectedNbHotels, resultNbHotels);                       
        } 
        else {
            assertEquals(true, false);
        }
    }
    
    /// TESTS FOR THE CANCEL FUNCTION
    
    // Canceling of an hotel with no input
    @Test
    public void cancelHotelTestError() throws CancelHotelFault, DatatypeConfigurationException{    
        try {
            cancelHotel(null);
        } catch (SOAPFaultException e) {
            assertEquals("Empty",e.getFault().getFaultString());
        }       
    }
    
    // Canceling of an hotel with a wrong booking number
    @Test
    public void cancelHotelTestError1() throws CancelHotelFault, DatatypeConfigurationException{    
        String input = "hello you";
        try {
            cancelHotel(input);
        } catch (SOAPFaultException e) {
            assertEquals("The booking number you provided was not linked to any hotel",e.getFault().getFaultString());
        }       
    }
    
    // We book one of the two hotel in Paris and then cancel it
    @Test 
    public void cancelingHotelScenarioTest() throws BookHotelFault, DatatypeConfigurationException, CancelHotelFault{
        
        // There are two hotels available in Paris, we book one of them   
        BookHotelInputType input = CreateBookHotelInputType("booking_Hotel_4", "Tick Joachim", "50408824", 2, 11);
        boolean result = bookHotel(input);
        assertEquals(true, result);
        
        // We are now doing a get request, there should be so only one hotel available in Paris
        if(result == true){
            // Trying to get the list of hotel from Paris and we should have only one hotel because the NY hotel is already booked
            GetHotelInputType getInput = CreateGetHotelInputType("Paris");
            GetHotelsOutputType getOutput = getHotels(getInput);
            int expectedNbHotels = 1;
            int resultNbHotels = 0;
            if (getOutput.getHotelInformations().isEmpty() == false){
                resultNbHotels = getOutput.getHotelInformations().size();
            }        
            assertEquals(expectedNbHotels, resultNbHotels);  
        } 
        else {
            assertEquals(true, false);
        }
        
        // We cancel the hotel we had and try to get the list of hotel from Paris. We should have two hotels again.
        cancelHotel("booking_Hotel_4");        
        GetHotelInputType getInput = CreateGetHotelInputType("Paris");
        GetHotelsOutputType getOutput = getHotels(getInput);
        int expectedNbHotels = 2;
        int resultNbHotels = 0;
        if (getOutput.getHotelInformations().isEmpty() == false){
            resultNbHotels = getOutput.getHotelInformations().size();
        }        
        assertEquals(expectedNbHotels, resultNbHotels);        
    }      
    
    /// Constructors
    
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
    
    private BookHotelInputType CreateBookHotelInputType(String bookingNumber, String name, String cardNumber, int month, int year) throws DatatypeConfigurationException{
        BookHotelInputType input = new BookHotelInputType();
        input.setBookingNumber(bookingNumber);
        CreditCardInfoType creditCard = CreateCreditCard(name, cardNumber, month, year);
        input.setCreditCard(creditCard);
        return input;
    }
    
    private CreditCardInfoType CreateCreditCard(String name, String number, int month, int year){
        CreditCardInfoType creditCard = new CreditCardInfoType();
        creditCard.setName(name);
        creditCard.setNumber(number);
        ExpirationDate date = new ExpirationDate();
        date.setMonth(month);
        date.setYear(year);
        creditCard.setExpirationDate(date);
        return creditCard;
    }
  
    /// Web services references
    
    private static boolean bookHotel(org.netbeans.j2ee.wsdl.niceview.java.niceview.BookHotelInputType bookHotelReqest) throws BookHotelFault {
        org.netbeans.j2ee.wsdl.niceview.java.niceview.NiceViewService service = new org.netbeans.j2ee.wsdl.niceview.java.niceview.NiceViewService();
        org.netbeans.j2ee.wsdl.niceview.java.niceview.NiceViewPortType port = service.getNiceViewBindingPort();
        return port.bookHotel(bookHotelReqest);
    }

    private static void cancelHotel(java.lang.String cancelHotelRequest) throws CancelHotelFault {
        org.netbeans.j2ee.wsdl.niceview.java.niceview.NiceViewService service = new org.netbeans.j2ee.wsdl.niceview.java.niceview.NiceViewService();
        org.netbeans.j2ee.wsdl.niceview.java.niceview.NiceViewPortType port = service.getNiceViewBindingPort();
        port.cancelHotel(cancelHotelRequest);
    }

    private static GetHotelsOutputType getHotels(org.netbeans.j2ee.wsdl.niceview.java.niceview.GetHotelInputType hotelsRequest) {
        org.netbeans.j2ee.wsdl.niceview.java.niceview.NiceViewService service = new org.netbeans.j2ee.wsdl.niceview.java.niceview.NiceViewService();
        org.netbeans.j2ee.wsdl.niceview.java.niceview.NiceViewPortType port = service.getNiceViewBindingPort();
        return port.getHotels(hotelsRequest);
    }
}