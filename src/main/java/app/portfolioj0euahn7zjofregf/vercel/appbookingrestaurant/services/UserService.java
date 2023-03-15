package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserResponse;

public interface UserService {

    public UserDTO createUser(UserDTO userDTO);

    public UserResponse getUsers(int pageNumber, int pageSize);

    public UserDTO getUserById(String userId);

    public UserDTO updateUser(UserDTO userDTO, String userId);

    public void deleteUser(String userId);

    public UserDTO updateEnabled(UserDTO userDTO, String userId);
}
