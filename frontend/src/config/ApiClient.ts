import axios from "axios";
import useAuth from "../Auth/store";
import { refreshToken } from "../services/AuthService";

console.log("API URL:", import.meta.env.VITE_API_BASE_URL);
const apiClient = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api/v1",
    
    withCredentials: true,
    timeout: 10000,
});



// every request
apiClient.interceptors.request.use((config) => {
    const accessToken = useAuth.getState().accessToken;
    if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
});

let isRefreshing = false;
let pending: Array<
  (token: string) => void
> = [];

function queueRequest(
  cb: (token: string) => void
) {
  pending.push(cb);
}

function resolveQueue(newToken: string) {
    pending.forEach((cb) => cb(newToken));
    pending = [];
}

apiClient.interceptors.response.use(
    (response) => response,
    async(error) => {
        const is401 = error.response.status == 401;
        const original = error.config;
        if (!is401 || original._retry) {
            //message
            return Promise.reject(error);
        }

        original._retry = true;
        //we will try to refresh the token
        if (isRefreshing) {
            return new Promise((resolve, reject) => {
                queueRequest((newToken: string) => {
                    if (!newToken) return reject();
                    original.headers.Authorization = `Bearer ${newToken}`;
                    resolve(apiClient(original));
                });
            });
        }
        // start refresh
isRefreshing = true

try {
    const loginResponseData = await refreshToken();
    const newToken = loginResponseData.accessToken;
    if(!newToken) throw new Error("no access token received");
    useAuth
    .getState()
    .changeLocalLoginData(
        loginResponseData.accessToken,
        loginResponseData.user,
        true,
    );
    //
    resolveQueue(newToken);

    original.headers.Authorization = `Bearer ${newToken}`;
    return apiClient(original);
} catch (error) {
    resolveQueue('null');
    useAuth.getState().logout();
    return Promise.reject(error);
} finally{
    isRefreshing = false;
}
        
        
    }
);

export default apiClient;