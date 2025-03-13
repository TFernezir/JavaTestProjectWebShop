export interface User {
    id: number;
    email: string;
    userName: string;
    role: string[];
}

export interface UserDto{
    email: string;
    password: string;
}