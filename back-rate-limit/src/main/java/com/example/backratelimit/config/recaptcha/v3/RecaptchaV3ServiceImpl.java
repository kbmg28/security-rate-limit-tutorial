package com.example.backratelimit.config.recaptcha.v3;

import com.example.backratelimit.exception.LockedClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.net.URI;

import static java.util.Optional.ofNullable;

@Service
@Slf4j
public class RecaptchaV3ServiceImpl extends AbstractCaptchaService{

    @Override
    public void processResponse(String response, final String action) {
        securityCheck(response);
        String url = String.format(RECAPTCHA_URL_TEMPLATE, getReCaptchaSecret(), response, getClientIP());
        final URI verifyUri = URI.create(url);

        try {
            final GoogleResponse googleResponseParse = restTemplate.getForObject(verifyUri, GoogleResponse.class);

            ofNullable(googleResponseParse).ifPresentOrElse(googleResponse -> {

                boolean isSameAction = ofNullable(googleResponse.getAction())
                        .map(action::equals)
                        .isPresent();

                if (!googleResponse.isSuccess() || !isSameAction || googleResponse.getScore() < recaptchaV3Config.getThreshold()) {
                    recaptchaAttemptService.reCaptchaFailed(getClientIP());
                    throw new LockedClientException();
                }
            }, () -> {
                throw new LockedClientException();
            });
        } catch (RestClientException rce) {
            throw new LockedClientException();
        }
        recaptchaAttemptService.reCaptchaSucceeded(getClientIP());
    }
}