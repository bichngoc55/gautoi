package com.example.gautoi.annotation;

import com.example.gautoi.util.SourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditableLogAspect {
    private final AuditHelper auditHelper;
//request --> proxy --> adivse --> real method
    @Pointcut("@annotation(com.example.gautoi.annotation.AuditableLog)")
    public void controller() {

    }

    @Around(value = "controller()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AuditableLog auditableLog = methodSignature.getMethod().getAnnotation(AuditableLog.class);
        SourceType sourceType = auditableLog.value();
        Throwable error = null;
        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            error = ex;
            log.error("Exception in AuditableLogAspect", ex);
            throw ex;
        } finally {
            switch (sourceType) {
                case CONTROLLER -> auditHelper.handleControllerAudit(joinPoint, error); // pass error
                case SERVICE -> auditHelper.handleServiceAudit(joinPoint, error);
                case CONSUMER -> auditHelper.handleConsumerAudit(joinPoint, error);
            }

        }
    }

    @Before(value = "controller()")
    public void logBefore(JoinPoint joinPoint) {
        String methodDataInShort = joinPoint.getSignature().toShortString();
        log.info("Bat dau di vao ham {} ", methodDataInShort);
    }


}
