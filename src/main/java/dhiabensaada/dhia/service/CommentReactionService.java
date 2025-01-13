package dhiabensaada.dhia.service;

import dhiabensaada.dhia.domain.Comment;
import dhiabensaada.dhia.domain.CommentReaction;
import dhiabensaada.dhia.domain.User;
import dhiabensaada.dhia.model.CommentReactionDTO;
import dhiabensaada.dhia.repos.CommentReactionRepository;
import dhiabensaada.dhia.repos.CommentRepository;
import dhiabensaada.dhia.repos.UserRepository;
import dhiabensaada.dhia.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CommentReactionService {

    private final CommentReactionRepository commentReactionRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentReactionService(final CommentReactionRepository commentReactionRepository,
            final UserRepository userRepository, final CommentRepository commentRepository) {
        this.commentReactionRepository = commentReactionRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public List<CommentReactionDTO> findAll() {
        final List<CommentReaction> commentReactions = commentReactionRepository.findAll(Sort.by("id"));
        return commentReactions.stream()
                .map(commentReaction -> mapToDTO(commentReaction, new CommentReactionDTO()))
                .toList();
    }

    public CommentReactionDTO get(final Long id) {
        return commentReactionRepository.findById(id)
                .map(commentReaction -> mapToDTO(commentReaction, new CommentReactionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CommentReactionDTO commentReactionDTO) {
        final CommentReaction commentReaction = new CommentReaction();
        mapToEntity(commentReactionDTO, commentReaction);
        return commentReactionRepository.save(commentReaction).getId();
    }

    public void update(final Long id, final CommentReactionDTO commentReactionDTO) {
        final CommentReaction commentReaction = commentReactionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(commentReactionDTO, commentReaction);
        commentReactionRepository.save(commentReaction);
    }

    public void delete(final Long id) {
        commentReactionRepository.deleteById(id);
    }

    private CommentReactionDTO mapToDTO(final CommentReaction commentReaction,
            final CommentReactionDTO commentReactionDTO) {
        commentReactionDTO.setId(commentReaction.getId());
        commentReactionDTO.setReaction(commentReaction.getReaction());
        commentReactionDTO.setCreatedAt(commentReaction.getCreatedAt());
        commentReactionDTO.setUser(commentReaction.getUser() == null ? null : commentReaction.getUser().getId());
        commentReactionDTO.setComment(commentReaction.getComment() == null ? null : commentReaction.getComment().getId());
        return commentReactionDTO;
    }

    private CommentReaction mapToEntity(final CommentReactionDTO commentReactionDTO,
            final CommentReaction commentReaction) {
        commentReaction.setReaction(commentReactionDTO.getReaction());
        commentReaction.setCreatedAt(commentReactionDTO.getCreatedAt());
        final User user = commentReactionDTO.getUser() == null ? null : userRepository.findById(commentReactionDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        commentReaction.setUser(user);
        final Comment comment = commentReactionDTO.getComment() == null ? null : commentRepository.findById(commentReactionDTO.getComment())
                .orElseThrow(() -> new NotFoundException("comment not found"));
        commentReaction.setComment(comment);
        return commentReaction;
    }

}
