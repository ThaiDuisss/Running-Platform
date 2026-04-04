package com.running_platform.security.Oauth2.factory;

import com.running_platform.security.Oauth2.common.CustomAbstractOAuth2UserInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Oauth2UserInfoFactoryProvider {
    private final Map<String, Oauth2UserInfoFactory> factories;

    public Oauth2UserInfoFactoryProvider( List<Oauth2UserInfoFactory> factories) {
        this.factories = factories.stream().collect(Collectors.toMap(Oauth2UserInfoFactory::getProvider, f -> f));
    }

    public CustomAbstractOAuth2UserInfo createOauth2Entity(String registerProvider, Map<String, Object> attributes) {
        Oauth2UserInfoFactory oauth2UserInfoFactory = factories.get(registerProvider);
        if(oauth2UserInfoFactory == null) {
            throw new RuntimeException("Unsupported provider");
        }
        return oauth2UserInfoFactory.create(attributes);
    }

}
