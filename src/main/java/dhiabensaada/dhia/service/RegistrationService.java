package dhiabensaada.dhia.service;

import dhiabensaada.dhia.domain.Role;
import dhiabensaada.dhia.domain.User;
import dhiabensaada.dhia.model.NomRole;
import dhiabensaada.dhia.model.RegistrationRequest;
import dhiabensaada.dhia.repos.RoleRepository;
import dhiabensaada.dhia.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static dhiabensaada.dhia.model.NomRole.ADMIN;


@Service
@Slf4j
public class RegistrationService {
    private final RoleRepository roleRepository ;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(final UserRepository userRepository,final RoleRepository roleRepository,
            final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

        this.passwordEncoder = passwordEncoder;
    }

    public void register(final RegistrationRequest registrationRequest) {
        log.info("registering new user: {}", registrationRequest.getUsername());

        final User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());
        user.setCreatedAt(registrationRequest.getCreatedAt());
        user.setUpdatedAt(registrationRequest.getUpdatedAt());
        // Assign a default role (e.g., ADMIN) to the user
// Assign a default role (e.g., ADMIN) to the user
        // Fetch the role by its ID (assuming ADMIN has id 1)
        Role defaultRole = roleRepository.findById(1L)  // Use the ID of the ADMIN role
            .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        user.setRole(defaultRole);  // Set the role

        userRepository.save(user);


    }

    public boolean usernameExists(final String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

}
