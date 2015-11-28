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
 * @author dsanz006
 */
@XmlRootElement
public class SearchOutputRepresentation extends Representation {
    
    public List<SearchHotelOutputRepresentation> hotelsList = new ArrayList<SearchHotelOutputRepresentation>();
    public List<SearchFlightOutputRepresentation> flightsList = new ArrayList<SearchFlightOutputRepresentation>();
    
    @XmlRootElement
    public static class SearchHotelOutputRepresentation {
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
        //class for hotel
        @XmlRootElement
        public static class SearchedHotel {
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
    
    @XmlRootElement
    public static class SearchFlightOutputRepresentation {
        private String BookingNumber;
        private Integer Price;
        private String AirlineReservationService;
        private SearchedFlight Flight;
        //constructor
        public SearchFlightOutputRepresentation() {
        }
        //getters and setters
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
        public void setAirlineReservationService(String AirlineReservationService) {
            this.AirlineReservationService = AirlineReservationService;
        }
        public String getAirlineReservationService() {
            return AirlineReservationService;
        }
        public void setFlight(SearchedFlight Flight) {
            this.Flight = Flight;
        }
        public SearchedFlight getFlight() {
            return Flight;
        }
        //class for flight
        @XmlRootElement
        public static class SearchedFlight {
            private String Start;
            private String Destination;
            private XMLGregorianCalendar StartDateTime;
            private XMLGregorianCalendar DestinationDateTime;
            private String Carrier;
            //constructor
            public SearchedFlight() {
            }
            //getters and setters
            public void setStart(String Start) {
                this.Start = Start;
            }
            public String getStart() {
                return Start;
            }
            public void setDestination(String Destination) {
                this.Destination = Destination;
            }
            public String getDestination() {
                return Destination;
            }
            public void setStartDateTime(XMLGregorianCalendar StartDateTime) {
                this.StartDateTime = StartDateTime;
            }
            public XMLGregorianCalendar getStartDateTime() {
                return StartDateTime;
            }
            public void setDestinationDateTime(XMLGregorianCalendar DestinationDateTime) {
                this.DestinationDateTime = DestinationDateTime;
            }
            public XMLGregorianCalendar getDestinationDateTime() {
                return DestinationDateTime;
            }
            public void setCarrier(String Carrier) {
                this.Carrier = Carrier;
            }
            public String getCarrier() {
                return Carrier;
            }
        }
    }
}
