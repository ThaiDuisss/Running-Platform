package com.running_platform.security.Oauth2.factory;

import com.running_platform.security.Oauth2.common.CustomAbstractOAuth2UserInfo;
import com.running_platform.security.Oauth2.common.FacebookCustomAbstractOAuth2UserInfo;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class FaceBookInfoFactory implements Oauth2UserInfoFactory {
    @Override
    public String getProvider() {
        return "facebook";
    }

    @Override
    public CustomAbstractOAuth2UserInfo create(Map<String, Object> attributes) {
        return new FacebookCustomAbstractOAuth2UserInfo(attributes);
    }
}
