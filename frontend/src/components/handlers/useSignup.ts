import { useState } from "react";
import { useNavigate } from "react-router";
import toast from "react-hot-toast";
import { registerUser } from "../../services/AuthService";
import type RegisterData from "../../models/RegisterData";

export const useSignup = () => {
  const [data, setData] = useState<RegisterData>({
    name: "",
    email: "",
    mobileNo: "",
    password: "",
    confirmPassword: "",
    imageFile: null,

  });

  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<any>(null);
  

  const navigate = useNavigate();

  //text input,email,password,number,textarea
  //handling for change
  const handleInputChange = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { name, value } = event.target;

    setData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleFileChange = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    const file = event.target.files?.[0];

    setData((prev) => ({
      ...prev,
      imageFile: file || null,
    }));
  };

  //handling from submit
  const handleFromSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    console.log("FORM SUBMITTED");
    console.log(data);

    // Name Validation
    if (!data.name.trim()) {
      toast.error("Name is required");
      return;
    }

    if (data.name.trim().length < 3) {
      toast.error("Name must be at least 3 characters");
      return;
    }

    // Email Validation
    if (!data.email.trim()) {
      toast.error("Email is required");
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(data.email)) {
      toast.error("Invalid email address");
      return;
    }
    // Mobile Validation
    if (!data.mobileNo.trim()) {
      toast.error("Mobile number is required");
      return;
    }

    const mobileRegex = /^[6-9]\d{9}$/;

    if (!mobileRegex.test(data.mobileNo)) {
      toast.error(
        "Please enter a valid 10-digit mobile number"
      );
      return;
    }
    // Password Validation
    if (!data.password.trim()) {
      toast.error("Password is required");
      return;
    }

    if (data.password.length < 8) {
      toast.error("Password must be at least 8 characters");
      return;
    }

    if (data.confirmPassword.length < 8) {
      toast.error("confirmPassword must be at least 8 characters");
      return;
    }

    if (data.password !== data.confirmPassword) {
      toast.error("Password and Confirm Password do not match");
      return;
    }

    // Image Validation
    if (!data.imageFile) {
      toast.error("Profile image is required");
      return;
    }

    toast.success("Validation Passed ✅");

    // API call / registration logic here
    try {
      setLoading(true);

      const formData = new FormData();

      formData.append("name", data.name);
      formData.append("email", data.email);
      formData.append("mobileNo", data.mobileNo);
      formData.append("password", data.password);
      formData.append("confirmPassword", data.confirmPassword);

      if (data.imageFile) {
        formData.append("imageFile", data.imageFile);
      }

      const result = await registerUser(formData);

      console.log(result);

      toast.success("User registered successfully...");

      setData({
        name: "",
        email: "",
        mobileNo: "",
        password: "",
        confirmPassword: "",
        imageFile: null,
      });

      navigate("/login");

    } catch (error: any) {
      console.log(error);

      toast.error("Error in registering the user...");

      setError(error);

    } finally {
      setLoading(false);
    }
  }

  return {
    data,
    loading,
    error,

    handleInputChange,
    handleFileChange,
    handleFromSubmit,
  };
};