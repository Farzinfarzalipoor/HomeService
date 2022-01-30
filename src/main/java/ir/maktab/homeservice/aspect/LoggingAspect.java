package ir.maktab.homeservice.aspect;


import ch.qos.logback.classic.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {
    Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* ir.maktab.homeservice.*.*(..) )")
    public void logServicePackage(ProceedingJoinPoint jp)  {
        String params = Arrays.stream(jp.getArgs()).map(Object::toString).collect( Collectors.joining( "," ) );
        logger.info("Before method: " +jp.getSignature().getDeclaringTypeName()+"." + jp.getSignature().getName()+"("+ params+")");
        Object proceed=null;
        try {
            proceed = jp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        logger.debug("After method: "+jp.getSignature().getDeclaringTypeName()+"."+ jp.getSignature().getName() +":"+ proceed);
    }

}