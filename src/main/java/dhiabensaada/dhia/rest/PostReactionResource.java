package dhiabensaada.dhia.rest;

import dhiabensaada.dhia.domain.Post;
import dhiabensaada.dhia.domain.User;
import dhiabensaada.dhia.model.NomRole;
import dhiabensaada.dhia.model.PostReactionDTO;
import dhiabensaada.dhia.repos.PostRepository;
import dhiabensaada.dhia.repos.UserRepository;
import dhiabensaada.dhia.service.PostReactionService;
import dhiabensaada.dhia.util.CustomCollectors;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200") // Allows all endpoints in this controller

@RestController
@RequestMapping(value = "/api/postReactions", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + NomRole.Fields.ADMIN + "', '" + NomRole.Fields.CLIENT + "')")
@SecurityRequirement(name = "bearer-jwt")
public class PostReactionResource {

    private final PostReactionService postReactionService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostReactionResource(final PostReactionService postReactionService,
            final UserRepository userRepository, final PostRepository postRepository) {
        this.postReactionService = postReactionService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping
    public ResponseEntity<List<PostReactionDTO>> getAllPostReactions() {
        return ResponseEntity.ok(postReactionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostReactionDTO> getPostReaction(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(postReactionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPostReaction(
            @RequestBody @Valid final PostReactionDTO postReactionDTO) {
        final Long createdId = postReactionService.create(postReactionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePostReaction(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PostReactionDTO postReactionDTO) {
        postReactionService.update(id, postReactionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePostReaction(@PathVariable(name = "id") final Long id) {
        postReactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/userValues")
    public ResponseEntity<Map<Long, String>> getUserValues() {
        return ResponseEntity.ok(userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
    }

    @GetMapping("/postValues")
    public ResponseEntity<Map<Long, String>> getPostValues() {
        return ResponseEntity.ok(postRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Post::getId, Post::getTitle)));
    }

}
