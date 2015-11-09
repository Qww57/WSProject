/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lameduck;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingType;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.BookFlightFault;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.CancelFlightFault;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.*;
/**
 *
 * @author Ehsan
 */
@WebService(serviceName = "LameDuckService", portName = "LameDuckBindingPort", endpointInterface = "org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.LameDuckPortType", targetNamespace = "http://j2ee.netbeans.org/wsdl/LameDuckWS/LameDuckWS/LameDuck", wsdlLocation = "WEB-INF/wsdl/LameDuckService/LameDuck.wsdl")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class LameDuckService {
    
    //initialization of database of flight list
    private List<FlightType> initializeFlightList() throws DatatypeConfigurationException{
        List<FlightType> listOfFlights= new ArrayList<FlightType>();
        FlightType flight1 =CreateFlight("Copenhagen", "London", SetGregorianDateTime(14,25,26,10,2015), SetGregorianDateTime(17,05,26,10,2015), "Ryanair");
        listOfFlights.add(flight1);
        FlightType flight2 =CreateFlight("London", "New York", SetGregorianDateTime(20,00,26,10,2015), SetGregorianDateTime(8,30,27,10,2015), "British Airways");
        listOfFlights.add(flight2);
        FlightType flight3 =CreateFlight("Copenhagen", "Kuala Lumpur", SetGregorianDateTime(7,25,26,2,2016), SetGregorianDateTime(17,05,26,2,2016), "Malasian Airways");
        listOfFlights.add(flight3);
        FlightType flight4 =CreateFlight("New York", "London", SetGregorianDateTime(14,25,30,10,2015), SetGregorianDateTime(23,05,27,10,2015), "Iberia");
        listOfFlights.add(flight4);
        FlightType flight5 =CreateFlight("Barcelona", "New York", SetGregorianDateTime(23,50,26,12,2015), SetGregorianDateTime(10,10,27,12,2015), "SAS");
        listOfFlights.add(flight5);
        return listOfFlights;
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
    
    //FlightType creator
    private FlightType CreateFlight(String Start, String Destination, XMLGregorianCalendar StartDateTime, XMLGregorianCalendar DestinationDateTime, String carrier ){
        FlightType flight = new FlightType();
        flight.setStart(Start);
        flight.setDestination(Destination);
        flight.setStartDateTime(StartDateTime);
        flight.setDestinationDateTime(StartDateTime);
        flight.setCarrier(carrier);
        
        return flight;
    }
    
    public GetFlightsOutputType getFlights(GetFlightsInputType getFlightsInput) throws DatatypeConfigurationException {
        //initialise list of all flights
        List<FlightType> listOfFlights = initializeFlightList();
        //create variables
        String start = getFlightsInput.getStart().toLowerCase();
        String destination = getFlightsInput.getDestination().toLowerCase();
        Integer day = getFlightsInput.getDate().getDay();
        Integer month = getFlightsInput.getDate().getMonth();
        Integer year = getFlightsInput.getDate().getYear();
        
        //Create list to be returned
        GetFlightsOutputType response = new GetFlightsOutputType();
        //iterate through flights
        for(int i=0; i < listOfFlights.size(); i++) {
            String this_start = listOfFlights.get(i).getStart().toLowerCase();
            String this_destination = listOfFlights.get(i).getDestination().toLowerCase();
            Integer this_day = listOfFlights.get(i).getDestinationDateTime().getDay();
            Integer this_month = listOfFlights.get(i).getDestinationDateTime().getMonth();
            Integer this_year = listOfFlights.get(i).getDestinationDateTime().getYear();
            if ((start.equals(this_start)) && (destination.equals(this_destination)) && (day.equals(this_day)) && (month.equals(this_month)) && (year.equals(this_year))) {
                FlightInformationType this_flight = new FlightInformationType();
                String bookingNumber = Integer.toString(i+19457);
                this_flight.setBookingNumber(bookingNumber);
                Integer price = 120;
                this_flight.setPrice(price);
                String AirlineReservationService = "LameDuck";
                this_flight.setAirlineReservationService(AirlineReservationService);
                FlightType this_flight_this = listOfFlights.get(i);
                this_flight.setFlight(this_flight_this);
                response.getFlightInformations().add(this_flight);
            }
        }
        
        return response;
    }
    

    public boolean bookFlight(BookFlightInputType bookFlightInput) throws BookFlightFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void cancelFlight(CancelFlightInputType cancelFlightInput) throws CancelFlightFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
