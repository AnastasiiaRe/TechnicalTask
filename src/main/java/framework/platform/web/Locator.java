package framework.platform.web;

import framework.platform.html.WebObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which provides locator for proper initialization of page class fields of {@link WebObject} type.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Locator {

    String main() default "";
    String mobile() default "";
    String tablet() default "";

}
