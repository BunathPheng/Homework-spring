package com.example.jpa_homework.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatusCode;

import java.time.OffsetDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Schema(description = "Response object",
        example = """
        {
            "code": "OK",
            "message": "Success",
            "requestedTime": "2023-10-01T12:00:00Z",
            "data": {}
        }
        """
)
public record Response<T>(HttpStatusCode code, String message, OffsetDateTime requestedTime, T data, Pagination pagination) {

}
