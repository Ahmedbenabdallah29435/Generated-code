package dhiabensaada.dhia.rest;

import dhiabensaada.dhia.domain.User;
import dhiabensaada.dhia.model.NomRole;
import dhiabensaada.dhia.model.ReclamationDTO;
import dhiabensaada.dhia.repos.UserRepository;
import dhiabensaada.dhia.service.ReclamationService;
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
@RequestMapping(value = "/api/reclamations", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + NomRole.Fields.ADMIN + "', '" + NomRole.Fields.CLIENT + "')")
@SecurityRequirement(name = "bearer-jwt")
public class ReclamationResource {

    private final ReclamationService reclamationService;
    private final UserRepository userRepository;

    public ReclamationResource(final ReclamationService reclamationService,
            final UserRepository userRepository) {
        this.reclamationService = reclamationService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<ReclamationDTO>> getAllReclamations() {
        return ResponseEntity.ok(reclamationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReclamationDTO> getReclamation(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(reclamationService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createReclamation(
            @RequestBody @Valid final ReclamationDTO reclamationDTO) {
        final Long createdId = reclamationService.create(reclamationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateReclamation(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ReclamationDTO reclamationDTO) {
        reclamationService.update(id, reclamationDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReclamation(@PathVariable(name = "id") final Long id) {
        reclamationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/userValues")
    public ResponseEntity<Map<Long, String>> getUserValues() {
        return ResponseEntity.ok(userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
    }

}
