/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import travelgood.objects.Itinerary;
import travelgood.representations.AddToItineraryInputRepresentation;
import travelgood.representations.ItineraryOutputRepresentation;
import travelgood.representations.CreateItineraryRepresentation;
import travelgood.representations.FindPlannedItineraryOutputRepresentation;

/**
 *
 * @author Daniel Brand
 */
@Path("itinerary")
public class ItineraryResource {
    
    private final String baseURI = "http://localhost:8080/ws/webresources";
    
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response createItinerary() {
        int ID = Database.createItinerary();
        
        List<Link> links = new ArrayList<>();
        
        Link.Builder builder = Link.fromMethod(ItineraryResource.class, "addToItinerary");
        builder.baseUri(baseURI);
        builder.rel("http://travelgood.ws/relations/add");
        links.add(builder.build(ID));
        
        builder = Link.fromMethod(ItineraryResource.class, "cancelPlannedItinerary");
        builder.baseUri(baseURI);
        builder.rel("http://travelgood.ws/relations/cancelplanned");
        links.add(builder.build(ID));
        
        CreateItineraryRepresentation rep = new CreateItineraryRepresentation();
        rep.links = links;
        rep.ID = ID;
        return Response.ok(rep).build();
    }
    
    @Path("{ID}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response findPlannedItinerary(@PathParam("ID") String ID) {
        try {
            int parsedID = Integer.parseInt(ID);
            Itinerary it = Database.getPlannedItinerary(parsedID);
            if (it != null) {
                
                // Create links
                List<Link> links = new ArrayList<>();
                
                Link.Builder builder = Link.fromMethod(ItineraryResource.class, "addToItinerary");
                builder.baseUri(baseURI);
                builder.rel("http://travelgood.ws/relations/add");
                links.add(builder.build(ID));

                builder = Link.fromMethod(ItineraryResource.class, "cancelPlannedItinerary");
                builder.baseUri(baseURI);
                builder.rel("http://travelgood.ws/relations/cancelplanned");
                links.add(builder.build(ID));
                
                if (!it.flights.isEmpty() && !it.hotels.isEmpty()) {
                    builder = Link.fromMethod(BookingResource.class, "bookItinerary");
                    builder.baseUri(baseURI);
                    builder.rel("http://travelgood.ws/relations/book");
                    links.add(builder.build(ID));
                }
                
                FindPlannedItineraryOutputRepresentation rep = new FindPlannedItineraryOutputRepresentation();
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
                List<Link> links = new ArrayList<>();
                
                Link.Builder builder = Link.fromMethod(ItineraryResource.class, "findPlannedItinerary");
                builder.baseUri(baseURI);
                builder.rel("http://travelgood.ws/relations/findplanned");
                links.add(builder.build(ID));
                
                builder = Link.fromMethod(ItineraryResource.class, "addToItinerary");
                builder.baseUri(baseURI);
                builder.rel("http://travelgood.ws/relations/add");
                links.add(builder.build(ID));

                builder = Link.fromMethod(ItineraryResource.class, "cancelPlannedItinerary");
                builder.baseUri(baseURI);
                builder.rel("http://travelgood.ws/relations/cancelplanned");
                links.add(builder.build(ID));
                
                builder = Link.fromMethod(BookingResource.class, "bookItinerary");
                builder.baseUri(baseURI);
                builder.rel("http://travelgood.ws/relations/book");
                links.add(builder.build(ID));
                
                ItineraryOutputRepresentation rep = new ItineraryOutputRepresentation();
                rep.itinerary = it;
                rep.links = links;
                
                return Response.ok().entity(rep).build();
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
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
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
