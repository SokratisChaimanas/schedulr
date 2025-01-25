// paginated.interface.ts

export interface Paginated<T> {
    data: T[];
    totalElements: number;
    totalPages: number;
    numberOfElements: number;
    currentPage: number;
    pageSize: number;
}