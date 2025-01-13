package dhiabensaada.dhia.service;

import dhiabensaada.dhia.domain.Comment;
import dhiabensaada.dhia.domain.CommentReaction;
import dhiabensaada.dhia.domain.Post;
import dhiabensaada.dhia.domain.User;
import dhiabensaada.dhia.model.CommentDTO;
import dhiabensaada.dhia.repos.CommentReactionRepository;
import dhiabensaada.dhia.repos.CommentRepository;
import dhiabensaada.dhia.repos.PostRepository;
import dhiabensaada.dhia.repos.UserRepository;
import dhiabensaada.dhia.util.NotFoundException;
import dhiabensaada.dhia.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentReactionRepository commentReactionRepository;

    public CommentService(final CommentRepository commentRepository,
            final UserRepository userRepository, final PostRepository postRepository,
            final CommentReactionRepository commentReactionRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentReactionRepository = commentReactionRepository;
    }

    public List<CommentDTO> findAll() {
        final List<Comment> comments = commentRepository.findAll(Sort.by("id"));
        return comments.stream()
                .map(comment -> mapToDTO(comment, new CommentDTO()))
                .toList();
    }

    public CommentDTO get(final Long id) {
        return commentRepository.findById(id)
                .map(comment -> mapToDTO(comment, new CommentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CommentDTO commentDTO) {
        final Comment comment = new Comment();
        mapToEntity(commentDTO, comment);
        return commentRepository.save(comment).getId();
    }

    public void update(final Long id, final CommentDTO commentDTO) {
        final Comment comment = commentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(commentDTO, comment);
        commentRepository.save(comment);
    }

    public void delete(final Long id) {
        commentRepository.deleteById(id);
    }

    private CommentDTO mapToDTO(final Comment comment, final CommentDTO commentDTO) {
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setUser(comment.getUser() == null ? null : comment.getUser().getId());
        commentDTO.setPost(comment.getPost() == null ? null : comment.getPost().getId());
        return commentDTO;
    }

    private Comment mapToEntity(final CommentDTO commentDTO, final Comment comment) {
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        final User user = commentDTO.getUser() == null ? null : userRepository.findById(commentDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        comment.setUser(user);
        final Post post = commentDTO.getPost() == null ? null : postRepository.findById(commentDTO.getPost())
                .orElseThrow(() -> new NotFoundException("post not found"));
        comment.setPost(post);
        return comment;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Comment comment = commentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final CommentReaction commentCommentReaction = commentReactionRepository.findFirstByComment(comment);
        if (commentCommentReaction != null) {
            referencedWarning.setKey("comment.commentReaction.comment.referenced");
            referencedWarning.addParam(commentCommentReaction.getId());
            return referencedWarning;
        }
        return null;
    }

}
