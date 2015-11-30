/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood.objects;

/**
 *
 * @author Daniel Brand
 */
public class LinkRelatives {
    // /itinerary
    // /{ID}
    public final static String ADD_TO_ITINERARY = "http://travelgood.ws/relations/add"; // POST
    public final static String FIND_PLANNED_ITINERARY ="http://travelgood.ws/relations/findplanned"; //GET
    public final static String CANCEL_PLANNED_ITINERARY = "http://travelgood.ws/relations/cancelplanned"; // GET
    
    // /booking
    // /{ID}
    public final static String BOOK_ITINERARY ="http://travelgood.ws/relations/book"; // POST
    public final static String FIND_BOOKED_ITINERARY = "http://travelgood.ws/relations/findbooked"; // GET
    // /status
    public final static String CANCEL_BOOKED_ITINERARY = "http://travelgood.ws/relations/cancelbooked"; // PUT
    
}
