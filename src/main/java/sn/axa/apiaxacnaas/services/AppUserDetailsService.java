package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.repositories.UserRepository;


@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User existingUser = userRepository.findByEmailAndIsActiveTrue(email).orElseThrow(()->new UsernameNotFoundException("Compte désactivé ou inexistant"));
        // Convert user roles to GrantedAuthority
        return new UserDetailsImpl(existingUser);
    }


}