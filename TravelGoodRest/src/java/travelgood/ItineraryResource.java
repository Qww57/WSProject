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
    public CreateItineraryRepresentation createItinerary() {
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
        return rep;
    }
    
    @Path("itinerary/{ID}")
    @POST
    public Itinerary addToItinerary(@PathParam("ID") String ID) {
        return null;
    }
    
    @Path("itinerary/{ID}/cancel")
    @GET
    public boolean cancelItinerary(@PathParam("ID") String ID) {
        try {
            return Database.removeItinerary(Integer.parseInt(ID));
            
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
