import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.tsx";
import { BrowserRouter, Routes, Route } from "react-router";
import RootLayout from "./Pages/RootLayout.tsx";
import Login from "./Pages/Login.tsx";
import Signup from "./Pages/Signup.tsx";
import Services from "./Pages/Services.tsx";
import About from "./Pages/About.tsx";
import Userlayout from "./Pages/users/Userlayout.tsx";
import Userprofile from "./Pages/users/Userprofile.tsx";
import Userdashboard from "./Pages/users/Userdashboard.tsx";
import OAuthSuccess from "./Pages/OAuthSuccess.tsx";
import OAuthFailure from "./Pages/OAuthFailure.tsx";
import ChangePassword from "./Pages/users/ChangePassword.tsx";
import ResetPassword from "./Pages/ResetPassword.tsx";
import ForgotPassword from "./Pages/ForgotPassword.tsx";


createRoot(document.getElementById("root")!).render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<RootLayout />}>
        <Route index element={<App />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/services" element={<Services />} />
        <Route path="/about" element={<About />} />
        <Route path="/dashboard" element={<Userlayout />}>
          <Route index element={<Userdashboard />} />
          <Route path="profile" element={<Userprofile />} />
          <Route path="change-password" element={<ChangePassword />} />
          {/* .... */}
        </Route>
        <Route path="oauth/success" element={<OAuthSuccess />} />
        <Route path="/reset-password" element={<ResetPassword />}/>
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="oauth/failure" element={<OAuthFailure />} />
      </Route>
    </Routes>
  </BrowserRouter>
);