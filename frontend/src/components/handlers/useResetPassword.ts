import {
  useEffect,
  useState,
} from "react";

import {
  useNavigate,
  useSearchParams,
} from "react-router";

import toast from "react-hot-toast";

import {
  resetPassword,
  sendInitialOtp,
  resendOtp,
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

  const [loading, setLoading] =
    useState(false);

  const [timeLeft, setTimeLeft] =
    useState(60);

  const [otpSent, setOtpSent] =
    useState(false);

  const getErrorMessage = (
    error: any
  ): string => {

    const data =
      error?.response?.data;

    if (typeof data === "string") {
      return data;
    }

    if (data?.message) {
      return data.message;
    }

    if (error?.message) {
      return error.message;
    }

    return "Something went wrong. Please try again.";
  };

  useEffect(() => {

    if (!token) {
      toast.error("Invalid reset link");
      return;
    }

    const sendInitialOtpRequest =
      async () => {

        try {

          await sendInitialOtp(token);

          setOtpSent(true);

          setTimeLeft(60);

          toast.success(
            "OTP sent to your registered email"
          );

        } catch (error: any) {

          console.error(
            "Initial OTP Error:",
            error?.response?.data || error
          );

          toast.error(
            getErrorMessage(error)
          );
        }
      };

    sendInitialOtpRequest();

  }, [token]);

  useEffect(() => {

    if (timeLeft <= 0) {
      return;
    }

    const interval =
      setInterval(() => {

        setTimeLeft((prev) => {

          if (prev <= 1) {
            return 0;
          }

          return prev - 1;
        });

      }, 1000);

    return () =>
      clearInterval(interval);

  }, [timeLeft]);

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {

    const {
      name,
      value,
    } = e.target;

    if (name === "otp") {

      const numericValue =
        value
          .replace(/\D/g, "")
          .slice(0, 6);

      setFormData((prev) => ({
        ...prev,
        otp: numericValue,
      }));

      return;
    }

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleResendOtp = async () => {

    if (!token) {
      toast.error("Invalid reset link");
      return;
    }

    if (loading) {
      return;
    }

    try {

      setLoading(true);

      await resendOtp(token);

      setTimeLeft(60);

      toast.success(
        "OTP sent successfully"
      );

    } catch (error: any) {

      console.error(
        "Resend OTP Error:",
        error?.response?.data || error
      );

      toast.error(
        getErrorMessage(error)
      );

    } finally {

      setLoading(false);
    }
  };

  const handleSubmit = async (
    e: React.FormEvent<HTMLFormElement>
  ) => {

    e.preventDefault();

    if (loading) {
      return;
    }

    if (!token) {
      toast.error(
        "Invalid or missing reset link"
      );
      return;
    }

    if (!formData.otp.trim()) {
      toast.error("OTP is required");
      return;
    }

    if (formData.otp.length !== 6) {
      toast.error(
        "OTP must be 6 digits"
      );
      return;
    }

    if (!formData.password) {
      toast.error(
        "Password is required"
      );
      return;
    }

    if (formData.password.length < 8) {
      toast.error(
        "Password must be at least 8 characters"
      );
      return;
    }

    if (!formData.confirmPassword) {
      toast.error(
        "Please confirm your password"
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

      setTimeout(() => {
        navigate("/login");
      }, 1000);

    } catch (error: any) {

      console.error(
        "Reset Password Error:",
        error?.response?.data || error
      );

      toast.error(
        getErrorMessage(error)
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
