/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lameduck;

import dk.dtu.imm.fastmoney.BankService;
import dk.dtu.imm.fastmoney.CreditCardFaultMessage;
import dk.dtu.imm.fastmoney.types.AccountType;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceRef;
import static lameduck.DataBase.*;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.BookFlightFault;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.BookFlightInputType;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.CancelFlightFault;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.CancelFlightInputType;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.FlightInformationType;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.FlightType;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.GetFlightsInputType;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.GetFlightsOutputType;

/**
 *
 * @author Daniel
 */
@WebService(serviceName = "LameDuckService", portName = "LameDuckBindingPort", endpointInterface = "org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.LameDuckPortType", targetNamespace = "http://j2ee.netbeans.org/wsdl/LameDuckWS/LameDuckWS/LameDuck", wsdlLocation = "WEB-INF/wsdl/LameDuckService/LameDuck.wsdl")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class LameDuckService {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/fastmoney.imm.dtu.dk_8080/BankService.wsdl")
    private BankService service_1;

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/fastmoney.imm.dtu.dk_8080/BankService.wsdl")
    private BankService service;
    private static List<FlightType> flightDataBase;
    private static HashMap<FlightType, String> bookingDB;
    private static HashMap<FlightType, Boolean> availabilityDB;
    private static int group = 1; // The number of our project group

    private void InitializeDataBases() throws DatatypeConfigurationException {
        //System.out.println("Initializing Databases");
        if (flightDataBase == null) {
            flightDataBase = initializeFlightList();
        }
        if (bookingDB == null) {
            bookingDB = InitializeBookingHashtable(flightDataBase);
        }
        if (availabilityDB == null) {
            availabilityDB = InitializeAvailableFlightHashtable(flightDataBase);
        }
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
        for (int i = 0; i < listOfFlights.size(); i++) {
            String this_start = listOfFlights.get(i).getStart().toLowerCase();
            String this_destination = listOfFlights.get(i).getDestination().toLowerCase();
            Integer this_day = listOfFlights.get(i).getDestinationDateTime().getDay();
            Integer this_month = listOfFlights.get(i).getDestinationDateTime().getMonth();
            Integer this_year = listOfFlights.get(i).getDestinationDateTime().getYear();
            if ((start.equals(this_start)) && (destination.equals(this_destination)) && (day.equals(this_day)) && (month.equals(this_month)) && (year.equals(this_year))) {
                FlightInformationType this_flight = new FlightInformationType();
                String bookingNumber = Integer.toString(i + 19457);
                this_flight.setBookingNumber(bookingNumber);
                Integer price = 5000;
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
        System.out.println("BOOKING - START");

        if (bookFlightInput != null) {
            try {
                // Initialize the list of flights we are using as a database
                InitializeDataBases();

                // Reverse Hashmap in order to get the boolean CreditCardGuarantee using the booking number
                HashMap<String, FlightType> reversedHM = reverse(bookingDB);

                String bookingNumber = bookFlightInput.getBookingNumber();
                System.out.println("BOOKING - Got booking number: " + bookingNumber);
                FlightType bookedFlight = reversedHM.get(bookingNumber);
                System.out.println("BOOKING - Got booked Flight: " + bookedFlight);

                if (bookedFlight != null) {

                    CreditCardInfoType creditCardInfo = bookFlightInput.getCreditCard();
                    AccountType account = new AccountType();
                    account.setName("LameDuck");
                    account.setNumber("50208812");
                    System.out.println("Account information: ");
                    System.out.println(account.getName() + " - " + account.getNumber());

                    //TODO Get a real price from the booking 
                    int price = 1500;

                    try {
                        System.out.println("BOOKING - Valide Credit Card");
                        chargeCreditCard(group, creditCardInfo, price, account);
                        availabilityDB.replace(bookedFlight, false);
                        System.out.println("BOOKING - Flight successfuly booked");
                    } catch (CreditCardFaultMessage e) { // if there is an error while charging the bank account
                        System.out.println("Message : " + e.getFaultInfo().getMessage());
                        BookFlightFault fault = new BookFlightFault(e.getFaultInfo().getMessage(), "BookFlightFault");
                        throw fault;
                    }

                } else { // if no matching hotel was found   
                    System.out.println("BOOKING - ERROR - No flight found for the current booking number");
                    BookFlightFault fault = new BookFlightFault("The booking number you provided was not linked to any flight", "BookFlightFault");
                    throw fault;
                }
            } catch (DatatypeConfigurationException ex) {
                System.out.println("BOOKING - ERROR - Error initializing list of flights");
                BookFlightFault fault = new BookFlightFault("Error initializing list of flights", "BookFlightFault");
                throw fault;
            }

            System.out.println("BOOKING - END");
            return true;
        } else { // If no input
            BookFlightFault exception = new BookFlightFault("Empty", "BookFlightFault");
            throw exception;
        }
    }

    public void cancelFlight(CancelFlightInputType cancelFlightInput) throws CancelFlightFault {
        System.out.println("CANCELING - START");

        if (cancelFlightInput != null) {
            try {
                String bookingNumber = cancelFlightInput.getBookingNumber();

                // Initialize the list of flights we are using as a database
                InitializeDataBases();

                // Putting the availability of the hotel to true
                HashMap<String, FlightType> reversedHM = reverse(bookingDB);

                if (reversedHM.get(bookingNumber) != null) {
                    FlightType bookedFlight = reversedHM.get(bookingNumber);
                    availabilityDB.replace(bookedFlight, true);
                    System.out.println("CANCELING - SUCCESS - END");
                } else {
                    System.out.println("BOOKING - ERROR - No flight found for the current booking number");
                    CancelFlightFault fault = new CancelFlightFault("The booking number you provided was not linked to any flight", "CancelFlightFault");
                    throw fault;
                }
            } catch (DatatypeConfigurationException ex) {
                System.out.println("BOOKING - ERROR - Error initializing list of flights");
                CancelFlightFault fault = new CancelFlightFault("Error initializing list of flights", "CancelFlightFault");
                throw fault;
            }
        } else { // If no input
            CancelFlightFault exception = new CancelFlightFault("Empty", "CancelFlightFault");
            throw exception;
        }
    }

    private boolean validateCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount) throws CreditCardFaultMessage {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        dk.dtu.imm.fastmoney.BankPortType port = service_1.getBankPort();
        return port.validateCreditCard(group, creditCardInfo, amount);
    }

    private boolean chargeCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount, dk.dtu.imm.fastmoney.types.AccountType account) throws CreditCardFaultMessage {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        dk.dtu.imm.fastmoney.BankPortType port = service_1.getBankPort();
        return port.chargeCreditCard(group, creditCardInfo, amount, account);
    }

}
