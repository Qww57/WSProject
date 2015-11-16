/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package niceview;

import dk.dtu.imm.fastmoney.*;
import dk.dtu.imm.fastmoney.types.*;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceRef;
import static niceview.DataBase.*;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.*;

/**
 *
 * @author Quentin
 */

@WebService(serviceName = "NiceViewService", portName = "NiceViewBindingPort", endpointInterface = "org.netbeans.j2ee.wsdl.niceview.java.niceview.NiceViewPortType", targetNamespace = "http://j2ee.netbeans.org/wsdl/NiceView/java/NiceView", wsdlLocation = "WEB-INF/wsdl/NiceViewService/NiceView.wsdl")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class NiceViewService {

  @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/fastmoney.imm.dtu.dk_8080/BankService.wsdl")   
    private BankService service;    
    private static List<HotelType> hotelDataBase;
    private static HashMap<HotelType,String> bookingDB;
    private static HashMap<HotelType, Boolean> availabilityDB;
    private static int group = 1; // The number of our project group
    
    private void InitializeDataBases()throws DatatypeConfigurationException {
        //System.out.println("Initializing Databases");
        if(hotelDataBase == null){
            hotelDataBase = InitializeHotelList();
        }
        if(bookingDB == null){
            bookingDB = InitializeBookingHashtable(hotelDataBase);
        }
        if(availabilityDB == null){
            availabilityDB = InitializeAvailableHotelHashtable(hotelDataBase);
        }
    }
      
    public GetHotelsOutputType getHotels(GetHotelInputType hotelsRequest)throws DatatypeConfigurationException {              
        System.out.println("GETTING - START " + hotelsRequest.getCity());      
        
        GetHotelsOutputType listOfHotels = new GetHotelsOutputType();
        
        // Getting information from the request
        String city = hotelsRequest.getCity();
        XMLGregorianCalendar arrivalDate = hotelsRequest.getArrivalDate();
        XMLGregorianCalendar depertureDate = hotelsRequest.getDepartureDate(); 
        
        // Initialize the list of hotel we are using as a database
        InitializeDataBases();
        
        for(int i = 0; i < hotelDataBase.size(); i++)
        {
            //System.out.println("GETTING - Searching loop: " + i);          
            HotelType wantedHotel = hotelDataBase.get(i);
            String address = wantedHotel.getAddress();
            //System.out.println("GETTING - Searching loop: " + i + " Address: "+ address);  
            Boolean availableHotel = availabilityDB.get(wantedHotel);
            //System.out.println("GETTING - Searching loop: " + i + " availabilityDB: "+ availableHotel);  
            
            // Check if there is an hotel is the city and if the hotel is available
            if (address.equals(city) && availabilityDB.get(hotelDataBase.get(i)).equals(true))
            { 
                //System.out.println("GETTING - Match found: " + i);
                      
                // Creating and initializing a new HotelInformationType object
                HotelInformationType hotelInfo = new HotelInformationType();
                hotelInfo.setHotel(hotelDataBase.get(i)); // Set the hotel               
                hotelInfo.setReservationService("NiceView"); // Set the reservation service                                             
                hotelInfo.setBookingNumber(bookingDB.get(hotelDataBase.get(i))); // Initialize the booking number using the HT
                                
                // Computation of the number of days
                Date d1 = new GregorianCalendar(arrivalDate.getYear(), arrivalDate.getMonth(), 
                        arrivalDate.getDay(), arrivalDate.getHour(), arrivalDate.getMinute())
                        .getTime();
                Date d2 = new GregorianCalendar(depertureDate.getYear(), depertureDate.getMonth(), 
                        depertureDate.getDay(), depertureDate.getHour(), depertureDate.getMinute())
                        .getTime();               
                long diff = d2.getTime()- d1.getTime();             
                diff = diff / (1000*60*60*24);               
                //System.out.println("Number of nights - GetHotels: " + diff);
                
                // Setting the price
                int nightPrice = 400;  // We assumed that a night in all hotels costs the same price
                int globalPrice = (int) diff * nightPrice;          
                hotelInfo.setPrice(globalPrice);
                                
                // Add the hotel to the list of results
                //System.out.println("Match info - GetHotels: " + hotelInfo);              
                listOfHotels.getHotelInformations().add(hotelInfo);                
                //System.out.println("List of hotels info - GetHotels: " + listOfHotels.getHotelInformations().size());
            }
        }
       
        System.out.println("GETTING - END  - " + hotelsRequest.getCity().toString() + " - result size: " + listOfHotels.getHotelInformations().size());        
        return listOfHotels;
    }
       
    public boolean bookHotel(BookHotelInputType bookHotelReqest) throws BookHotelFault, DatatypeConfigurationException {

        System.out.println("BOOKING - START");
               
        if(bookHotelReqest != null){
            // Initialize the list of hotel we are using as a database
            InitializeDataBases();
            HashMap<String,HotelType> reversedHM = reverse (bookingDB); 
            
            // Reverse Hashmap in order to get the boolean CreditCardGuarantee using the booking number
            String bookingNumber = bookHotelReqest.getBookingNumber();
            System.out.println("BOOKING - Got booking number: " + bookingNumber);
            HotelType bookedHotel = reversedHM.get(bookingNumber);
            System.out.println("BOOKING - Got booked Hotel: " + bookedHotel);
            
            if (bookedHotel != null){             
                if (bookedHotel.isCreditCardGuarantee() == true){
                    System.out.println("BOOKING - Credit card guarantee needed to book the Hotel");               
                    CreditCardInfoType creditCardInfo = bookHotelReqest.getCreditCard();            
                    AccountType account = new AccountType();
                    account.setName("NiceView");
                    account.setNumber("50308815"); 
                    System.out.println("Account information: ");
                    System.out.println(account.getName() + " - " + account.getNumber());
                    
                    //TODO Get a real price from the booking 
                    int price = 1500;
                    
                    try {
                        System.out.println("BOOKING - Trying to validate the Credit Card");
                        validateCreditCard(group, creditCardInfo, price);
                        try{
                            System.out.println("BOOKING - Valide Credit Card");
                            chargeCreditCard(group, creditCardInfo, price, account);                       
                            availabilityDB.replace(bookedHotel, false);
                            System.out.println("BOOKING - Hotel successfuly booked");
                        }
                        catch(CreditCardFaultMessage e){ // if there is an error while charging the bank account
                            System.out.println("Message : " + e.getFaultInfo().getMessage());
                            BookHotelFault fault = new BookHotelFault( e.getFaultInfo().getMessage(), "BookHotelFault");
                            throw fault;
                        } 
                    } catch (CreditCardFaultMessage ex) { // if there is an error while validating the card info
                        System.out.println("Message : " + ex.getFaultInfo().getMessage());
                        BookHotelFault exception = new BookHotelFault( ex.getFaultInfo().getMessage(), "BookHotelFault");
                        throw exception;
                    }
                } else{ // if no credit card was required
                    System.out.println("BOOKING - No credit card guarantee needed to book the Hotel");                                
                    availabilityDB.replace(bookedHotel, false);               
                    System.out.println("BOOKING - Hotel successfuly booked");
               }        
            } else { // if no matching hotel was found   
                System.out.println("BOOKING - ERROR - No hotel found for the current booking number");  
                BookHotelFault fault = new BookHotelFault("The booking number you provided was not linked to any hotel", "BookHotelFault");
                throw fault;
            }   
            System.out.println("BOOKING - END");
            return true;
        } 
        else { // If no input
            BookHotelFault exception = new BookHotelFault("Empty", "BookHotelFault");
            throw exception;
        } 
    }

    public void cancelHotel(java.lang.String cancelHotelRequest) throws CancelHotelFault, DatatypeConfigurationException {
        System.out.println("CANCELING - START");
        
        if(cancelHotelRequest != null){
         
            String bookingNumber = cancelHotelRequest;

            // Initialize the list of hotel we are using as a database
            InitializeDataBases();

            // Putting the availability of the hotel to true
            HashMap<String,HotelType> reversedHM = reverse (bookingDB);
            
            if(reversedHM.get(bookingNumber) != null){
              
                   
                HotelType bookedHotel = reversedHM.get(bookingNumber);
                availabilityDB.replace(bookedHotel, true);           
                System.out.println("CANCELING - SUCCESS - END");
                
            } else {
                System.out.println("ERROR - There was an error canceling the hotel");  
                CancelHotelFault fault = new CancelHotelFault("ERROR", "CancelHotelFault");
                throw fault;
            }
        } else { // If no input
            CancelHotelFault exception = new CancelHotelFault("Empty", "CancelHotelFault");
            throw exception;
        } 
    }  
    
    // Web services references
    
    private boolean validateCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount) throws CreditCardFaultMessage {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        dk.dtu.imm.fastmoney.BankPortType port = service.getBankPort();
        return port.validateCreditCard(group, creditCardInfo, amount);
    }

    private boolean chargeCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount, AccountType account) throws CreditCardFaultMessage {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        dk.dtu.imm.fastmoney.BankPortType port = service.getBankPort();
        return port.chargeCreditCard(group, creditCardInfo, amount, account);
    }
}

