import { Button } from "./ui/button";
import {  Github } from "lucide-react";
import { NavLink } from "react-router";

function OAuth2Buttons() {
    return (

        <div className="space-y-4">

            {/* Google */}
            <NavLink to = {`${import.meta.env.VITE_BASE_URL || "http://localhost:8080"}/oauth2/authorization/google`} className={"block"}>

                <Button
                    variant="outline"
                    className="h-12 w-full rounded-xl border-black/10 bg-black/[0.03] text-black transition-all hover:bg-cyan-500/10 hover:text-cyan-700 dark:border-white/10 dark:bg-white/5 dark:text-white dark:hover:bg-cyan-500/10 dark:hover:text-cyan-300"
                >
                    <img
                        src="https://www.svgrepo.com/show/475656/google-color.svg"
                        alt="Google"
                        className="mr-3 h-5 w-5"
                    />

                    Continue with Google
                </Button>

            </NavLink>

            {/* Github */}
            <NavLink to = {`${import.meta.env.VITE_BASE_URL || "http://localhost:8080"}/oauth2/authorization/github`} className={"block"}>
                <Button
                variant="outline"
                className="h-12 w-full rounded-xl border-black/10 bg-black/[0.03] text-black transition-all hover:bg-violet-500/10 hover:text-violet-700 dark:border-white/10 dark:bg-white/5 dark:text-white dark:hover:bg-violet-500/10 dark:hover:text-violet-300"
            >
                <Github className="mr-3 h-5 w-5" />

                Continue with GitHub
            </Button>
            </NavLink>
        </div>
    )
}
export default OAuth2Buttons;