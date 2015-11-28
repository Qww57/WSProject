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
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import travelgood.representations.*;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.GetFlightsInputType;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.GetFlightsOutputType;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.FlightInformationType;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.GetHotelInputType;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.GetHotelsOutputType;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.HotelInformationType;

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
    public Response search(SearchInputRepresentation input) throws DatatypeConfigurationException{
        //starting print
        System.out.println("Stariting to search now!!");
        List<SearchInputRepresentation.SearchHotelInputRepresentation> hotelsList = input.hotelsList;
        List<SearchInputRepresentation.SearchFlightInputRepresentation> flightsList = input.flightsList;
        //output stuff
        List<SearchOutputRepresentation.SearchHotelOutputRepresentation> outputHotelsList = new ArrayList<SearchOutputRepresentation.SearchHotelOutputRepresentation>();
        List<SearchOutputRepresentation.SearchFlightOutputRepresentation> outputFlightsList = new ArrayList<SearchOutputRepresentation.SearchFlightOutputRepresentation>();
        SearchOutputRepresentation outputList = new SearchOutputRepresentation();
        if (hotelsList != null) {
            for (SearchInputRepresentation.SearchHotelInputRepresentation hotel : hotelsList) {
                //prepare datafor niceview
                GetHotelInputType inputHotelForNiceView = new GetHotelInputType();
                inputHotelForNiceView.setArrivalDate(hotel.getArrivalDate());
                inputHotelForNiceView.setDepartureDate(hotel.getDepartureDate());
                inputHotelForNiceView.setCity(hotel.getCity());
                //gethotel operation of niceview
                GetHotelsOutputType outputHotelFromNiceView = getHotels(inputHotelForNiceView);
                SearchOutputRepresentation.SearchHotelOutputRepresentation searchedHotelsInformations = new SearchOutputRepresentation.SearchHotelOutputRepresentation();
                for (HotelInformationType hotelInformationFromNiceView : outputHotelFromNiceView.getHotelInformations()) {
                    SearchOutputRepresentation.SearchHotelOutputRepresentation.SearchHotelInformationOutputRepresentation searchedHotelInformation = new SearchOutputRepresentation.SearchHotelOutputRepresentation.SearchHotelInformationOutputRepresentation();
                    SearchOutputRepresentation.SearchHotelOutputRepresentation.SearchHotelInformationOutputRepresentation.SearchedHotel searchedHotel = new SearchOutputRepresentation.SearchHotelOutputRepresentation.SearchHotelInformationOutputRepresentation.SearchedHotel();
                    searchedHotel.setName(hotelInformationFromNiceView.getHotel().getName());
                    searchedHotel.setAddress(hotelInformationFromNiceView.getHotel().getAddress());
                    searchedHotel.setCreditCardGuarantee(hotelInformationFromNiceView.getHotel().isCreditCardGuarantee());
                    searchedHotelInformation.sethotel(searchedHotel);
                    searchedHotelInformation.setBookingNumber(hotelInformationFromNiceView.getBookingNumber());
                    searchedHotelInformation.setPrice(hotelInformationFromNiceView.getPrice());
                    searchedHotelInformation.setReservationService(hotelInformationFromNiceView.getReservationService());
                    //add hotelInformation to list of hotelsInformations
                    searchedHotelsInformations.hotelsInformationList.add(searchedHotelInformation);
                    //add dates of booking numbers to database
                    Database.addHotelDate(hotelInformationFromNiceView.getBookingNumber(), hotel.getArrivalDate());
                }
                //add object to output list of hotels
                outputHotelsList.add(searchedHotelsInformations);
            }
        }
        if(flightsList != null) {
            for (SearchInputRepresentation.SearchFlightInputRepresentation flight : flightsList) {
                //prepare datafor lameduck
                GetFlightsInputType inputFlightForLameDuck = new GetFlightsInputType();
                inputFlightForLameDuck.setDate(flight.getDate());
                inputFlightForLameDuck.setDestination(flight.getDestination());
                inputFlightForLameDuck.setStart(flight.getStart());
                //getflight operation of lameduck
                GetFlightsOutputType outputFlightFromLameDuck = getFlights(inputFlightForLameDuck);
                
                SearchOutputRepresentation.SearchFlightOutputRepresentation searchedFlightsInformations = new SearchOutputRepresentation.SearchFlightOutputRepresentation();
                for (FlightInformationType flightInformationFromLameDuck : outputFlightFromLameDuck.getFlightInformations()) {
                    SearchOutputRepresentation.SearchFlightOutputRepresentation.SearchFlightInformationOutputRepresentation searchedFlightInformation = new SearchOutputRepresentation.SearchFlightOutputRepresentation.SearchFlightInformationOutputRepresentation();
                    SearchOutputRepresentation.SearchFlightOutputRepresentation.SearchFlightInformationOutputRepresentation.SearchedFlight searchedFlight = new SearchOutputRepresentation.SearchFlightOutputRepresentation.SearchFlightInformationOutputRepresentation.SearchedFlight();
                    searchedFlight.setCarrier(flightInformationFromLameDuck.getFlight().getCarrier());
                    searchedFlight.setDestination(flightInformationFromLameDuck.getFlight().getDestination());
                    searchedFlight.setDestinationDateTime(flightInformationFromLameDuck.getFlight().getDestinationDateTime());
                    searchedFlight.setStart(flightInformationFromLameDuck.getFlight().getStart());
                    searchedFlight.setStartDateTime(flightInformationFromLameDuck.getFlight().getStartDateTime());
                    searchedFlightInformation.setFlight(searchedFlight);
                    searchedFlightInformation.setAirlineReservationService(flightInformationFromLameDuck.getAirlineReservationService());
                    searchedFlightInformation.setBookingNumber(flightInformationFromLameDuck.getBookingNumber());
                    searchedFlightInformation.setPrice(flightInformationFromLameDuck.getPrice());
                    //add flightInformation to list of flightInformations
                    searchedFlightsInformations.flightsInformationList.add(searchedFlightInformation);
                    //add dates of booking numbers to database
                    Database.addFlightDate(flightInformationFromLameDuck.getBookingNumber(), flight.getDate());
                }                
                //add object to output list
                outputFlightsList.add(searchedFlightsInformations);
            }    
        }
        //prepare output
        outputList.hotelsList = outputHotelsList;
        outputList.flightsList = outputFlightsList;
        //return!!
        return Response.accepted().entity(outputList).build();
    }


    private static GetHotelsOutputType getHotels(org.netbeans.j2ee.wsdl.niceview.java.niceview.GetHotelInputType hotelsRequest) {
        org.netbeans.j2ee.wsdl.niceview.java.niceview.NiceViewService service = new org.netbeans.j2ee.wsdl.niceview.java.niceview.NiceViewService();
        org.netbeans.j2ee.wsdl.niceview.java.niceview.NiceViewPortType port = service.getNiceViewBindingPort();
        return port.getHotels(hotelsRequest);
    }

    private static GetFlightsOutputType getFlights(org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.GetFlightsInputType getFlightsInput) {
        org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.LameDuckService service = new org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.LameDuckService();
        org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.LameDuckPortType port = service.getLameDuckBindingPort();
        return port.getFlights(getFlightsInput);
    }
}
