export interface Paginated<T> {
    content: T[];               // Array of items (list of EventReadOnlyDTO)
    empty: boolean;             // Whether the page is empty
    first: boolean;             // Whether this is the first page
    last: boolean;              // Whether this is the last page
    number: number;             // Current page number (0-based)
    numberOfElements: number;   // Number of elements on the current page
    pageable: {
        pageNumber: number;     // Current page number
        pageSize: number;       // Size of the page
        sort: {
            empty: boolean;
            unsorted: boolean;
            sorted: boolean;
        };
        offset: number;         // Offset of the current page
        unpaged: boolean;
        paged: boolean;
    };
    size: number;               // Size of the page
    sort: {
        empty: boolean;
        unsorted: boolean;
        sorted: boolean;
    };
    totalElements: number;      // Total number of elements
    totalPages: number;         // Total number of pages
}
