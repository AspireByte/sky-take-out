package com.sky.aspect;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");
        MethodSignature methodSignature=(MethodSignature)joinPoint.getSignature();
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType = autoFill.value();

        Object[] args=joinPoint.getArgs();
        if (args==null||args.length==0) {
            return;
        }

        Object entity=args[0];
        LocalDateTime now=LocalDateTime.now();
        Long currentId=BaseContext.getCurrentId();

        if (operationType==OperationType.INSERT) {
            // 填充创建时间
            try {
                Method setCreateDateTime=entity.getClass().getDeclaredMethod("setCreateDateTime",LocalDateTime.class);
                Method setCreateUser=entity.getClass().getDeclaredMethod("setCreateUser",Long.class);
                Method setUpdateDateTime=entity.getClass().getDeclaredMethod("setUpdateDateTime",LocalDateTime.class);
                Method setUpdateUser=entity.getClass().getDeclaredMethod("setUpdateUser",Long.class);

                setCreateDateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateDateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();                
            }
        }else if (operationType==OperationType.UPDATE) {
           try {
                Method setUpdateDateTime=entity.getClass().getDeclaredMethod("setUpdateDateTime",LocalDateTime.class);
                Method setUpdateUser=entity.getClass().getDeclaredMethod("setUpdateUser",Long.class);

                setUpdateDateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();                
            }
        }

    }
}
