import { useState } from "react";
import { useNavigate } from "react-router";
import axios from "axios";
import toast from "react-hot-toast";

import useAuth from "../../Auth/store";
import { updatePassword } from "../../services/AuthService";

export const useChangePassword = () => {

  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    oldPassword: "",
    newPassword: "",
    confirmPassword: "",
  });

  const [loading, setLoading] = useState(false);

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleFormSubmit = async (
    e: React.FormEvent
  ) => {
    e.preventDefault();

    if (
      formData.newPassword !==
      formData.confirmPassword
    ) {
      toast.error("Passwords do not match");
      return;
    }

    try {
      setLoading(true);

      await updatePassword(formData);

      toast.success(
        "Password changed successfully"
      );

      await useAuth.getState().logout();

      navigate("/login");

    } catch (error) {

      if (axios.isAxiosError(error)) {
        toast.error(
          error.response?.data?.message ??
          "Unable to change password"
        );
      } else {
        toast.error(
          "Unexpected error occurred"
        );
      }

    } finally {
      setLoading(false);
    }
  };

  return {
    formData,
    loading,

    handleInputChange,
    handleFormSubmit,

    navigate,
  };
};