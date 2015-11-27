/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import travelgood.representations.BookItineraryInputRepresentation;

/**
 *
 * @author Quentin
 */
public class Constructors {
    
    public static BookItineraryInputRepresentation createBookItineraryInputRepresentation(String name, String number, int month, int year){
        BookItineraryInputRepresentation output = new BookItineraryInputRepresentation();       
        output.name = name;
        output.number = number;
        output.expirationMonth = month;
        output.expirationYear = year;
        
        return output;
    }
}
