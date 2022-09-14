package com.example.backratelimit.config.recaptcha.v3;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class RecaptchaAttemptService {

    @Value("${recaptcha.cache.max.retry.ip}")
    private Integer MAX_ATTEMPT;

    private LoadingCache<String, Integer> attemptsCache;

    public RecaptchaAttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(final String key) {
                return 0;
            }
        });
    }

    public void reCaptchaSucceeded(final String key) {
        attemptsCache.invalidate(key);
    }

    public void reCaptchaFailed(final String key) {
        int attempts = attemptsCache.getUnchecked(key);
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(final String key) {
        return attemptsCache.getUnchecked(key) >= MAX_ATTEMPT;
    }
}