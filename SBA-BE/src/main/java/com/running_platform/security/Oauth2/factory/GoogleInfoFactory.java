package com.running_platform.security.Oauth2.factory;

import com.running_platform.security.Oauth2.common.CustomAbstractOAuth2UserInfo;
import com.running_platform.security.Oauth2.common.GoogleCustomAbstractOAuth2UserInfo;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class GoogleInfoFactory implements Oauth2UserInfoFactory {
    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public CustomAbstractOAuth2UserInfo create(Map<String, Object> attributes) {
        return new GoogleCustomAbstractOAuth2UserInfo(attributes);
    }
}
