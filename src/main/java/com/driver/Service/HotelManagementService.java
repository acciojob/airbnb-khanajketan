package com.driver.Service;

import com.driver.Repository.HotelManagementRepository;
import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HotelManagementService {

    HotelManagementRepository hotelManagementRepository = new HotelManagementRepository();


    public String addHotel(Hotel hotel) {
        if(hotel == null)return "FAILURE";
        if(hotelManagementRepository.getHotelDatabase().containsKey(hotel.getHotelName())) return "FAILURE";
        hotelManagementRepository.getHotelDatabase().put(hotel.getHotelName(), hotel);
        return "SUCCES";
    }

    public Integer addUser(User user) {
        if(user == null) return -1;
        if(hotelManagementRepository.getUserDatabase().containsKey(user.getaadharCardNo())) return -2;
        hotelManagementRepository.getUserDatabase().put(user.getaadharCardNo(), user);

        return user.getaadharCardNo();
    }

    public List<Hotel> getListOfAllHotels(){
        List<Hotel> list = new ArrayList<>();
        for(String name : hotelManagementRepository.getHotelDatabase().keySet()){
            list.add(hotelManagementRepository.getHotelDatabase().get(name));
        }
        return list;
    }

    public String getHotelWithMostFacilities() {
        List<Hotel> list = getListOfAllHotels();
        String maxFacilitiesHotel = list.get(0).getHotelName();
        for(Hotel hotel : list){
            int sizeOfmaxFacilitiesHotel = hotelManagementRepository.getHotelDatabase().get(maxFacilitiesHotel).getFacilities().size();
            int sizeOfCurrentHotelFacilities =hotel.getFacilities().size();
            if(sizeOfCurrentHotelFacilities > sizeOfmaxFacilitiesHotel) maxFacilitiesHotel = hotel.getHotelName();
            else if(sizeOfmaxFacilitiesHotel == sizeOfCurrentHotelFacilities){

                if(maxFacilitiesHotel.compareTo(hotel.getHotelName()) > 0){
                    maxFacilitiesHotel = hotel.getHotelName();

                }
            }

        }
        return maxFacilitiesHotel;
    }


    //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
    //Have bookingId as a random UUID generated String
    //save the booking Entity and keep the bookingId as a primary key
    //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
    //If there arent enough rooms available in the hotel that we are trying to book return -1
    //in other case return total amount paid
    public int bookARoom(Booking booking) {
        String bookingId = UUID.randomUUID().toString();
        // if hotel is not present in database;
        if(!hotelManagementRepository.getHotelDatabase().containsKey(booking.getHotelName()))return -1;

        // if there rooms are not available that are required
        if(hotelManagementRepository.getHotelDatabase().get(booking.getHotelName()).getAvailableRooms() < booking.getNoOfRooms()) return -1;
        Hotel hotel = hotelManagementRepository.getHotelDatabase().get(booking.getHotelName());

        // setting booking id to booking
        booking.setBookingId(bookingId);
        int amountToBePaid = booking.getNoOfRooms() * hotel.getPricePerNight();

        // setting amount to be paid in booking
        booking.setAmountToBePaid(amountToBePaid);

        // decreasing available rooms that are booked
        hotel.setAvailableRooms(hotel.getAvailableRooms() - booking.getNoOfRooms());

        if(!hotelManagementRepository.getBookingDatabase().containsKey(booking.getBookingAadharCard())){
            List<Booking> list = new ArrayList<>();
            list.add(booking);
            hotelManagementRepository.getBookingDatabase().put(booking.getBookingAadharCard(),list);
        }else{
            hotelManagementRepository.getBookingDatabase().get(booking.getBookingAadharCard()).add(booking);
        }
        return amountToBePaid;
    }
    //In this function return the bookings done by a person
    public int getBookings(Integer aadharCard) {
        //In this function return the bookings done by a person

        return hotelManagementRepository.getBookingDatabase().get(aadharCard).size();
    }
    //We are having a new facilites that a hotel is planning to bring.
    //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
    //return the final updated List of facilities and also update that in your hotelDb
    //Note that newFacilities can also have duplicate facilities possible

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        HashSet<Facility> hs = new HashSet<>();
        List<Facility> list = hotelManagementRepository.getHotelDatabase().get(hotelName).getFacilities();
        for(int i=0; i<list.size(); i++){
            hs.add(list.get(i));
        }
        for(int i=0; i<newFacilities.size(); i++){
            if(!hs.contains(newFacilities.get(i))){
                hs.add(list.get(i));
                list.add(newFacilities.get(i));
            }
        }
        hotelManagementRepository.getHotelDatabase().get(hotelName).setFacilities(list);
        return hotelManagementRepository.getHotelDatabase().get(hotelName);
    }
}
