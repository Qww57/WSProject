/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType.ExpirationDate;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.*;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.*;
import travelgood.objects.Itinerary;
import travelgood.objects.LinkRelatives;
import travelgood.representations.*;

/**
 *
 * @author Quentin, Daniel Brand, Rustam
 */
@Path("booking")
public class BookingResource {
    
    private final String baseURI = "http://localhost:8080/ws/webresources/booking/";
    
    @Path("{ID}")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response findBookedItinerary(@PathParam("ID") String ID) {
         try {
            int parsedID = Integer.parseInt(ID);
            Itinerary it = Database.getBookedItinerary(parsedID);
            if (it != null) {
                
                // Create links
                Response.ResponseBuilder responseBuilder = Response.accepted();

                Link.Builder linkBuilder = Link.fromMethod(BookingResource.class, "cancelBookedItinerary");
                linkBuilder.baseUri(baseURI);
                linkBuilder.rel(LinkRelatives.CANCEL_BOOKED_ITINERARY);
                responseBuilder.links(linkBuilder.build(ID));
                
                ItineraryOutputRepresentation rep = new ItineraryOutputRepresentation();
                rep.itinerary = it;
                
                return responseBuilder.entity(rep).build();
            }
            else {
                return Response.status(Response.Status.NOT_FOUND).
                        entity("Itinerary with ID " + ID + " was not found.").
                        build();
            }
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("ID is malformed. Must be numbers only.").
                    build();
        }
    }
    
    @Path("{ID}")
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response bookItinerary(@PathParam("ID") String ID, BookItineraryInputRepresentation input) {
        if (input != null) {
            try {
                int parsedID = Integer.parseInt(ID);
                Itinerary it = Database.getPlannedItinerary(parsedID);
                if (it != null) {
                                   
                    ExpirationDate expDate = new ExpirationDate();                                             
                    expDate.setMonth(input.expirationMonth);
                    expDate.setYear(input.expirationYear);
                    CreditCardInfoType creditCard = new CreditCardInfoType();
                    creditCard.setExpirationDate(expDate);
                    creditCard.setName(input.name);
                    creditCard.setNumber(input.number);
                    
                    Database.storeCreditCard(parsedID, creditCard);
                    
                    boolean booking = true;
                    BookFlightInputType bookFlightInput = new BookFlightInputType();
                    BookHotelInputType bookHotelInput = new BookHotelInputType();
                    
                    // Book hotels                
                    for (String bookingNumber : it.hotels.keySet()) {
                                              
                        bookHotelInput.setBookingNumber(bookingNumber);
                        bookHotelInput.setCreditCard(creditCard);
                        
                        if (booking == true){
                            try {
                                bookHotel(bookHotelInput);
                                it.hotels.put(bookingNumber, "confirmed");
                            } catch (BookHotelFault ex) {
                                System.out.println(ex.getFaultInfo());
                                bookingCompensationLoop(parsedID, it);
                                booking = false;
                            }
                        }                      
                    }
                    
                    // Booking flight first
                    for (String bookingNumber : it.flights.keySet()) {
                                                                                                                                         
                        bookFlightInput.setBookingNumber(bookingNumber);
                        bookFlightInput.setCreditCard(creditCard);
                        
                        if (booking == true){
                            try {
                                bookFlight(bookFlightInput);
                                 it.flights.put(bookingNumber, "confirmed");
                            } catch (BookFlightFault ex) {
                                System.out.println(ex.getFaultInfo());
                                bookingCompensationLoop(parsedID, it);
                                booking = false;
                            }
                        }                       
                    }                                     
                    
                    // Move itinerary to booked database
                    Database.moveItineraryToBooked(parsedID);

                    // Create links
                    Response.ResponseBuilder responseBuilder = Response.ok();

                    Link.Builder linkBuilder = Link.fromMethod(BookingResource.class, "cancelBookedItinerary");
                    linkBuilder.baseUri(baseURI);
                    linkBuilder.rel(LinkRelatives.CANCEL_BOOKED_ITINERARY);
                    responseBuilder.links(linkBuilder.build(ID));

                    linkBuilder = Link.fromMethod(BookingResource.class, "findBookedItinerary");
                    linkBuilder.baseUri(baseURI);
                    linkBuilder.rel(LinkRelatives.FIND_BOOKED_ITINERARY);
                    responseBuilder.links(linkBuilder.build(ID));
                    
                    ItineraryOutputRepresentation output = new ItineraryOutputRepresentation();
                    output.itinerary = it;
                    
                    return responseBuilder.entity(output).build();
                }
                else {
                    return Response.status(Response.Status.NOT_FOUND).
                            entity("Itinerary with ID " + ID + " was not found.").
                            build();
                }
            } catch (NumberFormatException e) {
                return Response.status(Response.Status.BAD_REQUEST).
                        entity("ID is malformed. Must be numbers only.").
                        build();
            }
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("No booking  numbers defined.").
                    build();
        }
    }
    
