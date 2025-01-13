package dhiabensaada.dhia.service;

import dhiabensaada.dhia.domain.Comment;
import dhiabensaada.dhia.domain.CommentReaction;
import dhiabensaada.dhia.domain.Post;
import dhiabensaada.dhia.domain.PostReaction;
import dhiabensaada.dhia.domain.Reclamation;
import dhiabensaada.dhia.domain.Role;
import dhiabensaada.dhia.domain.User;
import dhiabensaada.dhia.model.UserDTO;
import dhiabensaada.dhia.repos.CommentReactionRepository;
import dhiabensaada.dhia.repos.CommentRepository;
import dhiabensaada.dhia.repos.PostReactionRepository;
import dhiabensaada.dhia.repos.PostRepository;
import dhiabensaada.dhia.repos.ReclamationRepository;
import dhiabensaada.dhia.repos.RoleRepository;
import dhiabensaada.dhia.repos.UserRepository;
import dhiabensaada.dhia.util.NotFoundException;
import dhiabensaada.dhia.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReclamationRepository reclamationRepository;
    private final PostReactionRepository postReactionRepository;
    private final CommentReactionRepository commentReactionRepository;

    public UserService(final UserRepository userRepository, final RoleRepository roleRepository,
            final PasswordEncoder passwordEncoder, final PostRepository postRepository,
            final CommentRepository commentRepository,
            final ReclamationRepository reclamationRepository,
            final PostReactionRepository postReactionRepository,
            final CommentReactionRepository commentReactionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.reclamationRepository = reclamationRepository;
        this.postReactionRepository = postReactionRepository;
        this.commentReactionRepository = commentReactionRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setRole(user.getRole() == null ? null : user.getRole().getId());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setUpdatedAt(userDTO.getUpdatedAt());
        final Role role = userDTO.getRole() == null ? null : roleRepository.findById(userDTO.getRole())
                .orElseThrow(() -> new NotFoundException("role not found"));
        user.setRole(role);
        return user;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Post userPost = postRepository.findFirstByUser(user);
        if (userPost != null) {
            referencedWarning.setKey("user.post.user.referenced");
            referencedWarning.addParam(userPost.getId());
            return referencedWarning;
        }
        final Comment userComment = commentRepository.findFirstByUser(user);
        if (userComment != null) {
            referencedWarning.setKey("user.comment.user.referenced");
            referencedWarning.addParam(userComment.getId());
            return referencedWarning;
        }
        final Reclamation userReclamation = reclamationRepository.findFirstByUser(user);
        if (userReclamation != null) {
            referencedWarning.setKey("user.reclamation.user.referenced");
            referencedWarning.addParam(userReclamation.getId());
            return referencedWarning;
        }
        final PostReaction userPostReaction = postReactionRepository.findFirstByUser(user);
        if (userPostReaction != null) {
            referencedWarning.setKey("user.postReaction.user.referenced");
            referencedWarning.addParam(userPostReaction.getId());
            return referencedWarning;
        }
        final CommentReaction userCommentReaction = commentReactionRepository.findFirstByUser(user);
        if (userCommentReaction != null) {
            referencedWarning.setKey("user.commentReaction.user.referenced");
            referencedWarning.addParam(userCommentReaction.getId());
            return referencedWarning;
        }
        return null;
    }

}
