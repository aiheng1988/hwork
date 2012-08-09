package org.hwork.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 标志Controller映射名
 * @author heng.ai@chinacache.com  
 * @date 2012-8-8 下午11:39:28 
 * @version V0.1  
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface Controller {
	String name();
}
