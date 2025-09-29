package com.mottu.trackyard.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ModeloMotoValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidModeloMoto {
    String message() default "Modelo de moto inválido. Use apenas: Pop, Sport ou E";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
