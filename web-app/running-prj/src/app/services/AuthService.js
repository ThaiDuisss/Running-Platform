import axiosClient from "@/shared/services/axiosClient";
import {
    ApiEndpoints
} from "./AppUrlConstant";
export const authService = {
    login: async (payload) => {

        if (
            payload.username === "admin@example.com" &&
            payload.password === "123456"
        ) {
            return {
                user: {
                    id: 1,
                    name: "John",
                    email: "admin@example.com",
                },
                accessToken: "ACCESS_TOKEN_SAMPLE",
            };
        }

        const res = await axiosClient.post("/auth/login", payload);
        return res.data;
    },

    register: async (payload) => {
        const res = await axiosClient.post("/auth/register", payload);
        return res.data;
    },

    refreshToken: async () => {
        const res = await axiosClient.post("/auth/refresh");
        return res.data;
    },

    findByEmail: async (email) => {
        const res = await axiosClient.get(`/auth/find-by-email`, {
            params: {
                email
            },
        });
        return res.data;
    },

    logout: () => {
        console.log("Logging out...");
        localStorage.removeItem("ACCESS-TOKEN");
        localStorage.removeItem("userInfo");
    },
    
    forgotPassword: async (email) => {
        const res = await axiosClient.post(`/auth/forgot-password?email=${email}`);
        return res.data;
    },

    resetPassword: async ({ token, newPassword }) => {
        const res = await axiosClient.post("/auth/reset-password", { token, newPassword });
        return res.data;
    },
    

    signinWithSocialPlatform: async (platform, routeQueryParams) => {
        console.log("Signing in with platform:", platform, routeQueryParams);
        const queryString = Object.entries(routeQueryParams).map(([key, value]) => `${key}=${value}`).join('&');
        const appendQueryParams = queryString ? `&${queryString}` : '';
        let oauth2Url = '';
        switch (platform) {
            case "google":
                oauth2Url = ApiEndpoints.AUTH.GOOGLE_AUTH + appendQueryParams;
                break;
            case "facebook":
                oauth2Url = ApiEndpoints.AUTH.FACEBOOK_AUTH + appendQueryParams;
                break;
            case "github":
                oauth2Url = ApiEndpoints.AUTH.GITHUB_AUTH + appendQueryParams;
                break;
            default:
                throw new Error("Unsupported platform: " + platform);
        }
        window.location.href = oauth2Url;
    }
}