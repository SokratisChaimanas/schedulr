export interface AuthenticationRequest {
    username: string;
    password: string;
}

export interface AuthenticationResponse {
    token: string;
    uuid: string;
    role: string;
}

export interface JwtPayload {
    sub?: string; // Username
    uuid?: string; // UUID
    role?: string; // Role
    exp: number
}