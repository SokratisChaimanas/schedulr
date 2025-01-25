export interface FailureResponse {
    status: number;
    code: string;
    message: string;
    extraInformation?: any; // optional any type for extraInformation
}

export interface SuccessResponse<T> {
    status: number;
    data: T;
}