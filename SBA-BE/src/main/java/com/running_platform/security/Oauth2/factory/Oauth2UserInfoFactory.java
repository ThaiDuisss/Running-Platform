package com.running_platform.security.Oauth2.factory;

import com.running_platform.security.Oauth2.common.CustomAbstractOAuth2UserInfo;

import java.util.Map;

public interface Oauth2UserInfoFactory {
     String getProvider();
     CustomAbstractOAuth2UserInfo create (Map<String, Object> attributes);
}
