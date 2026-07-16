import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "../../components/ui/card";

import { Button } from "../../components/ui/button";
import { Label } from "../../components/ui/label";
import { Input } from "../../components/ui/input";

import {
  Avatar,
  AvatarFallback,
  AvatarImage,
} from "../../components/ui/avatar";

import { motion } from "framer-motion";
import { useUserProfile } from "../../components/handlers/useUserProfile";

function Userprofile() {
  const {
    user,
    navigate,

    isEditing,
    setIsEditing,

    profileImage,

    editData,
    setEditData,

    handleImageChange,
    handleSaveProfile,
    handleDeleteAccount,
  } = useUserProfile();

  return (
  <div className="min-h-screen bg-background text-foreground p-6">

    {/* Background Glow */}
    <div className="fixed top-0 left-0 h-96 w-96 rounded-full bg-cyan-500/10 blur-3xl pointer-events-none" />
    <div className="fixed bottom-0 right-0 h-96 w-96 rounded-full bg-violet-500/10 blur-3xl pointer-events-none" />

    <div className="max-w-5xl mx-auto space-y-8">

      {/* Heading */}
      <motion.h1
        initial={{ opacity: 0, y: -15 }}
        animate={{ opacity: 1, y: 0 }}
        className="
          text-center
          text-5xl
          font-black
          bg-gradient-to-r
          from-cyan-500
          via-blue-500
          to-violet-500
          bg-clip-text
          text-transparent
        "
      >
        User Profile
      </motion.h1>

      {/* Profile Card */}
      <Card className="bg-card text-card-foreground rounded-3xl shadow-xl border">

        <CardHeader>
          <CardTitle className="text-2xl">
            Profile Information
          </CardTitle>
        </CardHeader>

        <CardContent className="space-y-8">

          {/* Avatar */}
          <div className="flex flex-col items-center gap-4">

            <Avatar className="w-36 h-36 border-4 border-cyan-500">
              <AvatarImage src={profileImage} />

              <AvatarFallback className="bg-cyan-500 text-white text-3xl">
                {user?.name?.charAt(0).toUpperCase()}
              </AvatarFallback>
            </Avatar>

            {isEditing && (
              <Input
                type="file"
                accept="image/*"
                onChange={handleImageChange}
                className="max-w-sm"
              />
            )}

          </div>

          {/* User Details */}
          <div className="grid md:grid-cols-2 gap-6">

            <div>
              <Label className="text-foreground">
                Full Name
              </Label>

              <Input
                className="mt-2"
                value={
                  isEditing
                    ? editData.name
                    : user?.name || ""
                }
                readOnly={!isEditing}
                onChange={(e) =>
                  setEditData({
                    ...editData,
                    name: e.target.value,
                  })
                }
              />
            </div>

            <div>
              <Label>Email</Label>

              <Input
                className="mt-2"
                value={user?.email || ""}
                readOnly
              />
            </div>

            <div>
              <Label>Provider</Label>

              <Input
                className="mt-2"
                value={user?.provider || ""}
                readOnly
              />
            </div>

            <div>
              <Label>Status</Label>

              <Input
                className="mt-2"
                value={
                  user?.enabled
                    ? "Active"
                    : "Disabled"
                }
                readOnly
              />
            </div>

          </div>

          {/* Buttons */}
          {!isEditing ? (
            <Button
              className="
                w-full
                bg-gradient-to-r
                from-cyan-500
                to-violet-500
                text-white
              "
              onClick={() => setIsEditing(true)}
            >
              Edit Profile
            </Button>
          ) : (
            <div className="flex gap-4">

              <Button
                variant="outline"
                className="flex-1"
                onClick={() => setIsEditing(false)}
              >
                Cancel
              </Button>

              <Button
                className="
                  flex-1
                  bg-gradient-to-r
                  from-green-500
                  to-emerald-500
                  text-white
                "
                onClick={handleSaveProfile}
              >
                Save Changes
              </Button>

            </div>
          )}

        </CardContent>
      </Card>

      {/* Account Settings */}
      <Card className="bg-card text-card-foreground rounded-3xl shadow-xl border">

        <CardHeader>
          <CardTitle className="text-2xl">
            Account Settings
          </CardTitle>
        </CardHeader>

        <CardContent className="space-y-4">

          <Button
            variant="outline"
            className="w-full"
            onClick={() =>
              navigate("/dashboard/change-password")
            }
          >
            Change Password
          </Button>

          <Button
            variant="destructive"
            className="w-full"
            onClick={handleDeleteAccount}
          >
            Delete Account
          </Button>

        </CardContent>
      </Card>

    </div>
  </div>
);
}

export default Userprofile;