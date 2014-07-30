package uk.co.village_greens_coop.VillageGreensMemberPortal.form.validation;

import javax.validation.ConstraintValidatorContext;

public abstract class ConstraintValidatorUtils {
	public static void addConstraintViolation(ConstraintValidatorContext context, String message, String field) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addNode(field).addConstraintViolation();
    }
	
	public static void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}