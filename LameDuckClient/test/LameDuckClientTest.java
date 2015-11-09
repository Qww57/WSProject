/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
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
    public void LameDuckTest() throws DatatypeConfigurationException {
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
        firstFlight.setPrice(120);
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
        System.out.println("Comparing size of list: ");
        assertEquals(outputFlightList.getFlightInformations().size(), expectedFlightList.getFlightInformations().size());
        System.out.println("Comparing Booking Number ");
        assertEquals(outputFlightList.getFlightInformations().get(0).getBookingNumber(), expectedFlightList.getFlightInformations().get(0).getBookingNumber());
        System.out.println("Comparing Start Date and Time (Gregorian Calendar: ");
        assertEquals(outputFlightList.getFlightInformations().get(0).getFlight().getStartDateTime(), expectedFlightList.getFlightInformations().get(0).getFlight().getStartDateTime());
        //more fields can be compared
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
}
