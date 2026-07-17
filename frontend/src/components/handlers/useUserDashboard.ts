import { useState } from "react";
import toast from "react-hot-toast";
import useAuth from "../../Auth/store";
import { getCurrentUser } from "../../services/AuthService";
import type UserT from "../../models/User";

export const useUserDashboard = () => {

  const user = useAuth((state) => state.user);
  const [user1, setUser1] = useState<UserT | null>(null);

  const getUserData = async () => {
    try {
      const user1 = await getCurrentUser(user?.email);

      setUser1(user1);
      toast.success("you are able to access secured apis")
    } catch (error) {
      console.log(error);
      toast.error("error in getting data");
    }
  };

  return {
    user,
    user1,
    getUserData,
  };
};