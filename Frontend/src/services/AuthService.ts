import apiClient from "../config/ApiClient";
import type LoginData from "../models/LoginData";
import type LoginResponseData from "../models/LoginResponseData";
import type { PasswordData } from "../models/PasswordData";
import type User from "../models/User";

// register function
export const registerUser = async (formData: FormData) => {
    //api call to server to save data

    const response = await apiClient.post(`/auth/register`, formData);
    return response.data;
};

//login

export const loginUser = async (loginData: LoginData) => {

    const response = await apiClient.post<LoginResponseData>("/auth/login", loginData);

    return response.data;
};

//logout
export const logoutUser = async () => {
    const response = await apiClient.post("/auth/logout");
    return response.data;
};

//get current login user

export const getCurrentUser = async (emailId: string | undefined) => {
    const response = await apiClient.get<User>(`/users/email/${emailId}`);
    return response.data;
}

//refresh Token

export const refreshToken = async () => {
    const response = await apiClient.post<LoginResponseData>(`/auth/refresh`)
    return response.data
};

//get profile picture
export const imageUrl = async () => {

    const response = await apiClient.get(
        `users/profile-picture`,
        {
            responseType: "blob",
        }
    );
    return response.data;
}

// update Profile 
export const updateProfile = async (
    formData: FormData
) => {
    const response = await apiClient.put(
        `/users/profile`,formData)

    return response.data;

};

//update password
export const updatePassword = async (passwordData: PasswordData
) => {
    const response = await apiClient.put(
        `/users/change-password`,passwordData)

    return response.data;

};

// delete profile 
export const deleteProfile = async () => {
 const response = await apiClient.delete(`/users/profile`)

 return response.data;
}

// reset password 
export const resetPassword = async (
  token: string,
  otp: string,
  password: string
) => {
  const response = await apiClient.post(
    "/auth/reset-password",
    {
      token,
      otp,
      password,
    }
  );

  return response.data;
};


//forget password
export const forgotPassword = async (
  identifier: string
) => {
  const response = await apiClient.post(
    "/auth/forgot-password",
    {
      identifier,
    }
  );

  return response.data;
};

//send otp 
export const sendOtp = async (
  token: string
) => {
  const response = await apiClient.post(
    "/auth/resend-otp",
    {
      token,
    }
  );

  return response.data;
};
