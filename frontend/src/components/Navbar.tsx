import { NavLink, useNavigate } from "react-router";
import { Button } from "../components/ui/button";
import { Moon, Sun } from "lucide-react";
import useAuth from "../Auth/store";
import { useEffect, useState } from "react";

function Navbar() {
  // Subscribe to raw properties reactively so React re-renders instantly on state change
  const authStatus = useAuth((state) => state.authStatus);
  const user = useAuth((state) => state.user);
  const logout = useAuth((state) => state.logout);
  const navigate = useNavigate();

  // Initialize directly from localStorage
  const [darkMode, setDarkMode] = useState(() => {
    if (typeof window !== "undefined") {
      return (
        localStorage.getItem("theme") === "dark" ||
        window.matchMedia("(prefers-color-scheme: dark)").matches
      );
    }
    return false;
  });

  // Await store changes before executing redirect
  const handleLogout = async () => {
    try {
      await logout();
    } catch (error) {
      console.error("Logout process encountered an error:", error);
    } finally {
      navigate("/login", { replace: true });
    }
  };

  // Sync theme with DOM
  useEffect(() => {
    if (darkMode) {
      document.documentElement.classList.add("dark");
      localStorage.setItem("theme", "dark");
    } else {
      document.documentElement.classList.remove("dark");
      localStorage.setItem("theme", "light");
    }
  }, [darkMode]);

  return (
    <header className="sticky top-0 z-50 border-b border-white/10 bg-white/40 backdrop-blur-2xl">
      <div className="mx-auto flex max-w-7xl items-center justify-between px-6 py-4">
        
        <div className="flex items-center gap-3">
          <NavLink to={"/"}>
            <div className="flex h-10 w-10 items-center justify-center rounded-2xl bg-gradient-to-br from-cyan-400 to-violet-500 text-lg font-bold text-white shadow-lg shadow-cyan-500/20">
              A
            </div>
          </NavLink>
          <span className="text-xl font-semibold tracking-wide"> Auth App </span>
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={() => setDarkMode(!darkMode)}
            className="flex h-10 w-10 items-center justify-center rounded-xl border"
          >
            {darkMode ? <Sun className="h-5 w-5" /> : <Moon className="h-5 w-5" />}
          </button>

          {/* Conditional layout updates instantly based on reactive authStatus */}
          {authStatus ? (
            <>
              <NavLink to={"/dashboard/profile"} className="text-sm font-medium">
                {user?.name || "User"}
              </NavLink>
              <Button
                onClick={handleLogout} // ✅ Triggers the asynchronous handler
                variant={"outline"}
                className="rounded-xl bg-gradient-to-r from-cyan-500 to-violet-500 px-5 py-2 text-sm font-semibold text-white shadow-lg shadow-cyan-500/20 transition hover:scale-105"
              >
                Logout
              </Button>
            </>
          ) : (
            <>
              <NavLink to={"/"}>
                <Button
                  variant={"outline"}
                  className="rounded-xl border border-black/10 px-4 py-2 text-sm font-medium transition hover:bg-black/5 dark:border-white/10 dark:hover:bg-white/10"
                >
                  Home
                </Button>
              </NavLink>
              <NavLink to={"/login"}>
                <Button
                  variant={"outline"}
                  className="rounded-xl border border-black/10 px-4 py-2 text-sm font-medium transition hover:bg-black/5 dark:border-white/10 dark:hover:bg-white/10"
                >
                  Login
                </Button>
              </NavLink>
              <NavLink to={"/signup"}>
                <Button
                  variant={"outline"}
                  className="rounded-xl bg-gradient-to-r from-cyan-500 to-violet-500 px-5 py-2 text-sm font-semibold text-white shadow-lg shadow-cyan-500/20 transition hover:scale-105"
                >
                  SignUp
                </Button>
              </NavLink>
            </>
          )}
        </div>

      </div>
    </header>
  );
}

export default Navbar;
