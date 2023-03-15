package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.BookingDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.BookingModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RestaurantModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;

public interface BookingService {

    public boolean createBooking(BookingDTO booking, String restaurantId, String userId);
}
