import axiosClient from "@/shared/services/axiosClient";
import {
    useNavigate
} from "react-router-dom";

const navigate = useNavigate();
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
        localStorage.removeItem("token");
        localStorage.removeItem("user");
    },

};