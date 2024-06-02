package com.gian.carrasco.reto.tecnico.errors;

import com.gian.carrasco.reto.tecnico.constant.ErroresConstants;
import com.gian.carrasco.reto.tecnico.exception.ControlledException;
import com.gian.carrasco.reto.tecnico.exception.IdNotExistsException;
import com.gian.carrasco.reto.tecnico.exception.MissingFieldException;
import com.gian.carrasco.reto.tecnico.vo.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalErrorWebExceptionHandler.class);

    public GlobalErrorWebExceptionHandler(DefaultErrorAttributes errorAttributes, WebProperties.Resources resources,
                                          ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);
        if(error instanceof MissingFieldException) {
            MissingFieldException ex = (MissingFieldException) error;
            String errorMsg = ex.getErrors().getFieldErrors().stream()
                    .map(x -> x.getField() + ": " + x.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseVO.badRequest(errorMsg);
        } else if(error instanceof IdNotExistsException e)
            return ResponseVO.notFound(e.getMessage());
        else if(error instanceof ControlledException e)
            return ResponseVO.badRequest(e.getMessage());

        log.error(error.getMessage(), error);

        return ResponseVO.internalServerError(ErroresConstants.ERROR_GENERICO);
    }
}