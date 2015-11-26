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
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import travelgood.representations.*;

/**
 *
 * @author Daniel & Dani Sanz
 */
@Path("search")
public class SearchResource {
    
    private final String baseURI = "http://localhost:8080/ws/webresources";
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public void search(SearchRepresentation input) {
        //return 0;
    }
}
