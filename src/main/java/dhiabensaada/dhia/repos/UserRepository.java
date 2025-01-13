package dhiabensaada.dhia.repos;

import dhiabensaada.dhia.domain.Role;
import dhiabensaada.dhia.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

    User findFirstByRole(Role role);

}
