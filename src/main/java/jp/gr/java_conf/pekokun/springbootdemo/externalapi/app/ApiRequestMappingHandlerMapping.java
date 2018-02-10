package jp.gr.java_conf.pekokun.springbootdemo.externalapi.app;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class ApiRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Value("${demo.externalapi.prefix:api}")
    private String prefix;

    public ApiRequestMappingHandlerMapping() {
        setOrder(0);
    }

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return AnnotatedElementUtils.hasAnnotation(beanType, Api.class);
    }

    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        Api api = method.getDeclaringClass().getAnnotation(Api.class);
        if (Objects.isNull(api)) {
            // No-annotated Api, then do nothing.
            return;
        }

        List<String> patterns = mapping.getPatternsCondition().getPatterns().stream()
                .map(p -> getPathMatcher().combine(prefix, p))
                .collect(Collectors.toList());

        super.registerHandlerMethod(handler, method,
            new RequestMappingInfo(
                new PatternsRequestCondition(patterns.toArray(new String[patterns.size()])),
                mapping.getMethodsCondition(),
                mapping.getParamsCondition(),
                mapping.getHeadersCondition(),
                mapping.getConsumesCondition(),
                mapping.getProducesCondition(),
                mapping.getCustomCondition()));

    }

}
