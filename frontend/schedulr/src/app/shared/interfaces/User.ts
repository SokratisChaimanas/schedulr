export interface UserReadOnly {
    uuid: string;
    username: string;
}

export interface UserRegister {
    username: string;
    email: string;
    password: string;
    confirmPassword: string,
    firstname: string;
    lastname: string;
    role: string;
}

export interface LoggedInUserData {
    username: string,
}
