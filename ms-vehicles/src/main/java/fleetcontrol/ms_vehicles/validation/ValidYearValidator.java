package fleetcontrol.ms_vehicles.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;
import java.time.ZoneOffset;

public class ValidYearValidator implements ConstraintValidator<ValidYear, Integer> {

    static final int MIN_YEAR = 1990;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // la obligatoriedad la valida @NotNull
        }
        int maxYear = Year.now(ZoneOffset.UTC).getValue() + 1;
        if (value >= MIN_YEAR && value <= maxYear) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("debe estar entre " + MIN_YEAR + " y " + maxYear)
                .addConstraintViolation();
        return false;
    }
}
