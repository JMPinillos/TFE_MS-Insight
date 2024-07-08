/// <reference types="vite/client" />
interface ImportMetaEnv {
    readonly VITE_SUPABASE_URL: string;
    readonly VITE_SUPABASE_ANON_KEY: string;
    // ... más variables
    readonly VITE_NET_API_URL: string;

    // emails mocks para pruebas
    readonly VITE_EMAIL_ADMIN: string;
    readonly VITE_EMAIL_RESEARCHER: string;
    readonly VITE_EMAIL_DOCTOR: string;
    readonly VITE_EMAIL_USER_PASS: string;

}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}