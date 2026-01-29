package com.banking.cliente.ratelimiter;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ConfigurationProperties(prefix = "rate.limit")
@Component
public class RateLimitingProperty {

    private int requestsPerMinute;
    private List<String> excludedPaths;

}
