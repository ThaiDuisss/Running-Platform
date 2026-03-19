import {
    QueryParamKey
} from "./QueryParamKey";


export const CURRENT_WINDOW_URL = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ":" + window.location.port : "");

const OAUTH2_REDIRECT_URI = QueryParamKey.REDIRECT_URI + '=' + CURRENT_WINDOW_URL + '/login';
const API_ENDPOINT = 'http://localhost:8080';
export class ApiEndpoints {


    static AUTH = {
        CUSTOM_USER_REGISTRATION: '/auth/register',
        CUSTOM_USER_LOGIN: '/auth/login',

        CHECK_VERIFICATION_CODE: '/auth/check-verification-code',
        RESEND_VERIFICATION_EMAIL: '/auth/resend-verification-email',
        FORGOT_PASSWORD: '/auth/send-forgot-password',
        PASSWORD_RESET_SET_NEW_PASS: '/auth/process-password-reset',


        // '/oauth2/authorize/google?redirect_uri=' + API_ENDPOINT
        GOOGLE_AUTH: API_ENDPOINT + '/oauth2/authorize/google?' + OAUTH2_REDIRECT_URI,
        FACEBOOK_AUTH: API_ENDPOINT + '/oauth2/authorize/facebook?' + OAUTH2_REDIRECT_URI,
        GITHUB_AUTH: API_ENDPOINT + '/oauth2/authorize/github?' + OAUTH2_REDIRECT_URI,

        LOGOUT: '/logout',

    };

    static USERS_API_ENDPOINTS = {
        MAIN: '/users',
        ME: '/my-info',
        SEARCH: '/users/search'
    };

    static PLANS_API_ENDPOINTS = {
        MAIN: '/plans',
        BY_MONTH: '/plans',
        CREATE: '/plans',
        UPDATE: '/plans',
        DELETE: '/plans',
        GET_BY_ID: '/plans'
    };

}