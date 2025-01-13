package dhiabensaada.dhia.rest;

import dhiabensaada.dhia.domain.Comment;
import dhiabensaada.dhia.domain.User;
import dhiabensaada.dhia.model.CommentReactionDTO;
import dhiabensaada.dhia.model.NomRole;
import dhiabensaada.dhia.repos.CommentRepository;
import dhiabensaada.dhia.repos.UserRepository;
import dhiabensaada.dhia.service.CommentReactionService;
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
@RequestMapping(value = "/api/commentReactions", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + NomRole.Fields.ADMIN + "', '" + NomRole.Fields.CLIENT + "')")
@SecurityRequirement(name = "bearer-jwt")
public class CommentReactionResource {

    private final CommentReactionService commentReactionService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentReactionResource(final CommentReactionService commentReactionService,
            final UserRepository userRepository, final CommentRepository commentRepository) {
        this.commentReactionService = commentReactionService;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping
    public ResponseEntity<List<CommentReactionDTO>> getAllCommentReactions() {
        return ResponseEntity.ok(commentReactionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentReactionDTO> getCommentReaction(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(commentReactionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCommentReaction(
            @RequestBody @Valid final CommentReactionDTO commentReactionDTO) {
        final Long createdId = commentReactionService.create(commentReactionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCommentReaction(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CommentReactionDTO commentReactionDTO) {
        commentReactionService.update(id, commentReactionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCommentReaction(@PathVariable(name = "id") final Long id) {
        commentReactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/userValues")
    public ResponseEntity<Map<Long, String>> getUserValues() {
        return ResponseEntity.ok(userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
    }

    @GetMapping("/commentValues")
    public ResponseEntity<Map<Long, Long>> getCommentValues() {
        return ResponseEntity.ok(commentRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Comment::getId, Comment::getId)));
    }

}
