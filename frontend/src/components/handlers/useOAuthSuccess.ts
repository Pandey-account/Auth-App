import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useNavigate } from "react-router";

import useAuth from "../../Auth/store";

import { refreshToken }
from "../../services/AuthService";

export const useOAuthSuccess = () => {

  const [loading, setLoading] =
    useState(false);

  const navigate = useNavigate();

  const changeLocalLoginData =
    useAuth(
      (state) =>
        state.changeLocalLoginData
    );

  useEffect(() => {

    const loadToken = async () => {

      try {

        setLoading(true);

        const response =
          await refreshToken();

        changeLocalLoginData(
          response.accessToken,
          response.user,
          true
        );

        toast.success(
          "Login Success"
        );

        navigate("/dashboard");

      } catch (error) {

        toast.error(
          "Login Failed"
        );

      } finally {

        setLoading(false);
      }
    };

    loadToken();

  }, []);

  return {
    loading,
  };
};