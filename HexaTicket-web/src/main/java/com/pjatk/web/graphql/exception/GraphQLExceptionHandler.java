package com.pjatk.web.graphql.exception;
import com.pjatk.core.exception.NotFoundException;
import com.pjatk.core.exception.TooEarlyDateException;
import com.pjatk.core.exception.TooMuchSeatsException;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ValidationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class GraphQLExceptionHandler implements DataFetcherExceptionResolver {

    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable exception, DataFetchingEnvironment environment) {

        if (exception instanceof NotFoundException) {
           GraphQLError error = GraphqlErrorBuilder.newError()
                   .message("not found " + exception.getMessage())
                   .path(environment.getExecutionStepInfo().getPath())
                   .errorType(ErrorType.DataFetchingException)
                   .extensions(Map.of("code", "NOT_FOUND"))
                   .build();
           return Mono.just(List.of(error));
        }
        if (exception instanceof ValidationException){
            GraphQLError error = GraphqlErrorBuilder.newError()
                    .message("validation failed " + exception.getMessage())
                    .path(environment.getExecutionStepInfo().getPath())
                    .errorType(ErrorType.ValidationError)
                    .extensions(Map.of("code", "BAD_REQUEST"))
                    .build();
            return Mono.just(List.of(error));
        }

        if (exception instanceof TooMuchSeatsException) {
            GraphQLError error = GraphqlErrorBuilder.newError()
                    .message("validation failed " + exception.getMessage())
                    .path(environment.getExecutionStepInfo().getPath())
                    .errorType(ErrorType.ValidationError)
                    .extensions(Map.of("code", "BAD_REQUEST"))
                    .build();
            return Mono.just(List.of(error));
        }


        if (exception instanceof TooEarlyDateException) {
            GraphQLError error = GraphqlErrorBuilder.newError()
                    .message("validation failed" + exception.getMessage())
                    .path(environment.getExecutionStepInfo().getPath())
                    .errorType(ErrorType.ValidationError)
                    .extensions(Map.of("code", "BAD_REQUEST"))
                    .build();
            return Mono.just(List.of(error));
        }


        return Mono.empty();
    }
}