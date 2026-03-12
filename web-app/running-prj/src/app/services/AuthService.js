import axiosClient from "@/shared/services/axiosClient";

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
    

};