
import { create } from "zustand";
import { persist } from "zustand/middleware";

import type LoginData from "../models/LoginData";
import type User from "../models/User";

import {
  loginUser,
  logoutUser,
} from "../services/AuthService";

const LOCAL_KEY = "app_state";

type LoginResponseData = {
  accessToken: string;
  user: User;
};

type AuthState = {
  accessToken: string | null;
  user: User | null;
  authStatus: boolean;
  authLoading: boolean;

  login: (
    loginData: LoginData
  ) => Promise<LoginResponseData>;

  logout: (silent?: boolean) => Promise<void>;

  checkLogin: () => boolean;

  setUser: (user: User) => void;

  setAccessToken: (
    accessToken: string | null
  ) => void;

  changeLocalLoginData: (
    accessToken: string,
    user: User,
    authStatus: boolean
  ) => void;

  clearAuth: () => void;
};

const useAuth = create<AuthState>()(
  persist(
    (set, get) => ({
      accessToken: null,
      user: null,
      authStatus: false,
      authLoading: false,

      login: async (loginData) => {
        set({ authLoading: true });

        try {
          const response =
            await loginUser(loginData);

          set({
            accessToken:
              response.accessToken,
            user: response.user,
            authStatus: true,
            authLoading: false,
          });

          return response;
        } catch (error) {
          set({ authLoading: false });
          throw error;
        }
      },

      logout: async (
        silent = false
      ) => {
        const token =
          get().accessToken;

        set({
          accessToken: null,
          user: null,
          authStatus: false,
          authLoading: false,
        });

        localStorage.removeItem(
          LOCAL_KEY
        );

        if (!silent && token) {
          try {
            await logoutUser();
          } catch (error) {
            console.error(
              "Logout API Error:",
              error
            );
          }
        }
      },

      checkLogin: () => {
        return !!(
          get().accessToken &&
          get().authStatus
        );
      },

      setUser: (user) => {
        set({ user });
      },

      setAccessToken: (
        accessToken
      ) => {
        set({ accessToken });
      },

      changeLocalLoginData: (
        accessToken,
        user,
        authStatus
      ) => {
        set({
          accessToken,
          user,
          authStatus,
        });
      },

      clearAuth: () => {
        set({
          accessToken: null,
          user: null,
          authStatus: false,
          authLoading: false,
        });

        localStorage.removeItem(
          LOCAL_KEY
        );
      },
    }),
    {
      name: LOCAL_KEY,

      partialize: (state) => ({
        accessToken:
          state.accessToken,
        user: state.user,
        authStatus:
          state.authStatus,
      }),
    }
  )
);

export default useAuth;
