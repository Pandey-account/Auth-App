import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { useLogin } from "../components/handlers/useLogin";
import { Card, CardContent } from "../components/ui/card";
import { Alert, AlertTitle } from "../components/ui/alert";
import { CheckCircle2Icon } from "lucide-react";
import { Spinner } from "../components/ui/spinner";
import OAuth2Buttons from "../components/OAuth2Buttons";
import { useNavigate } from "react-router";

function Login() {
    const navigate = useNavigate();
    const {
        loginData,
        loading,
        error,
        handleInputChange,
        handleFormSubmit,
    } = useLogin();

    return (

        <div className="relative flex min-h-screen items-center justify-center overflow-hidden bg-white px-6 py-10 transition-colors dark:bg-black">

            {/* Background Glow */}
            <div className="absolute left-0 top-0 h-72 w-72 rounded-full bg-cyan-500/20 blur-3xl" />
            <div className="absolute bottom-0 right-0 h-72 w-72 rounded-full bg-violet-500/20 blur-3xl" />

            {/* Grid Overlay */}
            <div className="absolute inset-0 bg-[linear-gradient(to_right,rgba(120,120,120,0.05)_1px,transparent_1px),linear-gradient(to_bottom,rgba(120,120,120,0.05)_1px,transparent_1px)] bg-[size:60px_60px]" />

            {/* Login Card */}
            <Card className="relative z-10 w-full max-w-md border border-black/10 bg-white/70 shadow-2xl backdrop-blur-2xl dark:border-white/10 dark:bg-white/5">
                <CardContent className="p-8">

                    {/* Logo */}
                    <div className="mb-8 flex items-center justify-center">
                        <div className="flex h-14 w-14 items-center justify-center rounded-2xl bg-gradient-to-br from-cyan-400 to-violet-500 text-2xl font-bold text-white shadow-lg shadow-cyan-500/30">
                            A
                        </div>
                    </div>

                    {/* Heading */}
                    <div className="text-center">
                        <h1 className="bg-gradient-to-r from-black via-cyan-700 to-violet-700 bg-clip-text text-4xl font-black tracking-tight text-transparent dark:from-white dark:via-cyan-200 dark:to-violet-300">
                            Welcome Back
                        </h1>

                        <p className="mt-3 text-sm leading-relaxed text-black/60 dark:text-white/60">
                            Securely access your account and continue your futuristic authentication journey.
                        </p>

                        {/* error section */}
                        {error && (
                            <Alert variant={'destructive'} className="mt-4">
                                <CheckCircle2Icon />
                                <AlertTitle>
                                    {error.response
                                        ? error?.response?.data?.message
                                        : error?.message}
                                </AlertTitle>

                            </Alert>
                        )}
                    </div>

                    {/* Form */}
                    <form onSubmit={handleFormSubmit} className="mt-8 space-y-5">

                        {/* Email */}
                        <div className="space-y-2">
                            <label className="text-sm font-medium text-black dark:text-white">
                                Email
                            </label>

                            <Input
                                type="email"
                                placeholder="Enter your email"
                                className="h-12 rounded-xl border-black/10 bg-black/[0.03] text-black placeholder:text-black/40 focus-visible:ring-cyan-500 dark:border-white/10 dark:bg-white/5 dark:text-white dark:placeholder:text-white/30"
                                name="email"
                                value={loginData.email}
                                onChange={handleInputChange}
                            />
                        </div>

                        {/* Password */}
                        <div className="space-y-2">
                            <label className="text-sm font-medium text-black dark:text-white">
                                Password
                            </label>

                            <Input
                                type="password"
                                placeholder="Enter your password"
                                className="h-12 rounded-xl border-black/10 bg-black/[0.03] text-black placeholder:text-black/40 focus-visible:ring-cyan-500 dark:border-white/10 dark:bg-white/5 dark:text-white dark:placeholder:text-white/30"
                                name="password"
                                value={loginData.password}
                                onChange={handleInputChange}
                            />
                        </div>

                        {/* Login Button */}
                        <Button
                            disabled={loading}
                            type="submit"
                            className="cursor-pointer h-12 w-full rounded-xl bg-gradient-to-r from-cyan-500 to-violet-500 text-base font-semibold text-white shadow-lg shadow-cyan-500/20 transition-all hover:scale-[1.02] hover:shadow-cyan-500/40"
                        >
                            {loading ? (
                                <>
                                    <Spinner />
                                    Please wait...
                                </>
                            ) : (
                                "Sign In"

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

                    {/* Social Buttons */}

                    <OAuth2Buttons />

                    {/* Footer */}
                    <p className="mt-8 text-center text-sm text-black/50 dark:text-white/50">
                        Don&apos;t have an account?{" "}
                        <span className="cursor-pointer font-medium text-cyan-600 transition hover:text-cyan-500 dark:text-cyan-300">
                            <p className="text-right">
                                <span
                                    onClick={() =>
                                        navigate("/forgot-password")
                                    }
                                    className="cursor-pointer text-cyan-500 hover:underline"
                                >
                                    Forgot Password?
                                </span>
                            </p>
                        </span>
                    </p>
                </CardContent>
            </Card>
        </div>
    );
}

export default Login;
