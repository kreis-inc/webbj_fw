package jp.bj_one.fw.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * BJ用ファイル添付用アノテーション
 * @author kreis
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BjFile {

	String fieldName() default "";
	String path() default "";

}