    @Path("{ID}/cancel")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response cancelBookedItinerary(@PathParam("ID") String ID) {
        try {
            int parsedID = Integer.parseInt(ID);
            Itinerary it = Database.getBookedItinerary(parsedID);
            if (it != null) {

                CreditCardInfoType creditCard = Database.getBookingCreditCard(parsedID);

                // Cancel flights
                for (String bookingNumber : it.flights.keySet()) {                       

                    CancelFlightInputType cancelFlightInput = new CancelFlightInputType();
                    cancelFlightInput.setBookingNumber(bookingNumber);
                    cancelFlightInput.setCreditCard(creditCard);

                    try {
                        cancelFlight(cancelFlightInput);
                        it.flights.replace(bookingNumber, "cancelled");                            
                    } catch (CancelFlightFault ex) {
                        // if failure, flight remains confirmed
                        System.out.println(ex.getFaultInfo());
                    }                       
                }

                // Cancel hotels
                for (String bookingNumber : it.hotels.keySet()) {                      

                    try {
                        cancelHotel(bookingNumber);
                        it.hotels.replace(bookingNumber, "cancelled");
                    } catch (CancelHotelFault ex) {
                        // if failure, hotel remains confirmed
                        System.out.println(ex.getFaultInfo());
                    }                       
                }

                // Create links
                Response.ResponseBuilder responseBuilder = Response.accepted();

                Link.Builder linkBuilder = Link.fromMethod(BookingResource.class, "findBookedItinerary");
                linkBuilder.baseUri(baseURI);
                linkBuilder.rel(LinkRelatives.FIND_BOOKED_ITINERARY);
                responseBuilder.links(linkBuilder.build(ID));
                
                ItineraryOutputRepresentation output = new ItineraryOutputRepresentation();
                output.itinerary = it;

                return responseBuilder.entity(output).build();
            }
            else {
                return Response.status(Response.Status.NOT_FOUND).
                        entity("Itinerary with ID " + ID + " was not found.").
                        build();
            }
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("ID is malformed. Must be numbers only.").
                    build();
        }
    }
    
    private void bookingCompensationLoop(int parsedID, Itinerary it){
        CreditCardInfoType creditCard = Database.getBookingCreditCard(parsedID);

        // Cancel flights
        for (String bookingNumber : it.flights.keySet()) {                       

            String bookingStatus = it.flights.get(bookingNumber);
            if(bookingStatus.equals("confirmed")){
                CancelFlightInputType cancelFlightInput = new CancelFlightInputType();
                cancelFlightInput.setBookingNumber(bookingNumber);
                cancelFlightInput.setCreditCard(creditCard);

                try {
                    cancelFlight(cancelFlightInput);
                    it.flights.replace(bookingNumber, "cancelled");                            
                } catch (CancelFlightFault ex) {
                    // if failure, flight remains confirmed 
                }            
            }                             
        }

        // Cancel hotels
        for (String bookingNumber : it.hotels.keySet()) {                      
     
            String bookingStatus = it.hotels.get(bookingNumber);
            if(bookingStatus.equals("confirmed")){
                try {
                    cancelHotel(bookingNumber);
                    it.hotels.replace(bookingNumber, "cancelled");
                } catch (CancelHotelFault ex) {
                    // if failure, hotel remains confirmed
                }                       
            }
        }
    }

    private static boolean bookFlight(org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.BookFlightInputType bookFlightInput) throws BookFlightFault {
        org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.LameDuckService service = new org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.LameDuckService();
        org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.LameDuckPortType port = service.getLameDuckBindingPort();
        return port.bookFlight(bookFlightInput);
    }

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

    private static boolean cancelFlight(org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.CancelFlightInputType cancelFlightInput) throws CancelFlightFault {
        org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.LameDuckService service = new org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.LameDuckService();
        org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.LameDuckPortType port = service.getLameDuckBindingPort();
        return port.cancelFlight(cancelFlightInput);
    }
}
