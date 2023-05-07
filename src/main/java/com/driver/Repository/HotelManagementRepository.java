package com.driver.Repository;

import com.driver.model.Booking;
import com.driver.model.Hotel;
import com.driver.model.User;

import java.util.HashMap;
import java.util.List;

public class HotelManagementRepository {
    private HashMap<String , Hotel> hotelDatabase = new HashMap<>();
    private HashMap<Integer, User> userDatabase = new HashMap<>();
    private HashMap<Integer, List<Booking>> bookingDatabase = new HashMap<>();

    public HashMap<Integer, List<Booking>> getBookingDatabase() {
        return bookingDatabase;
    }

    public HashMap<Integer, User> getUserDatabase() {
        return userDatabase;
    }

    public HashMap<String, Hotel> getHotelDatabase() {
        return hotelDatabase;
    }

}
