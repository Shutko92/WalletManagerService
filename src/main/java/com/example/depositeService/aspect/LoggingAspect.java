package com.example.depositeService.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
@Slf4j(topic = "DepositService")
public class LoggingAspect {

    @Before(value = "execution(public * com.example.depositeService.controller.*Controller.*(..))")
    public void loggingControllers(JoinPoint joinPoint) {
        String controllerName = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String path = request.getRequestURI();
        log.info("Контроллер: {} Вызван метод {} с полным путем {}", controllerName, methodName, path);
    }

    @Before(value = "execution(public * com.example.depositeService.service..*.*(..))")
    public void loggingServices(JoinPoint joinPoint) {
        String serviceName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        log.info("Сервис: {} Вызван метод {} с аргументами {}", serviceName, methodName, Arrays.toString(arguments));
    }

    @After(value = "@annotation(exceptionHandler) && execution(public * com.example.depositeService.controller.advice.*.*(..))")
    public void loggingControllerAdvice(JoinPoint joinPoint, ExceptionHandler exceptionHandler) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        StringBuilder exception = new StringBuilder();
        StringBuilder message = new StringBuilder();
        Optional<Throwable> exceptionsOptional = Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof Throwable)
                .map(arg -> (Throwable) arg)
                .findFirst();
        if (exceptionsOptional.isPresent()) {
            exception.append(exceptionsOptional.get().getClass().getName());
            message.append(exceptionsOptional.get().getMessage());
        }
        log.warn("Перехвачено исключение {}, сообщение: {}, контроллер: {}, метод: {}",
                exception, message, className, methodName);
    }
}
