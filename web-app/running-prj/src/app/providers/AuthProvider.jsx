import React, { createContext, useEffect, useState, } from 'react'
import { authService } from '../services/authService';
import { ApiEndpoints } from '../services/AppUrlConstant';
import axiosClient from '@/shared/services/axiosClient';
import { getUserInfo } from '@/features/admin/users/services/UserService';
export const AuthDataContext = createContext();
export const AuthActionContext = createContext();

const AuthProvider = ({ children }) => {
    const [user, setUser] = useState({});
    const [theme, setTheme] = useState("Light");
    const [language, setLanguage] = useState("EN")

    useEffect(() => {
        const storedUser = localStorage.getItem("userInfo");
        const storedTheme = localStorage.getItem("theme");
        const storedLang = localStorage.getItem("language");

        if (storedUser) setUser(JSON.parse(storedUser));
        if (storedTheme) setTheme(storedTheme);
        if (storedLang) setLanguage(storedLang);

    }, []);


    const checkAuth = async () => {
        try {
            console.log("Checking authentication..." + ApiEndpoints.USERS_API_ENDPOINTS.ME);
            const res = await getUserInfo();
            console.log("Authentication check successful:", res);
            setUser(res.data);
        } catch (error) {
            setUser(null);
            console.error("Authentication check failed:", error);
            throw error;
        }
    }

    // 🔐 LOGIN
    const login = async (payload) => {
        try {
            const data = await authService.login(payload);
            console.log("Login successful:", data);
            const access_token = data.data;

<<<<<<< HEAD
            localStorage.setItem("ACCESS-TOKEN", tokenResponse.accessToken);
            localStorage.setItem("userInfo", JSON.stringify(userResponse));
=======
            localStorage.setItem("AccessToken", JSON.stringify(access_token));
>>>>>>> cfcbcb1 (update login)

            checkAuth();
        } catch (error) {
            throw error;
        }
    };

    // 🚪 LOGOUT
    const logout = () => {
        localStorage.removeItem("ACCESS-TOKEN");
        localStorage.removeItem("userInfo");
        authService.logout();
        setUser("");
    };

    // 👤 CHANGE USER
    const changeUser = (newUser) => {
        setUser(newUser);
        localStorage.setItem("userInfo", JSON.stringify(newUser));
    };

    // 🎨 CHANGE THEME
    const changeTheme = (newTheme) => {
        setTheme(newTheme);
        localStorage.setItem("theme", newTheme);
    };

    // 🌍 CHANGE LANGUAGE
    const changeLanguage = (newLang) => {
        setLanguage(newLang);
        localStorage.setItem("language", newLang);
    };
    const stateValues = { user, language, theme };
    const actionValues = {
        login,
        logout,
        changeUser,
        changeTheme,
        changeLanguage,
        checkAuth
    };

    return (
        <>
            <AuthDataContext.Provider value={stateValues}>
                <AuthActionContext.Provider value={actionValues}>
                    {children}
                </AuthActionContext.Provider>
            </AuthDataContext.Provider>
        </>
    )
}

export default AuthProvider
