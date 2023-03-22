package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserResponse;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.ResourceNotFoundException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.RestoAppException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    // Entity to DTO
    private UserDTO mapDTO(UserModel userModel){

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userModel.getUserId());
        userDTO.setUserName(userModel.getUserName());
        userDTO.setFirstName(userModel.getFirstName());
        userDTO.setLastName(userModel.getLastName());
        userDTO.setUserPhone(userModel.getUserPhone());
        userDTO.setUserEmail(userModel.getUserEmail());
        userDTO.setUserPassword(userModel.getUserPassword());
        userDTO.setEnabled(userModel.getEnabled());

        return userDTO;
    }

    // DTO to Entity
    private UserModel mapEntity(UserDTO userDTO){

        UserModel user = new UserModel();
        user.setUserId(userDTO.getUserId());
        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUserPhone(userDTO.getUserPhone());
        user.setUserEmail(userDTO.getUserEmail());
        user.setUserPassword(userDTO.getUserPassword());
        user.setEnabled(true);

        return user;
    }
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        UserModel user = mapEntity(userDTO);

        UserModel newUser = userRepository.save(user);

        UserDTO userResponse = mapDTO(newUser);

        return userResponse;
    }

    @Override
    public UserResponse getUsers(int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<UserModel> users = userRepository.findAll(pageable);

        List<UserModel> usersList = users.getContent();

        List<UserDTO> content = usersList.stream().map(user -> mapDTO(user)).collect(Collectors.toList());

        UserResponse userResponse = new UserResponse();
        userResponse.setContents(content);
        userResponse.setPageNumber(users.getNumber());
        userResponse.setPageSize(users.getSize());
        userResponse.setTotalElements(users.getTotalElements());
        userResponse.setTotalPages(users.getTotalPages());
        userResponse.setLast(users.isLast());

        return userResponse;
    }

    @Override
    public UserDTO getUserById(String userId) {

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        return mapDTO(user);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, String userId) {

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUserPhone(userDTO.getUserPhone());
        user.setUserEmail(userDTO.getUserEmail());
        user.setUserPassword(userDTO.getUserPassword());


        UserModel userUpdated = userRepository.save(user);

        return mapDTO(userUpdated);
    }

    @Override
    public void deleteUser(String userId) {
        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        userRepository.delete(user);
    }

    @Override
    public UserDTO updateEnabled(UserDTO userDTO, String userId) {

        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        user.setEnabled(userDTO.getEnabled());

        UserModel userUpdated = userRepository.save(user);

        return mapDTO(userUpdated);
    }
}
