/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package niceview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.j2ee.wsdl.niceview.java.niceview.HotelType;

/**
 *
 * @author Quentin
 */
public class DataBase {
    
    // constructor used to create an Hotel        
    static private HotelType CreateHotel(String name, String address, boolean creditCardGuarantee){
        HotelType hotel = new HotelType();
        hotel.setName(name);
        hotel.setAddress(address);
        hotel.setCreditCardGuarantee(creditCardGuarantee);
        return hotel;
    }
    
    // Create a list of hotels
    static public List<HotelType> InitializeHotelList() {
        List<HotelType> list = new ArrayList<HotelType>();       
        HotelType hotel1 = CreateHotel("Hilton Hotel", "Copenhagen",true);
        HotelType hotel2 = CreateHotel("Hotel de France", "Paris", true);
        HotelType hotel3 = CreateHotel("Hotel Madrid", "Madrid", true);
        HotelType hotel4= CreateHotel("Milan Hotel", "Milan", false); 
        HotelType hotel5 = CreateHotel("NY Hotel", "Paris", false);
        list.add(hotel1);
        list.add(hotel2);
        list.add(hotel3);
        list.add(hotel4);
        list.add(hotel5);
        return list;
    }
    
    // Create a hashmap to put the hotel and a reservation number
    static public HashMap<HotelType, String> InitializeBookingHashtable(List<HotelType> listHotel){
        HashMap<HotelType, String> bookingHashtable = new  HashMap<HotelType, String>();
        for(int i=0; i < listHotel.size(); i++){
            String bookingNumber = "booking_Hotel_" + Integer.toString(i);
            bookingHashtable.put(listHotel.get(i), bookingNumber);
        }
        return bookingHashtable;
    }
   
    // Create a hasmap to put if the hotel is reserved or not, hotel is regestir using his name  
    static public HashMap<HotelType, Boolean> InitializeAvailableHotelHashtable(List<HotelType> listHotel){
        HashMap<HotelType, Boolean> availableHotels = new  HashMap<>();
        for (HotelType listHotel1 : listHotel) {
            availableHotels.put(listHotel1, true);
        }
        return availableHotels;
    }
    
    // Function used to revert hashmaps
    public static<K,V> HashMap<V,K> reverse(Map<K,V> map) {
        HashMap<V,K> rev = new HashMap<>();
        for(Map.Entry<K,V> entry : map.entrySet())
            rev.put(entry.getValue(), entry.getKey());
        return rev;
    }
    
    // Used to find the Hotel when only having the name of the hotel
    static public HashMap<String, HotelType> helpHotelHashMap(){
        HashMap<String, HotelType> hashMap = new HashMap<>();          
        List<HotelType> listHotels = InitializeHotelList();
        for (HotelType listHotel : listHotels) {
            hashMap.put(listHotel.getName(), listHotel);
        }
        return hashMap;
    }    
}
