package com.liu.soyanrpcspringbootstarter.annotations;

import com.liu.soyanrpcspringbootstarter.bootstrap.RpcConsumerBootStrap;
import com.liu.soyanrpcspringbootstarter.bootstrap.RpcInitBootStrap;
import com.liu.soyanrpcspringbootstarter.bootstrap.RpcProviderBootStrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用rpc的注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootStrap.class, RpcProviderBootStrap.class, RpcConsumerBootStrap.class})
public @interface EnableRpc {
    boolean needSerer() default true;
}
