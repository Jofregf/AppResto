package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.AdminUserDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserResponse;

public interface UserService {

    public UserResponse getUsers(int pageNumber, int pageSize, String token);

    public UserDTO getUserById(String userId, String token);

    public UserDTO updateUser(UserDTO userDTO, String userId, String token);

    public void deleteUser(String userId, String token);

    public AdminUserDTO updateEnabled(AdminUserDTO adminUserDTO, String userId, String token);

    public AdminUserDTO findByUserName(String userName, String token);

    public AdminUserDTO findByUserEmail(String email, String token);

    public AdminUserDTO updateUserRole(AdminUserDTO adminUserDTO, String userId, String token);
}
