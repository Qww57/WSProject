/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import travelgood.objects.Itinerary;
import travelgood.representations.*;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.GetFlightsInputType;
import org.netbeans.j2ee.wsdl.lameduckws.lameduckws.lameduck.GetFlightsOutputType;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.GetHotelInputType;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.GetHotelsOutputType;

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
            Integer i = 0;
            for (SearchInputRepresentation.SearchHotelInputRepresentation hotel : hotelsList) {
                //prepare datafor niceview
                GetHotelInputType inputHotelForNiceView = new GetHotelInputType();
                inputHotelForNiceView.setArrivalDate(hotel.getArrivalDate());
                inputHotelForNiceView.setDepartureDate(hotel.getDepartureDate());
                inputHotelForNiceView.setCity(hotel.getCity());
                //gethotel operation of niceview
                GetHotelsOutputType outputHotelFromNiceView = getHotels(inputHotelForNiceView);
                SearchOutputRepresentation.SearchHotelOutputRepresentation searchedHotelInformation = new SearchOutputRepresentation.SearchHotelOutputRepresentation();
                SearchOutputRepresentation.SearchHotelOutputRepresentation.SearchedHotel searchedHotelItem = new SearchOutputRepresentation.SearchHotelOutputRepresentation.SearchedHotel();
                searchedHotelItem.setName(outputHotelFromNiceView.getHotelInformations().get(i).getHotel().getName());
                searchedHotelItem.setAddress(outputHotelFromNiceView.getHotelInformations().get(i).getHotel().getAddress());
                searchedHotelItem.setCreditCardGuarantee(outputHotelFromNiceView.getHotelInformations().get(i).getHotel().isCreditCardGuarantee());
                searchedHotelInformation.sethotel(searchedHotelItem);
                searchedHotelInformation.setBookingNumber(outputHotelFromNiceView.getHotelInformations().get(i).getBookingNumber());
                searchedHotelInformation.setPrice(outputHotelFromNiceView.getHotelInformations().get(i).getPrice());
                searchedHotelInformation.setReservationService(outputHotelFromNiceView.getHotelInformations().get(i).getReservationService());
                //add object to output list
                outputHotelsList.add(searchedHotelInformation);
                i++;
            }
        }
        if(flightsList != null) {
            Integer j = 0;
            for (SearchInputRepresentation.SearchFlightInputRepresentation flight : flightsList) {
                //prepare datafor lameduck
                GetFlightsInputType inputFlightForLameDuck = new GetFlightsInputType();
                inputFlightForLameDuck.setDate(flight.getDate());
                inputFlightForLameDuck.setDestination(flight.getDestination());
                inputFlightForLameDuck.setStart(flight.getStart());
                //getflight operation of lameduck
                GetFlightsOutputType outputFlightFromLameDuck = getFlights(inputFlightForLameDuck);
                SearchOutputRepresentation.SearchFlightOutputRepresentation searchedFlightInformation = new SearchOutputRepresentation.SearchFlightOutputRepresentation();
                SearchOutputRepresentation.SearchFlightOutputRepresentation.SearchedFlight searchedFlightItem = new SearchOutputRepresentation.SearchFlightOutputRepresentation.SearchedFlight();
                searchedFlightItem.setStart(outputFlightFromLameDuck.getFlightInformations().get(j).getFlight().getStart());
                searchedFlightItem.setStartDateTime(outputFlightFromLameDuck.getFlightInformations().get(j).getFlight().getStartDateTime());
                searchedFlightItem.setDestination(outputFlightFromLameDuck.getFlightInformations().get(j).getFlight().getDestination());
                searchedFlightItem.setDestinationDateTime(outputFlightFromLameDuck.getFlightInformations().get(j).getFlight().getDestinationDateTime());
                searchedFlightItem.setCarrier(outputFlightFromLameDuck.getFlightInformations().get(j).getFlight().getCarrier());
                searchedFlightInformation.setFlight(searchedFlightItem);
                searchedFlightInformation.setAirlineReservationService(outputFlightFromLameDuck.getFlightInformations().get(j).getAirlineReservationService());
                searchedFlightInformation.setBookingNumber(outputFlightFromLameDuck.getFlightInformations().get(j).getBookingNumber());
                searchedFlightInformation.setPrice(outputFlightFromLameDuck.getFlightInformations().get(j).getPrice());
                //add object to output list
                outputFlightsList.add(searchedFlightInformation);
                j++;
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
