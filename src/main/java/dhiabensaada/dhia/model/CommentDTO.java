package dhiabensaada.dhia.model;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentDTO {

    private Long id;

    @NotNull
    private String content;

    private OffsetDateTime createdAt;

    @NotNull
    private Long user;

    @NotNull
    private Long post;

}
