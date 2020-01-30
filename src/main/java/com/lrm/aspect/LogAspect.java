package com.lrm.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.lrm.web.*.*(..))")
    public void log(){};

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String id = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog  = new RequestLog(url,id,classMethod,args);


        logger.info("Request : {}",requestLog);
    }

    @After("log()")
    public void doAfter(){
      //  logger.info("-----doAfter------");
    }

    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result){
        logger.info("Result : {}",result);
    }

    private class RequestLog{
        private String url;
        private String id;
        private String classMethod;
        private Object[] args;
        public RequestLog(String url, String id, String classMethod, Object[] args) {
            this.url = url;
            this.id = id;
            this.classMethod = classMethod;
            this.args = args;
        }
        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", id='" + id + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
