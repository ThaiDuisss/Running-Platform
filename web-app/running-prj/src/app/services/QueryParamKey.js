export const QueryParamKey = {

    TOKEN: "token",
    ERROR: "error",
    EMAIL: "email",

    // Authentication constants, API Authenntication flow will process this param ob the basis of the implementation logic
    REDIRECT_URI: "redirect_uri",
    ORIGINAL_REQUEST_URI: "original_request_uri",

    // Email Verification QueryParam Keys
    IS_PROCESS_VERIFY_EMAIL: "isProcessVerifyEmail",
    VERIFICATION_CODE: "verificationCode",
    REGISTERED_PROVIDER_NAME: "registeredProviderName",

    // Password reset query params
    IS_PROCESS_PASSWORD_RESET: "isProcessPasswordReset",
    FORGOT_PASSWORD_VER_CODE: "forgotPasswordVerCode"

}

export const QueryParamUIKey = {

    INFO_MESSAGE: "infoMessage",

    REGISTRATION_SUCCESSFUL: "registrationSuccess",
    EMAIL_VERIFICATION_SUCCESSFUL: "emailVerificationSuccess",
    PASSWORD_RESET_SUCCESSFUL: "passwordResetSuccess",
}


export const MESSAGE_RESPONSE_CONSTANTS = {

    Success_Action: 'Action Success',
    Operation_Failed_MSG: 'Oops! Something went wrong !!',

};

export const ERROR_CODES_CONSTANTS = {
    ServerDown: 0,
    BadRequest: 400,
    Unauthorized: 401,
    ResourceNotFound: 404,
    InternalServerError: 500,
};