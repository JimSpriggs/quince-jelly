package uk.co.village_greens_coop.VillageGreensMemberPortal.form.validation;

import static org.hibernate.validator.internal.util.logging.Messages.MESSAGES;

import javax.script.ScriptException;
import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
 
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.ScriptAssertValidator;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.scriptengine.ScriptEvaluator;
import org.hibernate.validator.internal.util.scriptengine.ScriptEvaluatorFactory;
 
/**
 * Validator for {@link ScriptAssertFieldError}
 * @see ScriptAssertValidator
 */
public class ScriptAssertFieldErrorValidator implements ConstraintValidator<ScriptAssertFieldError, Object> {
 
  private static final String TRUE_OR_FALSE_EXCEPTION = "script must return true or false";
	private String script;
	private String languageName;
	private String alias;
	private String fieldName;
	private String errorMessage;
	
	@Override
	public void initialize(ScriptAssertFieldError constraintAnnotation) {
		validateParameters( constraintAnnotation );
 
		this.script = constraintAnnotation.script();
		this.languageName = constraintAnnotation.lang();
		this.alias = constraintAnnotation.alias();
		this.fieldName = constraintAnnotation.fieldName();
		this.errorMessage = constraintAnnotation.message();
		
	}
 
	/* (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Object evaluationResult;
		ScriptEvaluator scriptEvaluator;
 
		try {
			ScriptEvaluatorFactory evaluatorFactory = ScriptEvaluatorFactory.getInstance();
			scriptEvaluator = evaluatorFactory.getScriptEvaluatorByLanguageName( languageName );
		}
		catch ( ScriptException e ) {
			throw new ConstraintDeclarationException( e );
		}
 
		try {
			evaluationResult = scriptEvaluator.evaluate( script, value, alias );
		}
		catch ( ScriptException e ) {
			throw new ConstraintDeclarationException("An error occurred during script execution", e);
		}
 
		if ( evaluationResult == null ) {
			throw new ConstraintDeclarationException(TRUE_OR_FALSE_EXCEPTION);
		}
		if ( !( evaluationResult instanceof Boolean ) ) {
			throw new ConstraintDeclarationException(TRUE_OR_FALSE_EXCEPTION);
		}
 
		Boolean result = Boolean.TRUE.equals( evaluationResult );
		if(!result && StringUtils.isNotEmpty(fieldName)) {
			ConstraintValidatorUtils.addConstraintViolation(context, errorMessage, fieldName);
		}
		return result;
	}
	
	private void validateParameters(ScriptAssertFieldError constraintAnnotation) {
		Contracts.assertNotEmpty( constraintAnnotation.script(), MESSAGES.parameterMustNotBeEmpty( "script" ) );
		Contracts.assertNotEmpty( constraintAnnotation.lang(), MESSAGES.parameterMustNotBeEmpty( "lang" ) );
		Contracts.assertNotEmpty( constraintAnnotation.alias(), MESSAGES.parameterMustNotBeEmpty( "alias" ) );
	}
 
}