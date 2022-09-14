package com.example.backratelimit.config.recaptcha.v3;

import com.example.backratelimit.exception.LockedClientException;
import com.example.backratelimit.exception.RateLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

@Slf4j
public abstract class AbstractCaptchaService implements RecaptchaV3Service {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected RecaptchaV3Config recaptchaV3Config;

    @Autowired
    protected RecaptchaAttemptService recaptchaAttemptService;

    @Autowired
    protected RestOperations restTemplate;

    protected static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    protected static final String RECAPTCHA_URL_TEMPLATE = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";

    @Override
    public String getReCaptchaSite() {
        return recaptchaV3Config.getSite();
    }

    @Override
    public String getReCaptchaSecret() {
        return recaptchaV3Config.getSecret();
    }

    protected void securityCheck(final String response) {
        log.debug("Attempting to validate response {}", response);
        String clientIP = getClientIP();
        boolean isSanitizedString = responseSanityCheck(response);
        boolean isBlockedClientIp = recaptchaAttemptService.isBlocked(clientIP);

        if (isBlockedClientIp) {
            throw new RateLimitException();
        }

        if (!isSanitizedString) {
            throw new LockedClientException();
        }

    }

    protected boolean responseSanityCheck(final String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    protected String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}