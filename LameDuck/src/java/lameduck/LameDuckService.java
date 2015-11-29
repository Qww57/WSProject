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
        System.out.println("GETTING FLIGHT - START " + getFlightsInput.getStart());
        
        InitializeDataBases();
        
        //create variables
        String start = getFlightsInput.getStart().toLowerCase();
        String destination = getFlightsInput.getDestination().toLowerCase();
        Integer day = getFlightsInput.getDate().getDay();
        Integer month = getFlightsInput.getDate().getMonth();
        Integer year = getFlightsInput.getDate().getYear();

        //Create list to be returned
        GetFlightsOutputType response = new GetFlightsOutputType();
        //iterate through flights
        for (int i = 0; i < flightDataBase.size(); i++) {
            FlightType flight = flightDataBase.get(i);
            String this_start = flightDataBase.get(i).getStart().toLowerCase();
            String this_destination = flightDataBase.get(i).getDestination().toLowerCase();
            Integer this_day = flightDataBase.get(i).getDestinationDateTime().getDay();
            Integer this_month = flightDataBase.get(i).getDestinationDateTime().getMonth();
            Integer this_year = flightDataBase.get(i).getDestinationDateTime().getYear();
            if ((start.equals(this_start)) && (destination.equals(this_destination))) {
                FlightInformationType this_flight = new FlightInformationType();
                String bookingNumber = bookingDB.get(flight);
                this_flight.setBookingNumber(bookingNumber);
                Integer price = 5000;
                this_flight.setPrice(price);
                String AirlineReservationService = "LameDuck";
                this_flight.setAirlineReservationService(AirlineReservationService);
                FlightType this_flight_this = flightDataBase.get(i);
                this_flight.setFlight(this_flight_this);
                response.getFlightInformations().add(this_flight);
            } 
        }
        
         System.out.println("GETTING FLIGHT - END " + getFlightsInput.getStart() + getFlightsInput.getDate());
        
        return response;
    }

    public boolean bookFlight(BookFlightInputType bookFlightInput) throws BookFlightFault, DatatypeConfigurationException {
        System.out.println("BOOKING - START");

        if (bookFlightInput != null) {
           
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
                    int price = 5000;

                    try {
                        System.out.println("BOOKING - Charge Credit Card");
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
            System.out.println("BOOKING - END");
            return true;
        } else { // If no input
            BookFlightFault exception = new BookFlightFault("Empty", "BookFlightFault");
            throw exception;
        }
    }

    public boolean cancelFlight(CancelFlightInputType cancelFlightInput) throws CancelFlightFault, DatatypeConfigurationException {
        System.out.println("CANCELING - START");
        
        if (cancelFlightInput != null) {
            try {
                System.out.println("it's not null");
                AccountType account = new AccountType();
                account.setName("LameDuck");
                account.setNumber("50208812");
                refundCreditCard(group, cancelFlightInput.getCreditCard(), 2500, account);
                System.out.println("we canceled successfully");
                return true;
            } catch (CreditCardFaultMessage ex) {
                System.out.println("ERROR - There was an error refunding the flight");  
                CancelFlightFault fault = new CancelFlightFault("ERROR", "CancelFlightFault");
                throw fault;
            }
        } else { // If no input
            System.out.println("ERROR - Input was Empty");
            CancelFlightFault exception = new CancelFlightFault("Empty", "CancelFlightFault");
            throw exception;
        }
       
    }
    private boolean refundCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount, dk.dtu.imm.fastmoney.types.AccountType account) throws CreditCardFaultMessage {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        dk.dtu.imm.fastmoney.BankPortType port = service_1.getBankPort();
        return port.refundCreditCard(group, creditCardInfo, amount, account);
    }

    private boolean chargeCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount, dk.dtu.imm.fastmoney.types.AccountType account) throws CreditCardFaultMessage {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        dk.dtu.imm.fastmoney.BankPortType port = service_1.getBankPort();
        return port.chargeCreditCard(group, creditCardInfo, amount, account);
    }

}
