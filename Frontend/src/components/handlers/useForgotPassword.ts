import { useState } from "react";
import toast from "react-hot-toast";
import { forgotPassword } from "../../services/AuthService";

export const useForgotPassword = () => {

  const [identifier, setIdentifier] =
    useState("");

  const [loading, setLoading] =
    useState(false);

  const [error, setError] =
    useState<string | null>(null);

  const handleSubmit = async (
    e: React.FormEvent
  ) => {

    e.preventDefault();

    try {

      setLoading(true);
      setError(null);

      await forgotPassword(identifier);

      toast.success(
        "Reset link sent successfully"
      );

    } catch (e) {
console.log(e)
      setError(
        "Unable to send reset link"
      );

      toast.error(
        "Unable to send reset link"
      );

    } finally {

      setLoading(false);
    }
  };

  return {
    identifier,
    setIdentifier,
    loading,
    error,
    handleSubmit,
  };
};