
import { useResetPassword } from "../components/handlers/useResetPassword";
import { Button } from "../components/ui/button";
import { Card, CardContent } from "../components/ui/card";
import { Input } from "../components/ui/input";

function ResetPassword() {
  const {
    formData,
    loading,
    timeLeft,
    handleInputChange,
    handleSubmit,
    handleResendOtp,

  } = useResetPassword();

  return (
    <div className="relative flex min-h-screen items-center justify-center overflow-hidden bg-white px-6 py-10 dark:bg-black">

      {/* Background Glow */}
      <div className="absolute left-0 top-0 h-72 w-72 rounded-full bg-cyan-500/20 blur-3xl" />
      <div className="absolute bottom-0 right-0 h-72 w-72 rounded-full bg-violet-500/20 blur-3xl" />

      {/* Grid Overlay */}
      <div className="absolute inset-0 bg-[linear-gradient(to_right,rgba(120,120,120,0.05)_1px,transparent_1px),linear-gradient(to_bottom,rgba(120,120,120,0.05)_1px,transparent_1px)] bg-[size:60px_60px]" />

      <Card className="relative z-10 w-full max-w-md border border-black/10 bg-white/70 shadow-2xl backdrop-blur-2xl dark:border-white/10 dark:bg-white/5">
        <CardContent className="p-8">

          <div className="mb-8 flex justify-center">
            <div className="flex h-14 w-14 items-center justify-center rounded-2xl bg-gradient-to-br from-cyan-400 to-violet-500 text-2xl font-bold text-white shadow-lg shadow-cyan-500/30">
              🔒
            </div>
          </div>

          <div className="text-center">
            <h1 className="bg-gradient-to-r from-black via-cyan-700 to-violet-700 bg-clip-text text-4xl font-black text-transparent dark:from-white dark:via-cyan-200 dark:to-violet-300">
              Reset Password
            </h1>

            <p className="mt-3 text-sm text-black/60 dark:text-white/60">
              Create a strong new password to secure your account.
            </p>
          </div>

          <form onSubmit={handleSubmit} className="mt-8 space-y-5">


            <Input
              type="password"
              name="password"
              placeholder="Enter new password"
              value={formData.password}
              onChange={handleInputChange}
              className="h-12 rounded-xl border-black/10 bg-black/[0.03]
              dark:border-white/10 dark:bg-white/5"
            />

            <Input
              type="password"
              name="confirmPassword"
              placeholder="Confirm new password"
              value={formData.confirmPassword}
              onChange={handleInputChange}
              className="h-12 rounded-xl border-black/10 bg-black/[0.03]
              dark:border-white/10 dark:bg-white/5"
            />
            <Input
              type="text"
              name="otp"
              maxLength={6}
              placeholder="OTP"
              value={formData.otp}
              onChange={handleInputChange}
              className="w-32 mx-auto text-center text-lg tracking-[0.5em]
                        h-12 rounded-xl
                        border-black/10 bg-black/[0.03]
                        dark:border-white/10 dark:bg-white/5"
            />

            <div className="flex justify-between text-sm text-gray-500 dark:text-gray-400">

              <span>
                OTP expires in:
                {" "}
                {Math.floor(timeLeft / 60)}
                :
                {String(timeLeft % 60).padStart(2, "0")}
              </span>

              <Button
                type="button"
                disabled={timeLeft > 0}
                onClick={handleResendOtp}
                className="
      text-cyan-500
      hover:text-cyan-400
      disabled:opacity-50
      disabled:cursor-not-allowed
    "
              >
                Resend OTP
              </Button>

            </div>

            <Button type="submit" disabled={loading}>
              {loading ? "Updating..." : "Reset Password"}
            </Button>
          </form>

        </CardContent>
      </Card>

    </div>
  );
}

export default ResetPassword;

