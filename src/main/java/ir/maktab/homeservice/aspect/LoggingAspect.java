package ir.maktab.homeservice.aspect;


import ch.qos.logback.classic.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* ir.maktab.homeservice.*.*(..) )")
    public void logServicePackage(ProceedingJoinPoint jp) {
        Object[] args = jp.getArgs();
        for (Object o: args) {
            logger.info(o.toString());
        }

        try {
            Object proceed = jp.proceed();
            logger.debug(proceed.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

}