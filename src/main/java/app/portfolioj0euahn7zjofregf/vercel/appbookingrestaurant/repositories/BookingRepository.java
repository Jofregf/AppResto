package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingModel, String> {
}
