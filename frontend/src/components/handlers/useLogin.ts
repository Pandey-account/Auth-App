import { useState } from "react";
import { useNavigate } from "react-router";
import toast from "react-hot-toast";
import useAuth from "../../Auth/store";
import type LoginData from "../../models/LoginData";

export const useLogin = () => {

    const navigate = useNavigate();
    const login = useAuth((state) => state.login);

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<any>(null);

    const [loginData, setLoginData] = useState<LoginData>({
        email: "",
        password: "",
    });

    const handleInputChange = (
        event: React.ChangeEvent<HTMLInputElement>
    ) => {
        const { name, value } = event.target;

        setLoginData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleFormSubmit = async (
        event: React.FormEvent
    ) => {
        event.preventDefault();

        try {
            setLoading(true);

            await login(loginData);

            toast.success("Login Successful");

            navigate("/dashboard");

        } catch (error: any) {
            setError(error);
        } finally {
            setLoading(false);
        }
    };

    return {
        loginData,
        setLoginData,
        loading,
        error,
        handleInputChange,
        handleFormSubmit,
    };
};