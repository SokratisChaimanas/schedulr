import {UserReadOnly} from "./User";
import {CommentReadOnly} from "./Comment";

export interface EventReadOnly {
    status: string;
    uuid: string;
    title: string;
    description: string;
    date: string;
    imageAttachmentReadOnlyDTO: ImageAttachmentReadOnly;
    ownerReadOnlyDTO: UserReadOnly;
    price: number;
    maxSeats: number;
    bookedSeats: number;
    category: string;
    commentsList: CommentReadOnly[];
}

export interface EventCreate {
    title: string;
    description: string;
    date: string;
    location: string;
    price: number;
    maxSeats: number;
    category: string;
}

export interface EventAttend {
    eventUuid: string;
    userUuid: string;
}

export interface EventFilter {
    startDate?: string;
    endDate?: string;
    location?: string;
    minPrice?: number;
    maxPrice?: number;
    category?: string;
    title?: string;
}

export interface EventCancel {
    eventUuid: string;
    userUuid: string;
}

export interface ImageAttachmentReadOnly {
    filepath: string;
    savedName: string;
    extension: string;
}