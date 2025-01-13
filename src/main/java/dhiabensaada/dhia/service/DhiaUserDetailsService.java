package dhiabensaada.dhia.service;

import dhiabensaada.dhia.domain.User;
import dhiabensaada.dhia.model.DhiaUserDetails;
import dhiabensaada.dhia.model.NomRole;
import dhiabensaada.dhia.repos.UserRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class DhiaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DhiaUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public DhiaUserDetails loadUserByUsername(final String username) {
        final User user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            log.warn("user not found: {}", username);
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        final String role = "client".equals(username) ? NomRole.CLIENT.name() : NomRole.ADMIN.name();
        final List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        return new DhiaUserDetails(user.getId(), username, user.getPassword(), authorities);
    }

}
