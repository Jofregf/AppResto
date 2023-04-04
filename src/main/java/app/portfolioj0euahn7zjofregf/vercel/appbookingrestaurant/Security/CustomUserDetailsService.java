package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.Security;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String usernameOrUserEmail) throws UsernameNotFoundException {
        UserModel user = userRepository
                .findByUserEmailOrUserNameContainingIgnoreCase(usernameOrUserEmail, usernameOrUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with that username or email " + usernameOrUserEmail));

        return CustomUserDetails.build(user);
    }

}
