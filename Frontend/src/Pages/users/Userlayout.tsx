import { Navigate, Outlet } from "react-router";
import useAuth from "../../Auth/store";

function Userlayout() {
  const authStatus = useAuth((state) => state.authStatus);
  const accessToken = useAuth((state) => state.accessToken);

  // Zustand hydration check
  const hasHydrated = useAuth.persist.hasHydrated();

  // Wait until persisted state loads
  if (!hasHydrated) {
    return (
      <div className="flex h-screen w-screen items-center justify-center bg-slate-50 dark:bg-slate-900">
        <div className="h-8 w-8 animate-spin rounded-full border-4 border-cyan-500 border-t-transparent"></div>
      </div>
    );
  }

  // Authenticated
  if (authStatus && accessToken) {
    return (
      <div className="min-h-screen w-full bg-slate-50 dark:bg-slate-900">
        <main>
          <Outlet />
        </main>
      </div>
    );
  }

  // Not authenticated
  return <Navigate to="/login" replace />;
}

export default Userlayout;