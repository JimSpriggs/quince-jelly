package uk.co.village_greens_coop.VillageGreensMemberPortal.form.validation;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import uk.co.village_greens_coop.VillageGreensMemberPortal.form.MemberForm;
import uk.co.village_greens_coop.VillageGreensMemberPortal.form.TelephoneForm;

public class MemberFormValidator implements Validator {

    private final Validator telephoneFormValidator;

    public MemberFormValidator(Validator telephoneFormValidator) {
        if (telephoneFormValidator == null) {
            throw new IllegalArgumentException("The supplied [Validator] is " +
                "required and must not be null.");
        }
        if (!telephoneFormValidator.supports(TelephoneForm.class)) {
            throw new IllegalArgumentException("The supplied [Validator] must " +
                "support the validation of [TelephoneForm] instances.");
        }
        this.telephoneFormValidator = telephoneFormValidator;
    }

    /**
     * This Validator validates MemberForm instances, and any subclasses of MemberForm
     */
    public boolean supports(@SuppressWarnings("rawtypes") Class clazz) {
        return MemberForm.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        MemberForm memberForm = (MemberForm) target;
        List<TelephoneForm> tfs = memberForm.getTelephones();
        if (tfs != null) {
        	for (int i = 0; i < tfs.size(); i++) {
                try {
                    errors.pushNestedPath("telephones[" + i + "]");
                    ValidationUtils.invokeValidator(this.telephoneFormValidator, tfs.get(i), errors);
                } finally {
                    errors.popNestedPath();
                }
        		
        	}
        }
    }
}