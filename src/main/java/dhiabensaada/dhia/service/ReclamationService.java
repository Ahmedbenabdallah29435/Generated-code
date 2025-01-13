package dhiabensaada.dhia.service;

import dhiabensaada.dhia.domain.Reclamation;
import dhiabensaada.dhia.domain.User;
import dhiabensaada.dhia.model.ReclamationDTO;
import dhiabensaada.dhia.repos.ReclamationRepository;
import dhiabensaada.dhia.repos.UserRepository;
import dhiabensaada.dhia.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReclamationService {

    private final ReclamationRepository reclamationRepository;
    private final UserRepository userRepository;

    public ReclamationService(final ReclamationRepository reclamationRepository,
            final UserRepository userRepository) {
        this.reclamationRepository = reclamationRepository;
        this.userRepository = userRepository;
    }

    public List<ReclamationDTO> findAll() {
        final List<Reclamation> reclamations = reclamationRepository.findAll(Sort.by("id"));
        return reclamations.stream()
                .map(reclamation -> mapToDTO(reclamation, new ReclamationDTO()))
                .toList();
    }

    public ReclamationDTO get(final Long id) {
        return reclamationRepository.findById(id)
                .map(reclamation -> mapToDTO(reclamation, new ReclamationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ReclamationDTO reclamationDTO) {
        final Reclamation reclamation = new Reclamation();
        mapToEntity(reclamationDTO, reclamation);
        return reclamationRepository.save(reclamation).getId();
    }

    public void update(final Long id, final ReclamationDTO reclamationDTO) {
        final Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reclamationDTO, reclamation);
        reclamationRepository.save(reclamation);
    }

    public void delete(final Long id) {
        reclamationRepository.deleteById(id);
    }

    private ReclamationDTO mapToDTO(final Reclamation reclamation,
            final ReclamationDTO reclamationDTO) {
        reclamationDTO.setId(reclamation.getId());
        reclamationDTO.setTitle(reclamation.getTitle());
        reclamationDTO.setDescription(reclamation.getDescription());
        reclamationDTO.setStatus(reclamation.getStatus());
        reclamationDTO.setCreatedAt(reclamation.getCreatedAt());
        reclamationDTO.setUpdatedAt(reclamation.getUpdatedAt());
        reclamationDTO.setUser(reclamation.getUser() == null ? null : reclamation.getUser().getId());
        return reclamationDTO;
    }

    private Reclamation mapToEntity(final ReclamationDTO reclamationDTO,
            final Reclamation reclamation) {
        reclamation.setTitle(reclamationDTO.getTitle());
        reclamation.setDescription(reclamationDTO.getDescription());
        reclamation.setStatus(reclamationDTO.getStatus());
        reclamation.setCreatedAt(reclamationDTO.getCreatedAt());
        reclamation.setUpdatedAt(reclamationDTO.getUpdatedAt());
        final User user = reclamationDTO.getUser() == null ? null : userRepository.findById(reclamationDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        reclamation.setUser(user);
        return reclamation;
    }

}
