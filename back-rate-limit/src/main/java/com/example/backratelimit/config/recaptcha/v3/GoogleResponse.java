package com.example.backratelimit.config.recaptcha.v3;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "success", "score", "action","challenge_ts", "hostname", "error-codes" })
@Data
public class GoogleResponse {

    @JsonProperty("success")
    private boolean success;
    @JsonProperty("challenge_ts")
    private String challengeTs;
    @JsonProperty("hostname")
    private String hostname;
    @JsonProperty("score")
    private float score;
    @JsonProperty("action")
    private String action;
    @JsonProperty("error-codes")
    private ErrorCode[] errorCodes;


    enum ErrorCode {
        MissingSecret, InvalidSecret, MissingResponse, InvalidResponse, BadRequest, TimeoutOrDuplicate;

        private static Map<String, ErrorCode> errorsMap = new HashMap<>(6);

        static {
            errorsMap.put("missing-input-secret", MissingSecret);
            errorsMap.put("invalid-input-secret", InvalidSecret);
            errorsMap.put("missing-input-response", MissingResponse);
            errorsMap.put("bad-request", InvalidResponse);
            errorsMap.put("invalid-input-response", BadRequest);
            errorsMap.put("timeout-or-duplicate", TimeoutOrDuplicate);
        }

        @JsonCreator
        public static ErrorCode forValue(final String value) {
            return errorsMap.get(value.toLowerCase());
        }
    }

    @JsonIgnore
    public boolean hasClientError() {
        final ErrorCode[] errors = getErrorCodes();

        if (errors == null) {
            return false;
        }

        ErrorCode errorCode = Stream.of(errors)
                .filter(Objects::nonNull)
                .filter(error ->
                    ErrorCode.InvalidResponse.equals(error) ||
                    ErrorCode.MissingResponse.equals(error) ||
                    ErrorCode.BadRequest.equals(error)
        ).findFirst().orElse(null);

        return errorCode != null;
    }
}