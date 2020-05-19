package util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The class or method to which this annotation is applied is a testcase which
 * depends on the cluster execution environment and cannot be run locally.
 * 
 * @author pvtroshin
 * @version 1.0 October 2009
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClusterDependedTest {
}
