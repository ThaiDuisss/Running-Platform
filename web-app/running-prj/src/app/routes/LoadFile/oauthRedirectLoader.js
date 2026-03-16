export const oauthRedirectLoader = () => {

    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");

    if (token) {
        localStorage.setItem("access_token", token);
    }

};