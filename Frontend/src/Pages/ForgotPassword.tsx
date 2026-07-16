import { Mail, Smartphone } from "lucide-react";
import { Card, CardContent } from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Button } from "../components/ui/button";
import { useForgotPassword } from "../components/handlers/useForgotPassword";
import { useNavigate } from "react-router";




function ForgotPassword() {
 const navigate = useNavigate();
  const {
    identifier,
    setIdentifier,
    loading,
    error,
    handleSubmit,
  } = useForgotPassword();

  return (

  <div className="relative flex min-h-screen items-center justify-center overflow-hidden bg-white px-6 py-10 transition-colors dark:bg-black">


{/* Background Glow */}
<div className="absolute left-0 top-0 h-72 w-72 rounded-full bg-cyan-500/20 blur-3xl" />
<div className="absolute bottom-0 right-0 h-72 w-72 rounded-full bg-violet-500/20 blur-3xl" />

{/* Grid Overlay */}
<div className="absolute inset-0 bg-[linear-gradient(to_right,rgba(120,120,120,0.05)_1px,transparent_1px),linear-gradient(to_bottom,rgba(120,120,120,0.05)_1px,transparent_1px)] bg-[size:60px_60px]" />

<Card className="relative z-10 w-full max-w-md border border-black/10 bg-white/70 shadow-2xl backdrop-blur-2xl dark:border-white/10 dark:bg-white/5">

  <CardContent className="p-8">

    {/* Logo */}
    <div className="mb-8 flex justify-center">
      <div className="flex h-14 w-14 items-center justify-center rounded-2xl bg-gradient-to-br from-cyan-400 to-violet-500 text-2xl font-bold text-white shadow-lg shadow-cyan-500/30">
        🔐
      </div>
    </div>

    {/* Heading */}
    <div className="text-center">
      <h1 className="bg-gradient-to-r from-black via-cyan-700 to-violet-700 bg-clip-text text-4xl font-black text-transparent dark:from-white dark:via-cyan-200 dark:to-violet-300">
        Forgot Password
      </h1>

      <p className="mt-3 text-sm text-black/60 dark:text-white/60">
        Enter your registered email or mobile number.
        We will send a secure password reset link.
      </p>

      {error && (
        <p className="mt-4 rounded-lg bg-red-500/10 p-3 text-sm text-red-500">
          {error}
        </p>
      )}
    </div>

    {/* Form */}
    <form
      onSubmit={handleSubmit}
      className="mt-8 space-y-5"
    >
      <div>
        <label className="mb-2 block text-sm font-medium text-black dark:text-white">
          Email or Mobile Number
        </label>

        <div className="relative">

          <Input
            placeholder="Enter Email or Mobile"
            value={identifier}
            onChange={(e) =>
              setIdentifier(e.target.value)
            }
            className="h-12 rounded-xl border-black/10 bg-black/[0.03] pr-16 text-black placeholder:text-black/40 focus-visible:ring-cyan-500 dark:border-white/10 dark:bg-white/5 dark:text-white dark:placeholder:text-white/30"
          />

          <Mail className="absolute right-10 top-4 h-4 w-4 text-cyan-500" />

          <Smartphone className="absolute right-4 top-4 h-4 w-4 text-violet-500" />

        </div>
      </div>

      <Button
        type="submit"
        disabled={loading}
        className="h-12 w-full rounded-xl bg-gradient-to-r from-cyan-500 to-violet-500 text-base font-semibold text-white shadow-lg shadow-cyan-500/20 transition-all hover:scale-[1.02]"
      >
        {loading
          ? "Sending..."
          : "Send Reset Link"}
      </Button>
    </form>

    <p className="mt-6 text-center text-sm text-black/50 dark:text-white/50">
      Remember your password?
      <span
        className="ml-2 cursor-pointer font-medium text-cyan-600 hover:text-cyan-500 dark:text-cyan-300"
        onClick={() => navigate("/signup")}
      >
        Sign In
      </span>
    </p>

  </CardContent>

</Card>


  </div>
);

}

export default ForgotPassword;