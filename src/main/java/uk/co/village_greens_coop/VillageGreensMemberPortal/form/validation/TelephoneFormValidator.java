package uk.co.village_greens_coop.VillageGreensMemberPortal.form.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import uk.co.village_greens_coop.VillageGreensMemberPortal.form.TelephoneForm;

public class TelephoneFormValidator implements Validator {

    /**
     * This Validator validates TelephoneForm instances, and any subclasses of TelephoneForm too
     */
    public boolean supports(@SuppressWarnings("rawtypes") Class clazz) {
        return TelephoneForm.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
    	
    	TelephoneForm tf = (TelephoneForm)target;
    	if ("N".equals(tf.getUpdateState())) {
    		// we'll allow a New empty telephone field set, as that's how the form is initially presented
    		// so, if New, allow both fields to be empty, otherwise require both
    		if (!"".equals(tf.getTelephoneType())) {
    			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "telephoneNumber", "telephoneNumber.required");
    		}
    		if (!"".equals(tf.getTelephoneNumber())) {
    			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "telephoneType", "telephoneType.required");
    		}
    	} else if ("U".equals(tf.getUpdateState())) {
    		// for non-new, perform standard validation of both fields independently
	        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "telephoneNumber", "telephoneNumber.required");
	        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "telephoneType", "telephoneType.required");
    	}
    	// don't validate "D" status numbers, as we're deleting it anyway
    }
}