/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType.ExpirationDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.*;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.*;
import travelgood.objects.Itinerary;
import travelgood.representations.AddToItineraryInputRepresentation;
import travelgood.representations.ItineraryOutputRepresentation;
import travelgood.representations.BookItineraryInputRepresentation;
import travelgood.representations.BookItineraryOutputRepresentation;
import travelgood.representations.CancelBookedItineraryInputRepresentation;
import travelgood.representations.FindBookedItineraryOutputRepresentation;
import travelgood.representations.FindPlannedItineraryOutputRepresentation;

/**
 *
 * @author Daniel Brand
 */
@Path("booking")
public class BookingResource {
    
    private final String baseURI = "http://localhost:8080/ws/webresources";
    
    @Path("{ID}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response findBookedItinerary(@PathParam("ID") String ID) {
         try {
            int parsedID = Integer.parseInt(ID);
            Itinerary it = Database.getBookedItinerary(parsedID);
            if (it != null) {
                
                // Create links
                List<Link> links = new ArrayList<>();

                Link.Builder builder = Link.fromMethod(ItineraryResource.class, "cancelBookedItinerary");
                builder.baseUri(baseURI);
                builder.rel("http://travelgood.ws/relations/cancelbooked");
                links.add(builder.build(ID));
                
                FindBookedItineraryOutputRepresentation rep = new FindBookedItineraryOutputRepresentation();
                rep.itinerary = it;
                rep.links = links;
                
                return Response.accepted().entity(rep).build();
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
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response bookItinerary(@PathParam("ID") String ID, BookItineraryInputRepresentation input) {
        if (input != null) {
            try {
                int parsedID = Integer.parseInt(ID);
                Itinerary it = Database.getBookedItinerary(parsedID);
                if (it != null) {
                    // Book flights
                    
                    ExpirationDate expDate = new ExpirationDate();                                             
                    expDate.setMonth(input.expirationMonth);
                    expDate.setYear(input.expirationYear);
                    CreditCardInfoType creditCard = new CreditCardInfoType();
                    creditCard.setExpirationDate(expDate);
                    creditCard.setName(input.name);
                    creditCard.setNumber(input.number);
                    
                    Database.storeCreditCard(parsedID, creditCard);
                    
                    for (String bookingNumber : it.flights.keySet()) {
                                                                                                                                         
                        BookFlightInputType bookFlightInput = new BookFlightInputType();
                        bookFlightInput.setBookingNumber(bookingNumber);
                        bookFlightInput.setCreditCard(creditCard);
                        
                        try {
                            bookFlight(bookFlightInput);
                        } catch (BookFlightFault ex) {
                            System.out.println(ex.getFaultInfo());
                            bookingCompensationLoop(parsedID, it);
                        }
                        
                        it.flights.put(bookingNumber, "confirmed");
                    }
                    // Book hotels
                    for (String bookingNumber : it.hotels.keySet()) {
                                              
                        BookHotelInputType bookHotelInput = new BookHotelInputType();
                        bookHotelInput.setBookingNumber(bookingNumber);
                        bookHotelInput.setCreditCard(creditCard);
                        
                        try {
                            bookHotel(bookHotelInput);
                        } catch (BookHotelFault ex) {
                            System.out.println(ex.getFaultInfo());
                            bookingCompensationLoop(parsedID, it);
                        }
                        
                        it.hotels.put(bookingNumber, "confirmed");
                    }

                    // Create links
                    List<Link> links = new ArrayList<>();

                    Link.Builder builder = Link.fromMethod(ItineraryResource.class, "cancelBookedItinerary");
                    builder.baseUri(baseURI);
                    builder.rel("http://travelgood.ws/relations/cancelbooked");
                    links.add(builder.build(ID));

                    builder = Link.fromMethod(ItineraryResource.class, "findBookedItinerary");
                    builder.baseUri(baseURI);
                    builder.rel("http://travelgood.ws/relations/findbooked");
                    links.add(builder.build(ID));

                    /* BookItineraryOutputRepresentation rep = new BookItineraryOutputRepresentation();
                    rep.confirmation = true;
                    rep.links = links; */
                    
                    ItineraryOutputRepresentation output = new ItineraryOutputRepresentation();
                    output.itinerary = it;
                    output.links = links;
                    
                    return Response.ok().entity(output).build();
                    //return Response.ok().entity(rep).build();
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
    
    @Path("{ID}/status")
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
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
                List<Link> links = new ArrayList<>();

                Link.Builder builder = Link.fromMethod(ItineraryResource.class, "cancelBookedItinerary");
                builder.baseUri(baseURI);
                builder.rel("http://travelgood.ws/relations/cancelbooked");
                links.add(builder.build(ID));
                
                ItineraryOutputRepresentation output = new ItineraryOutputRepresentation();
                output.itinerary = it;
                output.links = links;

                return Response.accepted().entity(output).build();
                //return Response.serverError().build();
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

            String bookingStatus = bookingStatus = it.flights.get(bookingNumber);
            if(bookingStatus == "confirmed"){
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
     
            String bookingStatus = bookingStatus = it.flights.get(bookingNumber);
            if(bookingStatus == "confirmed"){
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
