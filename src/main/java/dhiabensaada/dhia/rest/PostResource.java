package dhiabensaada.dhia.rest;

import dhiabensaada.dhia.domain.User;
import dhiabensaada.dhia.model.NomRole;
import dhiabensaada.dhia.model.PostDTO;
import dhiabensaada.dhia.repos.UserRepository;
import dhiabensaada.dhia.service.PostService;
import dhiabensaada.dhia.util.CustomCollectors;
import dhiabensaada.dhia.util.ReferencedException;
import dhiabensaada.dhia.util.ReferencedWarning;
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
@RequestMapping(value = "/api/posts", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class PostResource {

    private final PostService postService;
    private final UserRepository userRepository;

    public PostResource(final PostService postService, final UserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + NomRole.Fields.ADMIN + "', '" + NomRole.Fields.CLIENT + "')")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + NomRole.Fields.ADMIN + "', '" + NomRole.Fields.CLIENT + "')")
    public ResponseEntity<PostDTO> getPost(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(postService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('" + NomRole.Fields.ADMIN + "')")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPost(@RequestBody @Valid final PostDTO postDTO) {
        final Long createdId = postService.create(postDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + NomRole.Fields.ADMIN + "')")
    public ResponseEntity<Long> updatePost(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PostDTO postDTO) {
        postService.update(id, postDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + NomRole.Fields.ADMIN + "')")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePost(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = postService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/userValues")
    @PreAuthorize("hasAuthority('" + NomRole.Fields.ADMIN + "')")
    public ResponseEntity<Map<Long, String>> getUserValues() {
        return ResponseEntity.ok(userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
    }

}
