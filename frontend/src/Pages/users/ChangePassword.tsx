import { useChangePassword } from "../../components/handlers/useChangePassword";
import { Button } from "../../components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "../../components/ui/card";
import { Input } from "../../components/ui/input";
import { Label } from "../../components/ui/label";


function ChangePassword() {

  const {
    formData,
    loading,
    navigate,

    handleInputChange,
    handleFormSubmit,

  } = useChangePassword();

  return (
  <div className="relative min-h-screen flex items-center justify-center overflow-hidden bg-background p-6">

    {/* Background Glow */}
    <div className="absolute top-0 left-0 h-96 w-96 rounded-full bg-cyan-500/10 blur-3xl" />
    <div className="absolute bottom-0 right-0 h-96 w-96 rounded-full bg-violet-500/10 blur-3xl" />

    <Card
      className="
        relative
        w-full
        max-w-md
        rounded-3xl
        border
        bg-card/80
        backdrop-blur-xl
        shadow-2xl
      "
    >
      <CardHeader>
        <CardTitle
          className="
            text-center
            text-3xl
            font-black
            bg-gradient-to-r
            from-cyan-500
            to-violet-500
            bg-clip-text
            text-transparent
          "
        >
          Change Password
        </CardTitle>
      </CardHeader>

      <CardContent>
        <form
          onSubmit={handleFormSubmit}
          className="space-y-5"
        >
          {/* Current Password */}
          <div className="space-y-2">
            <Label className="text-foreground">
              Current Password
            </Label>

            <Input
              type="password"
              name="oldPassword"
              value={formData.oldPassword}
              onChange={handleInputChange}
              required
              className="
                h-12
                rounded-xl
                bg-background
                text-foreground
                border-border
              "
            />
          </div>

          {/* New Password */}
          <div className="space-y-2">
            <Label className="text-foreground">
              New Password
            </Label>

            <Input
              type="password"
              name="newPassword"
              value={formData.newPassword}
              onChange={handleInputChange}
              required
              className="
                h-12
                rounded-xl
                bg-background
                text-foreground
                border-border
              "
            />
          </div>

          {/* Confirm Password */}
          <div className="space-y-2">
            <Label className="text-foreground">
              Confirm Password
            </Label>

            <Input
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleInputChange}
              required
              className="
                h-12
                rounded-xl
                bg-background
                text-foreground
                border-border
              "
            />
          </div>

          {/* Submit Button */}
          <Button
            type="submit"
            disabled={loading}
            className="
              h-12
              w-full
              rounded-xl
              bg-gradient-to-r
              from-cyan-500
              to-violet-500
              text-white
              font-semibold
              hover:scale-[1.02]
              transition-all
            "
          >
            {loading
              ? "Updating..."
              : "Update Password"}
          </Button>

          {/* Back Button */}
          <Button
            type="button"
            variant="outline"
            className="
              w-full
              rounded-xl
            "
            onClick={() =>
              navigate(
                "/dashboard/profile"
              )
            }
          >
            ← Back to Profile
          </Button>
        </form>
      </CardContent>
    </Card>
  </div>
);
}

export default ChangePassword