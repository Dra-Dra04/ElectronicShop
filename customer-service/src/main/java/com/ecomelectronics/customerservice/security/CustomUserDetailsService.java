package com.ecomelectronics.customerservice.security;

import com.ecomelectronics.customerservice.model.UserAccount;
import com.ecomelectronics.customerservice.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserAccountRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ở đây username chính là email
        UserAccount user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // .roles() sẽ tự thêm prefix "ROLE_"
        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())   // ADMIN / CUSTOMER -> ROLE_ADMIN / ROLE_CUSTOMER
                .disabled(!Boolean.TRUE.equals(user.getActive()))
                .build();
    }
}
