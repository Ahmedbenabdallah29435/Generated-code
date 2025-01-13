package dhiabensaada.dhia.repos;

import dhiabensaada.dhia.domain.Post;
import dhiabensaada.dhia.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {

    Post findFirstByUser(User user);

}
