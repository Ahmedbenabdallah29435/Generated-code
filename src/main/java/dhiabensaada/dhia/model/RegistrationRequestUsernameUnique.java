package dhiabensaada.dhia.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import dhiabensaada.dhia.service.RegistrationService;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Validate that the username value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = RegistrationRequestUsernameUnique.RegistrationRequestUsernameUniqueValidator.class
)
public @interface RegistrationRequestUsernameUnique {

    String message() default "{registration.register.taken}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class RegistrationRequestUsernameUniqueValidator implements ConstraintValidator<RegistrationRequestUsernameUnique, String> {

        private final RegistrationService registrationService;

        public RegistrationRequestUsernameUniqueValidator(
                final RegistrationService registrationService) {
            this.registrationService = registrationService;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            return !registrationService.usernameExists(value);
        }

    }

}
