// import React, { useState } from "react";
// import { Button } from "../components/ui/button";
// import { Input } from "../components/ui/input";
// import { Card, CardContent } from "../components/ui/card";
// import { CheckCircle2Icon, Github, UploadCloud } from "lucide-react";
// import toast from "react-hot-toast";
// import type RegisterData from "../models/RegisterData";
// import { registerUser } from "../services/AuthService";
// import { useNavigate } from "react-router";
// import { Alert, AlertTitle } from "../components/ui/alert";
// import { Spinner } from "../components/ui/spinner";
// import OAuth2Buttons from "../components/OAuth2Buttons";

import { CheckCircle2Icon, UploadCloud } from "lucide-react";
import { Alert, AlertTitle } from "../components/ui/alert";
import { Card, CardContent } from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Button } from "../components/ui/button";
import OAuth2Buttons from "../components/OAuth2Buttons";
import { useSignup } from "../components/handlers/useSignup";
import { Spinner } from "../components/ui/spinner";
import axios from "axios";
import { useNavigate } from "react-router";

function Signup() {
  const navigate = useNavigate();
  const {
    data,
    loading,
    error,

    handleInputChange,
    handleFileChange,
    handleFromSubmit,

  } = useSignup();

  return (
    <div className="relative flex min-h-screen items-center justify-center overflow-hidden bg-white px-6 py-10 dark:bg-black">

      {/* Background Effects */}
      <div className="absolute left-0 top-0 h-72 w-72 rounded-full bg-cyan-500/20 blur-3xl" />
      <div className="absolute bottom-0 right-0 h-72 w-72 rounded-full bg-violet-500/20 blur-3xl" />

      <div className="absolute inset-0 bg-[linear-gradient(to_right,rgba(120,120,120,0.05)_1px,transparent_1px),linear-gradient(to_bottom,rgba(120,120,120,0.05)_1px,transparent_1px)] bg-[size:60px_60px]" />

      <Card className="relative z-10 w-full max-w-md border border-black/10 bg-white/80 shadow-2xl backdrop-blur-2xl dark:border-white/10 dark:bg-white/5">

        <CardContent className="p-8">

          {/* Logo */}
          <div className="mb-8 flex justify-center">
            <div className="flex h-16 w-16 items-center justify-center rounded-2xl bg-gradient-to-br from-cyan-400 to-violet-500 text-2xl font-bold text-white shadow-lg">
              A
            </div>
          </div>

          {/* Header */}
          <div className="text-center">

            <h1 className="bg-gradient-to-r from-black via-cyan-700 to-violet-700 bg-clip-text text-4xl font-black text-transparent dark:from-white dark:via-cyan-200 dark:to-violet-300">
              Create Account
            </h1>

            <p className="mt-3 text-sm text-black/60 dark:text-white/60">
              Join the future of authentication with secure identity management.
            </p>

            {error && (
              <Alert
                variant="destructive"
                className="mt-4"
              >
                <CheckCircle2Icon />

                <AlertTitle>
                  {axios.isAxiosError(error)
                    ? error.response?.data?.message ||
                    "Registration Failed"
                    : error instanceof Error
                      ? error.message
                      : "Registration Failed"}
                </AlertTitle>

              </Alert>
            )}

          </div>

          {/* Form */}
          <form
            onSubmit={handleFromSubmit}
            className="mt-8 space-y-5"
          >

            {/* Name */}
            <div className="space-y-2">
              <label className="text-sm font-medium text-black dark:text-white">
                Full Name
              </label>

              <Input
                type="text"
                name="name"
                placeholder="Enter your name"
                value={data.name}
                onChange={handleInputChange}
                className="h-12 rounded-xl border-black/10 bg-black/[0.03] dark:border-white/10 dark:bg-white/5"
              />
            </div>

            {/* Email */}
            <div className="space-y-2">
              <label className="text-sm font-medium text-black dark:text-white">
                Email
              </label>

              <Input
                type="email"
                name="email"
                placeholder="Enter your email"
                value={data.email}
                onChange={handleInputChange}
                className="h-12 rounded-xl border-black/10 bg-black/[0.03] dark:border-white/10 dark:bg-white/5"
              />
            </div>

            {/* Mobile */}
            <div className="space-y-2">
              <label className="text-sm font-medium text-black dark:text-white">
                Mobile Number
              </label>

              <Input
                type="tel"
                name="mobileNo"
                placeholder="Enter mobile number"
                value={data.mobileNo}
                onChange={handleInputChange}
                maxLength={10}
                pattern="[0-9]*"
                inputMode="numeric"
                className="h-12 rounded-xl border-black/10 bg-black/[0.03]
                           dark:border-white/10 dark:bg-white/5"
                // This blocks letters instantly when typing
                onKeyPress={(e) => {
                  if (!/[0-9]/.test(e.key)) {
                    e.preventDefault();
                  }
                }}
              />
            </div>

            {/* Password */}
            <div className="space-y-2">
              <label className="text-sm font-medium text-black dark:text-white">
                Password
              </label>

              <Input
                type="password"
                name="password"
                placeholder="Create Password"
                value={data.password}
                onChange={handleInputChange}
                className="h-12 rounded-xl border-black/10 bg-black/[0.03] dark:border-white/10 dark:bg-white/5"
              />
            </div>

            {/* Confirm Password */}
            <div className="space-y-2">
              <label className="text-sm font-medium text-black dark:text-white">
                Confirm Password
              </label>

              <Input
                type="password"
                name="confirmPassword"
                placeholder="Confirm Password"
                value={data.confirmPassword}
                onChange={handleInputChange}
                className="h-12 rounded-xl border-black/10 bg-black/[0.03] dark:border-white/10 dark:bg-white/5"
              />
            </div>

            {/* Upload */}
            <div className="space-y-2">

              <label className="text-sm font-medium text-black dark:text-white">
                Profile Image
              </label>

              <label className="flex h-28 cursor-pointer flex-col items-center justify-center rounded-2xl border border-dashed border-black/20 bg-black/[0.03] transition hover:border-cyan-500 hover:bg-cyan-500/5 dark:border-white/10 dark:bg-white/5">

                <UploadCloud className="mb-2 h-6 w-6 text-cyan-500" />

                <span className="text-sm text-black/60 dark:text-white/60">
                  Upload Profile Image
                </span>

                <input
                  type="file"
                  accept="image/*"
                  className="hidden"
                  onChange={handleFileChange}
                />

              </label>

            </div>

            {/* Submit */}
            <Button
              disabled={loading}
              type="submit"
              className="h-12 w-full rounded-xl bg-gradient-to-r from-cyan-500 to-violet-500 text-white shadow-lg transition-all hover:scale-[1.02]"
            >
              {loading ? (
                <>
                  <Spinner />
                  Please Wait...
                </>
              ) : (
                "Create Account"
              )}
            </Button>

          </form>

          {/* Divider */}
          <div className="my-6 flex items-center gap-4">
            <div className="h-px flex-1 bg-black/10 dark:bg-white/10" />
            <span className="text-xs uppercase tracking-widest text-black/40 dark:text-white/40">
              OR
            </span>
            <div className="h-px flex-1 bg-black/10 dark:bg-white/10" />
          </div>

          <OAuth2Buttons />

          {/* Footer */}
          <p className="mt-8 text-center text-sm text-black/50 dark:text-white/50">
            Already have an account?{" "}
            <span
              onClick={() => navigate("/login")}
              className="cursor-pointer font-medium text-cyan-600 hover:text-cyan-500 dark:text-cyan-300"
            >
              Sign In
            </span>
          </p>

        </CardContent>
      </Card>
    </div>
  );
}

export default Signup;