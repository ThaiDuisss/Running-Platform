import React, { createContext, useEffect, useState, } from 'react'
import { authService } from '../services/AuthService';

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
            const res = await getUserInfo();
            localStorage.setItem("userInfo", JSON.stringify(res.data));
            const newUser = res.data.data;
            localStorage.setItem("userInfo", JSON.stringify(newUser));

            setUser(newUser);
        } catch (error) {
            setUser(null);
            throw error;
        }
    }

    // 🔐 LOGIN
    const login = async (payload) => {
        try {
            const data = await authService.login(payload);
            const access_token = data.data;



            localStorage.setItem("access_token", access_token);

            checkAuth();
        } catch (error) {
            throw error;
        }
    };

    // 🚪 LOGOUT
    const logout = () => {
        localStorage.removeItem("access_token");
        localStorage.removeItem("userInfo");
        authService.logout();
        setUser(null);
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
