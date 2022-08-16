package com.hq.es;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

@Service
@Aspect
public class AnnotationService {


    @Around("@annotation(com.hq.es.annotations.MyAnnotation)")
    public void myannotation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println(proceed);
    }
}
