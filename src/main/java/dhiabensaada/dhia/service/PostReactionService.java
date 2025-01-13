package dhiabensaada.dhia.service;

import dhiabensaada.dhia.domain.Post;
import dhiabensaada.dhia.domain.PostReaction;
import dhiabensaada.dhia.domain.User;
import dhiabensaada.dhia.model.PostReactionDTO;
import dhiabensaada.dhia.repos.PostReactionRepository;
import dhiabensaada.dhia.repos.PostRepository;
import dhiabensaada.dhia.repos.UserRepository;
import dhiabensaada.dhia.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PostReactionService {

    private final PostReactionRepository postReactionRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostReactionService(final PostReactionRepository postReactionRepository,
            final UserRepository userRepository, final PostRepository postRepository) {
        this.postReactionRepository = postReactionRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<PostReactionDTO> findAll() {
        final List<PostReaction> postReactions = postReactionRepository.findAll(Sort.by("id"));
        return postReactions.stream()
                .map(postReaction -> mapToDTO(postReaction, new PostReactionDTO()))
                .toList();
    }

    public PostReactionDTO get(final Long id) {
        return postReactionRepository.findById(id)
                .map(postReaction -> mapToDTO(postReaction, new PostReactionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PostReactionDTO postReactionDTO) {
        final PostReaction postReaction = new PostReaction();
        mapToEntity(postReactionDTO, postReaction);
        return postReactionRepository.save(postReaction).getId();
    }

    public void update(final Long id, final PostReactionDTO postReactionDTO) {
        final PostReaction postReaction = postReactionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(postReactionDTO, postReaction);
        postReactionRepository.save(postReaction);
    }

    public void delete(final Long id) {
        postReactionRepository.deleteById(id);
    }

    private PostReactionDTO mapToDTO(final PostReaction postReaction,
            final PostReactionDTO postReactionDTO) {
        postReactionDTO.setId(postReaction.getId());
        postReactionDTO.setReaction(postReaction.getReaction());
        postReactionDTO.setCreatedAt(postReaction.getCreatedAt());
        postReactionDTO.setUser(postReaction.getUser() == null ? null : postReaction.getUser().getId());
        postReactionDTO.setPost(postReaction.getPost() == null ? null : postReaction.getPost().getId());
        return postReactionDTO;
    }

    private PostReaction mapToEntity(final PostReactionDTO postReactionDTO,
            final PostReaction postReaction) {
        postReaction.setReaction(postReactionDTO.getReaction());
        postReaction.setCreatedAt(postReactionDTO.getCreatedAt());
        final User user = postReactionDTO.getUser() == null ? null : userRepository.findById(postReactionDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        postReaction.setUser(user);
        final Post post = postReactionDTO.getPost() == null ? null : postRepository.findById(postReactionDTO.getPost())
                .orElseThrow(() -> new NotFoundException("post not found"));
        postReaction.setPost(post);
        return postReaction;
    }

}
