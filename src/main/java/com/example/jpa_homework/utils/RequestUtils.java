package com.example.jpa_homework.utils;

import com.example.jpa_homework.model.dto.response.Pagination;
import com.example.jpa_homework.model.dto.response.Response;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;


import java.net.URI;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;


public class RequestUtils {

    public static <T> Response<T> getResponse(HttpStatusCode code, String message, T data) {
        return new Response<>(
                code,
                message,
                OffsetDateTime.now(),
                data,
                null
        );
    }


    public static <T> Response<T> getPaginatedResponse(
            HttpStatusCode code,
            String message,
            T data,
            int page,
            int limit,
            long totalCount) {

        int totalPages = (int) Math.ceil((double) totalCount / limit);

        Pagination pagination = Pagination.builder()
                .page(page)
                .limit(limit)
                .totalCount((int) totalCount)
                .totalPages(totalPages)
                .nextPage(page < totalPages ? page + 1 : totalPages)
                .previousPage(page > 1 ? page - 1 : 1)
                .offset((page - 1) * limit)
                .pagesToShow(totalPages)
                .startPage(Math.max(1, page - 2))
                .endPage(Math.min(totalPages, page + 2))
                .build();

        return new Response<>(
                code,
                message,
                OffsetDateTime.now(),
                data,
                pagination
        );
    }

//    public static void handleErrorException(@NonNull HttpServletRequest request, HttpServletResponse response, Exception e) {
//        HttpStatus status = determineHttpStatus(e);
//        ProblemDetail apiResponse = getErrorResponse(request, response, e, status);
//        try {
//            writeResponse.accept(response, apiResponse);
//        } catch (Exception ex) {
//            throw new ApiException("Failed to write error response: " + ex.getMessage(), ex);
//        }
//    }
//
//    private static HttpStatus determineHttpStatus(Exception e) {
//        if (e instanceof AccessDeniedException || e instanceof SignatureException) {
//            return UNAUTHORIZED;
//        } else if (e instanceof AuthenticationException) {
//            return FORBIDDEN;
//        } else {
//            return BAD_REQUEST;
//        }
//    }
//
//    private static final BiConsumer<HttpServletResponse, ProblemDetail> writeResponse = (response, apiResponse) -> {
//        try {
//            response.setStatus(apiResponse.getStatus());
//            response.setContentType(APPLICATION_PROBLEM_JSON_VALUE);
//            ServletOutputStream outputStream = response.getOutputStream();
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.findAndRegisterModules();
//            objectMapper.writeValue(outputStream, apiResponse);
//            outputStream.flush();
//        } catch (IOException e) {
//            throw new ApiException("Failed to write ProblemDetail response: " + e.getMessage(), e);
//        }
//    };
//
//    private static final BiFunction<Exception, HttpStatus, String> errorReason = (ex, status) -> {
//        if (status.isSameCodeAs(FORBIDDEN)) {
//            return "Email and/or password is incorrect, Please try again";
//        }
//        if (status.isSameCodeAs(UNAUTHORIZED)) {
//            return "You are not authorized to access this resource";
//        }
//        if (ex instanceof DisabledException || ex instanceof LockedException || ex instanceof BadCredentialsException || ex instanceof AccessDeniedException || ex instanceof ApiException) {
//            return ex.getMessage();
//        }
//        if(ex instanceof ExpiredJwtException){
//            return "Token is expired";
//        }
//        if (ex instanceof CannotGetJdbcConnectionException) {
//            return "Database connection error";
//        }
//        if (ex instanceof SignatureException) {
//            return "Invalid token signature";
//        }
//        if (status.is5xxServerError()) {
//            return "An internal server error occurred while processing your request";
//        } else {
//            return ex.getMessage();
//        }
//    };
//
//    public static ProblemDetail getErrorResponse(HttpServletRequest request, HttpServletResponse response,  Exception e, HttpStatus status) {
//        ProblemDetail problem = ProblemDetail.forStatus(status);
//        problem.setDetail(errorReason.apply(e, status));
//        problem.setProperty("path", request.getRequestURI());
//        return problem;
//    }
//
//    public static void handleAuthorityError(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        response.setStatus(SC_UNAUTHORIZED);
//        response.setContentType(APPLICATION_PROBLEM_JSON_VALUE);
//
//        ProblemDetail problem = ProblemDetail.forStatus(UNAUTHORIZED);
//        problem.setDetail(message);
//        problem.setProperty("path", request.getRequestURI());
//        problem.setInstance(URI.create(API_URL + request.getRequestURI()));
//
//        objectMapper.writeValue(response.getOutputStream(), problem);
//    }
//
    public static ProblemDetail handleDetailsException(HttpServletRequest request, String message, HttpStatus status) {
        ProblemDetail detail = ProblemDetail.forStatus(status);
        detail.setProperty("code", "Error");
        detail.setProperty("errors", message);
        detail.setProperty("timestamp", LocalDateTime.now());
//        detail.setType(URI.create(API_URL + request.getRequestURI()));
        return detail;
    }
}
