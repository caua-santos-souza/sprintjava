package com.mottu.trackyard.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PontoLeituraValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPontoLeitura {
    String message() default "Ponto de leitura inválido. Use apenas: defeito motor, dano estrutural, minha mottu, agendamento, pendência, reparos simples, para alugar, sem placa";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
