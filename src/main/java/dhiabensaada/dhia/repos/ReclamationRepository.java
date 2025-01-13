package dhiabensaada.dhia.repos;

import dhiabensaada.dhia.domain.Reclamation;
import dhiabensaada.dhia.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {

    Reclamation findFirstByUser(User user);

}
