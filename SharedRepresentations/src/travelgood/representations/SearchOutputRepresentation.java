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
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author dsanz006
 */
public class SearchOutputRepresentation {
    
    public List<SearchHotelOutputRepresentation> hotelsList = new ArrayList<SearchHotelOutputRepresentation>();
    public List<SearchFlightOutputRepresentation> flightsList = new ArrayList<SearchFlightOutputRepresentation>();
    
    public class SearchHotelOutputRepresentation {
        private SearchedHotel hotel;
        private String BookingNumber;
        private Integer Price;
        private String ReservationService;
        //constructor
        public SearchHotelOutputRepresentation() {
        }
        //getters and setters
        public void sethotel(SearchedHotel hotel) {
            this.hotel = hotel;
        }
        public SearchedHotel gethotel() {
            return hotel;
        }
        public void setBookingNumber(String BookingNumber) {
            this.BookingNumber = BookingNumber;
        }
        public String getBookingNumber() {
            return BookingNumber;
        }
        public void setPrice(Integer Price) {
            this.Price = Price;
        }
        public Integer getPrice() {
            return Price;
        }
        public void setReservationService(String ReservationService) {
            this.ReservationService = ReservationService;
        }
        public String getReservationService() {
            return ReservationService;
        }
        
        public class SearchedHotel {
            private String Name;
            private String Address;
            private Boolean CreditCardGuarantee;
            //constructor
            public SearchedHotel() {
            }
            //getters and setters
            public void setName(String Name) {
                this.Name = Name;
            }
            public String getName() {
                return Name;
            }
            public void setAddress(String Address) {
                this.Address = Address;
            }
            public String getAddress() {
                return Address;
            }
            public void setCreditCardGuarantee(Boolean CreditCardGuarantee) {
                this.CreditCardGuarantee = CreditCardGuarantee;
            }
            public Boolean getCreditCardGuarantee() {
                return CreditCardGuarantee;
            }
        
        }
    }
    
    public class SearchFlightOutputRepresentation {
        private String start;
        private String destination;
        private XMLGregorianCalendar date;
        //constructor
        public SearchFlightOutputRepresentation(String start, String destination, XMLGregorianCalendar date) {
            this.start = start;
            this.destination = destination;
            this.date = date;
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
