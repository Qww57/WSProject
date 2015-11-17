/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Daniel
 */
@Path("travelgood")
public class TravelGoodResource {
    
    private static String name = "TravelGood";
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getTravelGoodName() {
        return name;
    }
    
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    public void setTravelGoodName(String name) {
        this.name = name;
    }
}
