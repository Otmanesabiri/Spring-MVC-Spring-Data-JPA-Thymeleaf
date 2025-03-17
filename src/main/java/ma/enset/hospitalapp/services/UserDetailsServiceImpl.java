package ma.enset.hospitalapp.services;

import ma.enset.hospitalapp.entities.AppUser;
import ma.enset.hospitalapp.repositories.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public UserDetailsServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);

        if(appUser == null) throw new UsernameNotFoundException("User not found");

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });

        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
}