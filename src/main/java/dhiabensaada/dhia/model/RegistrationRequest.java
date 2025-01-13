package dhiabensaada.dhia.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegistrationRequest {

    @NotNull
    @Size(max = 50)
    @RegistrationRequestUsernameUnique
    private String username;

    @NotNull
    @Size(max = 72)
    private String password;

    @NotNull
    @Size(max = 100)
    private String email;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
