package dhiabensaada.dhia.repos;

import dhiabensaada.dhia.domain.Role;
import dhiabensaada.dhia.model.NomRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Object> findByName(NomRole nomRole);
}
