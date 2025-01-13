package dhiabensaada.dhia.repos;

import dhiabensaada.dhia.domain.Comment;
import dhiabensaada.dhia.domain.CommentReaction;
import dhiabensaada.dhia.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {

    CommentReaction findFirstByUser(User user);

    CommentReaction findFirstByComment(Comment comment);

}
