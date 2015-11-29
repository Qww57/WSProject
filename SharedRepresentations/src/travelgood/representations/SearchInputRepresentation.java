/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood.representations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Dani Sanz
 */
@XmlRootElement
public class SearchInputRepresentation {
    
    public List<SearchHotelInputRepresentation> hotelsList = new ArrayList<SearchHotelInputRepresentation>();
    public List<SearchFlightInputRepresentation> flightsList = new ArrayList<SearchFlightInputRepresentation>();
    
    @XmlRootElement
    public static class SearchHotelInputRepresentation {
        private String city;
        private XMLGregorianCalendar arrivalDate;
        private XMLGregorianCalendar departureDate;
        //constructor
        public SearchHotelInputRepresentation() {
        }
        //getters and setters
        public void setCity(String city) {
            this.city = city;
        }
        public String getCity() {
            return city;
        }
        public void setArrivalDate(XMLGregorianCalendar arrivalDate) {
            this.arrivalDate = arrivalDate;
        }
        public XMLGregorianCalendar getArrivalDate() {
            return arrivalDate;
        }
        public void setDepartureDate(XMLGregorianCalendar departureDate) {
            this.departureDate = departureDate;
        }
        public XMLGregorianCalendar getDepartureDate() {
            return departureDate;
        }
    }
    
    @XmlRootElement
    public static class SearchFlightInputRepresentation {
        private String start;
        private String destination;
        private XMLGregorianCalendar date;
        //constructor
        public SearchFlightInputRepresentation() {
        }
        //getters and setters
        public void setStart(String start) {
            this.start = start;
        }
        public String getStart() {
            return start;
        }
        public void setDestination(String destination) {
            this.destination = destination;
        }
        public String getDestination() {
            return destination;
        }
        public void setDate(XMLGregorianCalendar date) {
            this.date = date;
        }
        public XMLGregorianCalendar getDate() {
            return date;
        }
    }
    
}
