package ir.maktab.homeservice.services;

import ir.maktab.homeservice.entities.users.User;
import ir.maktab.homeservice.repositories.UserRepository;
import ir.maktab.homeservice.services.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        if (user==null)
            throw new UserNotFoundException();
        List<GrantedAuthority> authorities = getAuthorities(user.getRole().toString());
        return new org.springframework.security.core.userdetails.User(String.valueOf( user.getId()), user.getPassword(),authorities );
    }

    private List<GrantedAuthority> getAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        switch (role){
            case "CUSTOMER":
                authorities= getCustomerAuthorities();
                break;
            case "SPECIALIST":
                authorities= getSpecialistAuthorities();
                break;
            case "ADMIN":
                authorities= getAdminAuthorities();
        }
        return authorities;
    }

    private List<GrantedAuthority> getAdminAuthorities() {
        String[] authorities = {"can_get_customers" , "can_get_specialists" ,"can_assign_assistance" ,
                "can_get_user_requests" , "can_add_assistance" , "can_add_subassistance", "can_get_users" ,
                "can_get_report" , "can_get_requests_by_parameter",
        };
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }

    private List<GrantedAuthority> getSpecialistAuthorities() {
        String[] authorities = {"can_add_offers"  };
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }

    private List<GrantedAuthority> getCustomerAuthorities() {
        String[] authorities = {"can_add_request" , "can_evaluate" , "can_get_offers" , "can_select_offer" ,
        "can_pay_request"};
        return Arrays.stream(authorities).map( s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
    }
}
