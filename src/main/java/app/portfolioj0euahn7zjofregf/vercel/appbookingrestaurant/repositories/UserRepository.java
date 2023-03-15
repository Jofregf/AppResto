package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, String> {
}
