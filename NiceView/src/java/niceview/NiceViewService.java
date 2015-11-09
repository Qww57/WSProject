/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package niceview;


import dk.dtu.imm.fastmoney.types.*;
import dk.dtu.imm.fastmoney.CreditCardFaultMessage;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import static niceview.DataBase.*;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.*;
import dk.dtu.imm.fastmoney.BankService;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceRef;

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
    
    private void InitializeDataBases(){
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
      
    public GetHotelsOutputType getHotels(GetHotelInputType hotelsRequest){              
        System.out.println("GETTING - START");      
        
        GetHotelsOutputType listOfHotels = new GetHotelsOutputType();
        
        // Getting information from the request
        String city = hotelsRequest.getCity();
        XMLGregorianCalendar arrivalDate = hotelsRequest.getArrivalDate();
        XMLGregorianCalendar depertureDate = hotelsRequest.getDepartureDate(); 
        
        // Initialize the list of hotel we are using as a database
        InitializeDataBases();
        
        for(int i = 0; i < hotelDataBase.size(); i++)
        {
            System.out.println("GETTING - Searching loop: " + i);          
            HotelType wantedHotel = hotelDataBase.get(i);
            String address = wantedHotel.getAddress();
            System.out.println("GETTING - Searching loop: " + i + " Address: "+ address);  
            Boolean availableHotel = availabilityDB.get(wantedHotel);
            System.out.println("GETTING - Searching loop: " + i + " availabilityDB: "+ availableHotel);  
            
            // Check if there is an hotel is the city and if the hotel is available
            if (address.equals(city) && availabilityDB.get(hotelDataBase.get(i)).equals(true))
            { 
                System.out.println("GETTING - Match found: " + i);
                      
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
                System.out.println("Number of nights - GetHotels: " + diff);
                
                // Setting the price
                int nightPrice = 400;  // We assumed that a night in all hotels costs the same price
                int globalPrice = (int) diff * nightPrice;          
                hotelInfo.setPrice(globalPrice);
                                
                // Add the hotel to the list of results
                System.out.println("Match info - GetHotels: " + hotelInfo);              
                listOfHotels.getHotelInformations().add(hotelInfo);                
                System.out.println("List of hotels info - GetHotels: " + listOfHotels.getHotelInformations().size());
            }
        }
       
        System.out.println("GETTING - END - result size: " + listOfHotels.getHotelInformations().size());        
        return listOfHotels;
    }
    
   /* public void main() throws niceview.BookHotelFault
    {
        boolean e = bookHotel(null);
    }*/
    
    public boolean bookHotel(BookHotelInputType bookHotelReqest) throws niceview.BookHotelFault {
        System.out.println("BOOKING - START");
        
        if(bookHotelReqest != null)
        {
            CreditCardInfoType ci = new CreditCardInfoType();
            CreditCardInfoType.ExpirationDate date = new CreditCardInfoType.ExpirationDate();
            date.setMonth(2);
            date.setYear(11);
            ci.setExpirationDate(date);
            ci.setName("Tick Joachim");
            ci.setNumber("50408824");
            try {
                validateCreditCard(10, ci, 100000);
            } catch (CreditCardFaultMessage e) {
                niceview.BookHotelFault exception = new niceview.BookHotelFault("msg", e.getFaultInfo().getMessage());
                throw exception;
            }
            return true;
        }
        else {
            niceview.BookHotelFault exception = new BookHotelFault("msg", "Empty");
            throw exception;
        }
        
        /* if(bookHotelReqest != null){
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

                    /* AccountType account = new AccountType();
                    account.setName(creditCardInfo.getName());
                    account.setNumber(creditCardInfo.getNumber()); 

                    CreditCardInfoType ci = new CreditCardInfoType();
                    CreditCardInfoType.ExpirationDate date = new CreditCardInfoType.ExpirationDate();
                    date.setMonth(2);
                    date.setYear(11);
                    ci.setExpirationDate(date);
                    ci.setName("Tick Joachim");
                    ci.setNumber("50408824");

                    //TODO get a real price from the booking // Useless
                    int price = 1000;

                    // TODO handle exception
                    try {
                        System.out.println("BOOKING - Trying to validate the Credit Card");
                        validateCreditCard(group, creditCardInfo, price);
                        /*try{
                            System.out.println("BOOKING - Valide Credit Card");
                            chargeCreditCard(group, creditCardInfo, price, account);                       
                            availabilityDB.replace(bookedHotel, false);
                            System.out.println("BOOKING - Hotel successfuly booked");
                        }
                        catch(CreditCardFaultMessage e){
                            System.out.println("BOOKING - ERROR - Not enough money on the bank account");
                            BookHotelFault fault = new BookHotelFault("Booking error", e.getFaultInfo().getMessage());
                            throw fault;
                        }
                    }
                    catch (CreditCardFaultMessage e){
                        System.out.println("BOOKING - ERROR - No valid card information"); 
                        BookHotelFault fault = new BookHotelFault("BookHotelFault", e.getFaultInfo().getMessage());
                        throw fault;
                    }
                }
                else{
                    System.out.println("BOOKING - No credit card guarantee needed to book the Hotel");                                
                    availabilityDB.replace(bookedHotel, false);               
                    System.out.println("BOOKING - Hotel successfuly booked");
               }        
            }           
            else { // if no matching hotel was found   
                System.out.println("BOOKING - ERROR - No hotel found for the current booking number");  
                BookHotelFault fault = new BookHotelFault("BookHotelFault", "FaultInfo");
                throw fault;
            }   
            System.out.println("BOOKING - END");
            return true;
        } 
        else { // If no input
            System.out.println("BOOKING - Null input");
            BookHotelFault fault = new BookHotelFault("BookHotelFault", "Null entry");
            throw fault;
        }*/
    }

    public void cancelHotel(java.lang.String cancelHotelRequest) throws CancelHotelFault {
        System.out.println("CANCELING - START");
 
        String bookingNumber = cancelHotelRequest;
        
        // Initialize the list of hotel we are using as a database
        InitializeDataBases();
        
        // Putting the availability of the hotel to true
        HashMap<String,HotelType> reversedHM = reverse (bookingDB);
        //if(reversedHM.get(bookingNumber) != null){
            HotelType bookedHotel = reversedHM.get(bookingNumber);
            availabilityDB.replace(bookedHotel, true);           
            System.out.println("CANCELING - SUCCESS - END");
            
        //TODO problem to reinitialize this...
        //}
        //else{
            // TODO customize error (fix problem)
            //CancelHotelFault error = null;
            //throw error;
        //} 
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

    private boolean refundCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount, AccountType account) throws CreditCardFaultMessage {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        dk.dtu.imm.fastmoney.BankPortType port = service.getBankPort();
        return port.refundCreditCard(group, creditCardInfo, amount, account);
    } 
}
