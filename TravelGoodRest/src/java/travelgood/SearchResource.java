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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Response search(SearchInputRepresentation input) {
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
                SearchOutputRepresentation.SearchHotelOutputRepresentation searchedHotelInformation = new SearchOutputRepresentation().new SearchHotelOutputRepresentation();
                SearchOutputRepresentation.SearchHotelOutputRepresentation.SearchedHotel searchedHotelItem = searchedHotelInformation.new SearchedHotel();
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
            //TODO
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
}
