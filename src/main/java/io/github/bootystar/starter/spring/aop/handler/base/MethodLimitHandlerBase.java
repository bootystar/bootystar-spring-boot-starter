package io.github.bootystar.starter.spring.aop.handler.base;

import io.github.bootystar.starter.spring.exception.MethodLimitException;
import io.github.bootystar.starter.spring.aop.handler.MethodLimitHandler;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

/**
 * @author bootystar
 */
public abstract class MethodLimitHandlerBase implements MethodLimitHandler {
    private final String prefix = getClass().getSimpleName() + "-";
    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();

    @Override
    public String signature(Method method, Object[] args, String expression) {
        String genericString = method.toGenericString();
        if (args == null || args.length == 0) {
            return prefix + genericString;
        }
        if (expression.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Object arg : args) {
                sb.append(";");
                if (arg != null) {
                    sb.append(arg);
                } else {
                    sb.append("null");
                }
            }
            return prefix + genericString + sb;
        }
        return prefix + genericString + parser.parseExpression(expression).getValue(new MethodBasedEvaluationContext(null, method, args, pnd));
    }

    @Override
    public Object fallback(String signature) {
        throw new MethodLimitException("processing, please try again later");
    }
}
