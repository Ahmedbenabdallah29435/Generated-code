package dhiabensaada.dhia.model;

import lombok.experimental.FieldNameConstants;


@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum NomRole {

    @FieldNameConstants.Include
    ADMIN,
    @FieldNameConstants.Include
    CLIENT

}
