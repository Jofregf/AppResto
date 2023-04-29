package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.AdminUserDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserResponse;

import java.util.Optional;

public interface UserService {

    public UserResponse getUsers(int pageNumber, int pageSize, String token);

    public UserDTO getUserById(String token);

    public UserDTO updateUser(UserDTO userDTO, String token);

    public void deleteUser(String token);

    public AdminUserDTO updateEnabled(AdminUserDTO adminUserDTO, String userId, String token);

    public AdminUserDTO updateUserRole(AdminUserDTO adminUserDTO, String usernameOrUserEmail, String token);

    public Optional<AdminUserDTO> findByUserNameOrEmail(String userNameOrEmail, String token);
}
