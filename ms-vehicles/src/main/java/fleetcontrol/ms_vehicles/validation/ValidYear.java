package fleetcontrol.ms_vehicles.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidYearValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidYear {

    String message() default "año fuera de rango";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
