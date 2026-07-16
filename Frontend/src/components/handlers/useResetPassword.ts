import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router";
import toast from "react-hot-toast";

import {
  resetPassword,
  sendOtp
} from "../../services/AuthService";

export const useResetPassword = () => {

  const [params] = useSearchParams();

  const token = params.get("token");

  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    otp: "",
    password: "",
    confirmPassword: "",
  });

  const [loading, setLoading] = useState(false);

  const [timeLeft, setTimeLeft] = useState(60);

  const [otpSent, setOtpSent] = useState(false);

  /*
   * Reset page open hote hi OTP send hoga
   */
  useEffect(() => {

    if (!token) {
      toast.error("Invalid reset link");
      return;
    }

    const sendInitialOtp = async () => {
      try {

        await sendOtp(token);

        setOtpSent(true);

        toast.success(
          "OTP sent to your registered email"
        );

        setTimeLeft(60);

      } catch (error) {
        toast.error(
          "Unable to send OTP"
        );
      }
    };

    sendInitialOtp();

  }, [token]);

  /*
   * Countdown timer
   */
  useEffect(() => {

    if (timeLeft <= 0) return;

    const interval = setInterval(() => {

      setTimeLeft((prev) => prev - 1);

    }, 1000);

    return () => clearInterval(interval);

  }, [timeLeft]);

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {

    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  /*
   * Resend OTP
   */
  const handleResendOtp = async () => {

    if (!token) return;

    try {

      await sendOtp(token);

      toast.success(
        "OTP sent successfully"
      );

      setTimeLeft(60);

    } catch (error: any) {

      toast.error(
        error?.response?.data ||
        "Unable to resend OTP"
      );
    }
  };

  /*
   * Reset Password
   */
  const handleSubmit = async (
    e: React.FormEvent
  ) => {

    e.preventDefault();

    if (!token) {
      toast.error("Invalid reset link");
      return;
    }

    if (!formData.otp.trim()) {
      toast.error("OTP is required");
      return;
    }

    if (formData.password.length < 8) {
      toast.error(
        "Password must be at least 8 characters"
      );
      return;
    }

    if (
      formData.password !==
      formData.confirmPassword
    ) {
      toast.error(
        "Passwords do not match"
      );
      return;
    }

    try {

      setLoading(true);

      await resetPassword(
        token,
        formData.otp,
        formData.password
      );

      toast.success(
        "Password updated successfully"
      );

      navigate("/login");

    } catch (error: any) {

      toast.error(
        error?.response?.data ||
        "Invalid OTP or expired reset link"
      );

    } finally {

      setLoading(false);
    }
  };

  return {
    formData,
    loading,
    timeLeft,
    otpSent,

    handleInputChange,
    handleSubmit,
    handleResendOtp,
  };
};