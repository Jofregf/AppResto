package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, String> {

    public UserModel findByUserNameContainingIgnoreCase(String userName);

    public UserModel findByUserEmailContainingIgnoreCase(String email);

    public Boolean existsByUserName(String userName);

    public Boolean existsByUserEmail(String email);

    public Optional<UserModel> findByUserEmailOrUserNameContainingIgnoreCase(String email, String userName);

    public Boolean existsByRole_RoleName(String role);

}
