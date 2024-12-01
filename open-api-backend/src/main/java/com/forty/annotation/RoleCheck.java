package com.forty.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleCheck {
    /**
     * 要满足所有角色才允许访问api
     * @return
     */
    String[] requiredRoles() default {};

    /**
     * 只要满足其中一个角色就可以访问api
     */
    String[] hasRoles() default {};
}
