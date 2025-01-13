package dhiabensaada.dhia.repos;

import dhiabensaada.dhia.domain.Comment;
import dhiabensaada.dhia.domain.Post;
import dhiabensaada.dhia.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findFirstByUser(User user);

    Comment findFirstByPost(Post post);

}
