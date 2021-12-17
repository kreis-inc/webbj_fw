package jp.bj_one.fw.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BjDbRequiredChk {
	String name() default "";
	String groupKeyNo() default "@bjOne";
	String tableName() default"";
	String deleteFlgName() default "";
}
