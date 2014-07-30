package uk.co.village_greens_coop.VillageGreensMemberPortal.form.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
 
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
 
import javax.validation.Constraint;
import javax.validation.Payload;
 
import org.hibernate.validator.constraints.ScriptAssert;
 
/**
 * Uses {@link ScriptAssert} logic to validate the class and allows field error definitions
 * 
 * @see ScriptAssert
 * @author bchild
 *
 */
@Target({ TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = {ScriptAssertFieldErrorValidator.class })
@Documented
public @interface ScriptAssertFieldError {
 
  String message() default "{org.hibernate.validator.constraints.ScriptAssert.message}";
 
	Class<?>[] groups() default { };
 
	Class<? extends Payload>[] payload() default { };
	
	/**
	 * @return The name of the script language used by this constraint as
	 *         expected by the JSR 223 {@link javax.script.ScriptEngineManager}. A
	 *         {@link javax.validation.ConstraintDeclarationException} will be thrown upon script
	 *         evaluation, if no engine for the given language could be found.
	 */
	String lang();
 
	/**
	 * @return The script to be executed. The script must return
	 *         <code>Boolean.TRUE</code>, if the annotated element could
	 *         successfully be validated, otherwise <code>Boolean.FALSE</code>.
	 *         Returning null or any type other than Boolean will cause a
	 *         {@link javax.validation.ConstraintDeclarationException} upon validation. Any
	 *         exception occurring during script evaluation will be wrapped into
	 *         a ConstraintDeclarationException, too. Within the script, the
	 *         validated object can be accessed from the {@link javax.script.ScriptContext
	 *         script context} using the name specified in the
	 *         <code>alias</code> attribute.
	 */
	String script();
 
	/**
	 * @return The name, under which the annotated element shall be registered
	 *         within the script context. Defaults to "_this".
	 */
	String alias() default "_this";
	
	String fieldName();
 
	/**
	 * Defines several {@code @ScriptAssertFieldError} annotations on the same element.
	 */
	@Target({ TYPE })
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		ScriptAssertFieldError[] value();
	}
}