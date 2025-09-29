package com.mottu.trackyard.validation;

import com.mottu.trackyard.enums.ModeloMoto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ModeloMotoValidator implements ConstraintValidator<ValidModeloMoto, String> {
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Deixa a validação @NotBlank cuidar dos valores nulos
        }
        
        try {
            // Verifica se o valor corresponde a algum enum
            for (ModeloMoto modelo : ModeloMoto.values()) {
                if (modelo.getDescricao().equals(value)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
