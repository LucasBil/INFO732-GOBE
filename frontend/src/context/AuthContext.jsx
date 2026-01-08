import React, { createContext, useState, useContext, useEffect } from 'react';
import { api } from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    // Check if user is persisted in localStorage
    useEffect(() => {
        const storedUser = localStorage.getItem('gobe_user');
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }
        setLoading(false);
    }, []);

    const login = async (email) => {
        try {
            const user = await api.login(email);
            setUser(user);
            localStorage.setItem('gobe_user', JSON.stringify(user));
            return user;
        } catch (error) {
            console.error("Login failed", error);
            throw error;
        }
    };

    const register = async (data) => {
        try {
            const user = await api.createProfile(data);
            setUser(user);
            localStorage.setItem('gobe_user', JSON.stringify(user));
            return user;
        } catch (error) {
            console.error("Registration failed", error);
            throw error;
        }
    };

    const logout = () => {
        setUser(null);
        localStorage.removeItem('gobe_user');
    };

    return (
        <AuthContext.Provider value={{ user, login, register, logout, loading }}>
            {!loading && children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
