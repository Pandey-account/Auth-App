import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import toast from "react-hot-toast";

import useAuth from "../../Auth/store";
import {
  deleteProfile,
  imageUrl,
  updateProfile,
} from "../../services/AuthService";

export const useUserProfile = () => {
  const user = useAuth((state) => state.user);

  const [isEditing, setIsEditing] = useState(false);

  const [profileImage, setProfileImage] = useState("");

  const navigate = useNavigate();

  const [editData, setEditData] = useState({
    name: user?.name || "",
  });

  const [selectedFile, setSelectedFile] =
    useState<File | null>(null);

  useEffect(() => {
    let url: string;

    const loadImage = async () => {
      try {
        const blob = await imageUrl();

        url = URL.createObjectURL(blob);

        setProfileImage(url);
      } catch (error) {
        console.error(error);
      }
    };

    loadImage();

    return () => {
      if (url) {
        URL.revokeObjectURL(url);
      }
    };
  }, []);

  const handleImageChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const file = e.target.files?.[0];

    if (!file) return;

    setSelectedFile(file);

    const previewUrl = URL.createObjectURL(file);
    setProfileImage(previewUrl);
  };

  const handleSaveProfile = async () => {
    try {
      const formData = new FormData();

      formData.append("name", editData.name);

      if (selectedFile) {
        formData.append("imageFile", selectedFile);
      }

      const updatedUser = await updateProfile(formData);

      // Zustand user update
      useAuth.getState().setUser(updatedUser);

      // Image reload
      const blob = await imageUrl();
      const imageUrlObject = URL.createObjectURL(blob);
      setProfileImage(imageUrlObject);

      setIsEditing(false);
      setSelectedFile(null);

      alert("Profile Updated Successfully");
    } catch (error) {
      console.error(error);
      alert("Profile Update Failed");
    }
  };

  const handleDeleteAccount = async () => {
  const confirmed = window.confirm(
    "Are you sure you want to delete your account?"
  );

  if (!confirmed) return;

  try {
    await deleteProfile();

    await useAuth.getState().logout(true);

    toast.success(
      "Account deleted successfully. Confirmation email sent."
    );

    navigate("/login", { replace: true });

    alert("Account deleted successfully");
  } catch (error) {
    console.error(error);
    alert("Failed to delete account");
  }
};

  return {
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
  };
};