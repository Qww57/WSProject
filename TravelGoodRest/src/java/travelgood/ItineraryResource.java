/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.xml.namespace.QName;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.GetFlightsInputType;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.GetFlightsOutputType;
import travelgood.representations.AddToItineraryInputRepresentation;
import travelgood.representations.CreateItineraryRepresentation;

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
        builder.rel("http://createitinerary.ws/relations/add");
        links.add(builder.build(ID));
        
        builder = Link.fromMethod(ItineraryResource.class, "cancelItinerary");
        builder.baseUri(baseURI);
        builder.rel("http://createitinerary.ws/relations/cancel");
        links.add(builder.build(ID));
        
        builder = Link.fromResource(SearchResource.class);
        builder.baseUri(baseURI);
        builder.rel("http://createitinerary.ws/relations/search");
        links.add(builder.build());
        
        CreateItineraryRepresentation rep = new CreateItineraryRepresentation();
        rep.links = links;
        rep.ID = ID;
        return Response.ok(rep).build();
    }
    
    @Path("itinerary/{ID}")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response addToItinerary(@PathParam("ID") String ID, AddToItineraryInputRepresentation input) {
        int parsedID;
        if (input != null) {
            try {
            parsedID = Integer.parseInt(ID);
            Itinerary it = Database.getItinerary(parsedID);
            if (it != null) {
                for (String bookingnumber : input.flight_booking_number) {
                    it.addFlight(bookingnumber);
                }
                for (String bookingnumber : input.hotel_booking_numbers) {
                    it.addHotel(bookingnumber);
                }
                return Response.accepted().entity(it).build();
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
    public Response cancelItinerary(@PathParam("ID") String ID) {
        try {
            boolean success = Database.removeItinerary(Integer.parseInt(ID));
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
