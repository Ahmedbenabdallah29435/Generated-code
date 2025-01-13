package dhiabensaada.dhia.service;

import dhiabensaada.dhia.domain.Comment;
import dhiabensaada.dhia.domain.Post;
import dhiabensaada.dhia.domain.PostReaction;
import dhiabensaada.dhia.domain.User;
import dhiabensaada.dhia.model.PostDTO;
import dhiabensaada.dhia.repos.CommentRepository;
import dhiabensaada.dhia.repos.PostReactionRepository;
import dhiabensaada.dhia.repos.PostRepository;
import dhiabensaada.dhia.repos.UserRepository;
import dhiabensaada.dhia.util.NotFoundException;
import dhiabensaada.dhia.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostReactionRepository postReactionRepository;

    public PostService(final PostRepository postRepository, final UserRepository userRepository,
            final CommentRepository commentRepository,
            final PostReactionRepository postReactionRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postReactionRepository = postReactionRepository;
    }

    public List<PostDTO> findAll() {
        final List<Post> posts = postRepository.findAll(Sort.by("id"));
        return posts.stream()
                .map(post -> mapToDTO(post, new PostDTO()))
                .toList();
    }

    public PostDTO get(final Long id) {
        return postRepository.findById(id)
                .map(post -> mapToDTO(post, new PostDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PostDTO postDTO) {
        final Post post = new Post();
        mapToEntity(postDTO, post);
        return postRepository.save(post).getId();
    }

    public void update(final Long id, final PostDTO postDTO) {
        final Post post = postRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(postDTO, post);
        postRepository.save(post);
    }

    public void delete(final Long id) {
        postRepository.deleteById(id);
    }

    private PostDTO mapToDTO(final Post post, final PostDTO postDTO) {
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUpdatedAt(post.getUpdatedAt());
        postDTO.setUser(post.getUser() == null ? null : post.getUser().getId());
        return postDTO;
    }

    private Post mapToEntity(final PostDTO postDTO, final Post post) {
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setCreatedAt(postDTO.getCreatedAt());
        post.setUpdatedAt(postDTO.getUpdatedAt());
        final User user = postDTO.getUser() == null ? null : userRepository.findById(postDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        post.setUser(user);
        return post;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Post post = postRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Comment postComment = commentRepository.findFirstByPost(post);
        if (postComment != null) {
            referencedWarning.setKey("post.comment.post.referenced");
            referencedWarning.addParam(postComment.getId());
            return referencedWarning;
        }
        final PostReaction postPostReaction = postReactionRepository.findFirstByPost(post);
        if (postPostReaction != null) {
            referencedWarning.setKey("post.postReaction.post.referenced");
            referencedWarning.addParam(postPostReaction.getId());
            return referencedWarning;
        }
        return null;
    }

}
