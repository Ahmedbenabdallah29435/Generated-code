package dhiabensaada.dhia.repos;

import dhiabensaada.dhia.domain.Post;
import dhiabensaada.dhia.domain.PostReaction;
import dhiabensaada.dhia.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {

    PostReaction findFirstByUser(User user);

    PostReaction findFirstByPost(Post post);

}
