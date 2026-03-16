package com.running_platform.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "myapp")
@Getter
@Setter
@Slf4j
public class AppProperties {
    public void init() {
        log.info("Init App Property");
    }

    private String appName = "My Stater App";

    private String officialCompanyName = "";

    private String officialCompanyDomain = "";

    // Mail config
    private Mail mail = new Mail();

    // CORS configuration
    private Cors cors = new Cors();

    // JWT token generation related properties
    private Jwt jwt = new Jwt();

    // Custom specific OAuth2 Properties
    private OAuth2 oAuth2 = new OAuth2();

    // Custom Defaults App/Web/Rest/Misc Properties
    private Defaults defaults = new Defaults();

    private Cloudinary cloudinary = new Cloudinary();

    @Getter
    @Setter
    public static class Mail {
        private String defaultEmailAddress = "thai110504@gmail.com";
        private long verificationCodeExpirationSeconds = 900;
    }

    @Getter
    @Setter
    public static class Jwt {
        private String secretAccessKey;
        private String secretRefreshKey;
        private long expirationAccessMillis = 3600000;
        private long expirationRefreshMillis = 3600000;
        private long shortLivedMillis = 120000;
    }


    @Getter
    @Setter
    public static class Cors {
        private String[] allowedOrigins = {"http://localhost:8080", "http://localhost:5173"};
        private String[] allowedMethods = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
        private String[] allowedHeaders = {"*"};
        private String[] exposedHeaders = {"*"};
    }


    @Getter
    @Setter
    public static class OAuth2 {
        private String[] authorizedRedirectOrigins;
        private int cookieExpireSeconds = 120; // Two minutes
    }


    @Getter
    @Setter
    public static class Defaults {
        private int defaultPageStart = 0;
        private int defaultPageSize = 50;
    }

    @Getter
    @Setter
    public static class Cloudinary {
        private String cloudName = "dcglndksy";
        private String apiKey = "811481571224712";
        private String apiSecret = "pbZKe72EdI7QQDy4pheLKbJkpoo";
    }

}

