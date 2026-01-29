package com.banking.cliente.ratelimiter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    @Autowired
    private RateLimitingProperty rateLimitingProperty;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private final Supplier<BucketConfiguration> configSupplier = () -> {
        Bandwidth limit = Bandwidth.builder()
                .capacity(rateLimitingProperty.getRequestsPerMinute())
                .refillGreedy(rateLimitingProperty.getRequestsPerMinute(), Duration.ofMinutes(1))
                .build();

        return BucketConfiguration.builder()
                .addLimit(limit)
                .build();
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return rateLimitingProperty.getExcludedPaths().stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        Bucket bucket = resolveBucket(ip);

        if (!bucket.tryConsume(1)) {
            long waitForRefillNanos = bucket.getAvailableTokens() == 0
                    ? bucket.estimateAbilityToConsume(1).getNanosToWaitForRefill()
                    : 0;
            long retryAfterSeconds = (long) Math.ceil(waitForRefillNanos / 1_000_000_000.0);

            response.setStatus(429); // 429 Too Many Requests
            response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
            response.getWriter().write("Too many requests. Try again in " + retryAfterSeconds + " seconds.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Bucket resolveBucket(String key) {
        Bandwidth[] limits = configSupplier.get().getBandwidths();
        return buckets.computeIfAbsent(key, k -> Bucket.builder()
                .addLimit(limits[0])
                .build());
    }
}