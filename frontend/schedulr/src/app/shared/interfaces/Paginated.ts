export interface Paginated<T> {
    content: T[];
    empty: boolean;
    first: boolean;
    last: boolean;
    number: number;
    numberOfElements: number;
    pageable: {
        pageNumber: number;
        pageSize: number;
        sort: {
            empty: boolean;
            unsorted: boolean;
            sorted: boolean;
        };
        offset: number;
        unpaged: boolean;
        paged: boolean;
    };
    size: number;
    sort: {
        empty: boolean;
        unsorted: boolean;
        sorted: boolean;
    };
    totalElements: number;
    totalPages: number;
}