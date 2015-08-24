package uk.co.village_greens_coop.VillageGreensMemberPortal.form.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import uk.co.village_greens_coop.VillageGreensMemberPortal.form.SendStockEmailForm;

@Component
public class SendStockEmailFormValidator implements Validator {

    /**
     * This Validator validates SendStockEmailForm instances
     */
    public boolean supports(@SuppressWarnings("rawtypes") Class clazz) {
        return SendStockEmailForm.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
    	
    	SendStockEmailForm ssef = (SendStockEmailForm)target;
    	if (ssef.getAdhocRecipients()) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "recipients", "emailRecipients.required");
    	}
   }
}