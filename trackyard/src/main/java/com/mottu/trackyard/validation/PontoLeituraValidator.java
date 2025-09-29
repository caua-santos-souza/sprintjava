package com.mottu.trackyard.validation;

import com.mottu.trackyard.enums.PontoLeitura;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PontoLeituraValidator implements ConstraintValidator<ValidPontoLeitura, String> {
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Deixa a validação @NotBlank cuidar dos valores nulos
        }
        
        try {
            // Verifica se o valor corresponde a algum enum
            for (PontoLeitura ponto : PontoLeitura.values()) {
                if (ponto.getDescricao().equals(value)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
