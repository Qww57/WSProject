/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import travelgood.objects.Itinerary;
import travelgood.objects.LinkRelatives;
import travelgood.representations.*;

/**
 *
 * @author Daniel Brand
 */
@Path("itinerary")
public class ItineraryResource {
    
    private final String baseURI = "http://localhost:8080/ws/webresources/itinerary/";
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createItinerary() {
        int ID = Database.createItinerary();
        
        ResponseBuilder responseBuilder = Response.ok();
        
        Link.Builder linkBuilder = Link.fromMethod(ItineraryResource.class, "addToItinerary");
        linkBuilder.baseUri(baseURI);
        linkBuilder.rel(LinkRelatives.ADD_TO_ITINERARY);
        responseBuilder.links(linkBuilder.build(ID));
        
        linkBuilder = Link.fromMethod(ItineraryResource.class, "findPlannedItinerary");
        linkBuilder.baseUri(baseURI);
        linkBuilder.rel(LinkRelatives.FIND_PLANNED_ITINERARY);
        responseBuilder.links(linkBuilder.build(ID));
        
        linkBuilder = Link.fromMethod(ItineraryResource.class, "cancelPlannedItinerary");
        linkBuilder.baseUri(baseURI);
        linkBuilder.rel(LinkRelatives.CANCEL_PLANNED_ITINERARY);
        responseBuilder.links(linkBuilder.build(ID));
        
        CreateItineraryRepresentation rep = new CreateItineraryRepresentation();
        rep.ID = ID;
        return responseBuilder.entity(rep).build();
    }
    
    @Path("{ID}")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response findPlannedItinerary(@PathParam("ID") String ID) {
        try {
            int parsedID = Integer.parseInt(ID);
            Itinerary it = Database.getPlannedItinerary(parsedID);
            if (it != null) {
                
                // Create links
                ResponseBuilder responseBuilder = Response.accepted();
                
                Link.Builder linkBuilder = Link.fromMethod(ItineraryResource.class, "addToItinerary");
                linkBuilder.baseUri(baseURI);
                linkBuilder.rel(LinkRelatives.ADD_TO_ITINERARY);
                responseBuilder.links(linkBuilder.build(ID));
                
                linkBuilder = Link.fromMethod(ItineraryResource.class, "cancelPlannedItinerary");
                linkBuilder.baseUri(baseURI);
                linkBuilder.rel(LinkRelatives.CANCEL_PLANNED_ITINERARY);
                responseBuilder.links(linkBuilder.build(ID));
                
                if (!it.flights.isEmpty() && !it.hotels.isEmpty()) {
                    linkBuilder = Link.fromMethod(BookingResource.class, "bookItinerary");
                    linkBuilder.baseUri("http://localhost:8080/ws/webresources/booking/");
                    linkBuilder.rel(LinkRelatives.BOOK_ITINERARY);
                    responseBuilder.links(linkBuilder.build(ID));
                }
                
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
    public Response addToItinerary(@PathParam("ID") String ID, AddToItineraryInputRepresentation input) {
        if (input != null) {
            try {
            int parsedID = Integer.parseInt(ID);
            Itinerary it = Database.getPlannedItinerary(parsedID);
            if (it != null) {
                for (String bookingnumber : input.flight_booking_number) {
                    it.addFlight(bookingnumber);
                }
                for (String bookingnumber : input.hotel_booking_numbers) {
                    it.addHotel(bookingnumber);
                }
                
                // Create links
                ResponseBuilder responseBuilder = Response.ok();
                
                Link.Builder linkBuilder = Link.fromMethod(ItineraryResource.class, "findPlannedItinerary");
                linkBuilder.baseUri(baseURI);
                linkBuilder.rel(LinkRelatives.FIND_PLANNED_ITINERARY);
                responseBuilder.links(linkBuilder.build(ID));
                
                linkBuilder = Link.fromMethod(ItineraryResource.class, "addToItinerary");
                linkBuilder.baseUri(baseURI);
                linkBuilder.rel(LinkRelatives.ADD_TO_ITINERARY);
                responseBuilder.links(linkBuilder.build(ID));

                linkBuilder = Link.fromMethod(ItineraryResource.class, "cancelPlannedItinerary");
                linkBuilder.baseUri(baseURI);
                linkBuilder.rel(LinkRelatives.CANCEL_PLANNED_ITINERARY);
                responseBuilder.links(linkBuilder.build(ID));
                
                linkBuilder = Link.fromMethod(BookingResource.class, "bookItinerary");
                linkBuilder.baseUri("http://localhost:8080/ws/webresources/booking/");
                linkBuilder.rel(LinkRelatives.BOOK_ITINERARY);
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
        else {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("No booking  numbers defined.").
                    build();
        }
        
    }
    
    @Path("{ID}/cancel")
    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response cancelPlannedItinerary(@PathParam("ID") String ID) {
        try {
            boolean success = Database.cancelPlannedItinerary(Integer.parseInt(ID));
            if (success) {
                return Response.ok().build();
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
}
