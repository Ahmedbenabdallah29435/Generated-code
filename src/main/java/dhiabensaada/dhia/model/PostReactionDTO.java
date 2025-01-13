package dhiabensaada.dhia.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PostReactionDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String reaction;

    private OffsetDateTime createdAt;

    @NotNull
    private Long user;

    @NotNull
    private Long post;

}
