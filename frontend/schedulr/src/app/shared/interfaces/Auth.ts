export interface AuthenticationRequest {
    username: string;
    password: string;
}

export interface AuthenticationResponse {
    token: string;
    uuid: string;
    role: string;
}