package ru.tsystems.tsproject.sbb.secure.adapters;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.tsproject.sbb.database.entity.User;
import ru.tsystems.tsproject.sbb.database.repositories.UserRepository;
import ru.tsystems.tsproject.sbb.services.UserService;

import javax.inject.Inject;

/**
 * Created by apple on 19.11.14.
 */
@Service("userDetailsService")
@Transactional(readOnly = true)
public class UserDetailsServiceAdapter implements UserDetailsService {
    @Inject UserService userService;
    @Inject UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(s);

        if (user == null) {
            throw new UsernameNotFoundException("No such user: " + s);
        }

        return new UserDetailsAdapter(user);
    }
}
