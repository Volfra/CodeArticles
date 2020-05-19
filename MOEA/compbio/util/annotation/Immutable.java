package util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation distinguish immutable class from non-immutable. Intended as a
 * documentation element for developers
 * 
 * @author pvtroshin
 * @version 1.0 October 2009
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Immutable {
}
