export interface CommentCreate {
    description: string;
    userUuid: string;
    eventUuid: string;
}

export interface CommentDelete {
    commentUuid: string;
    userUuid: string;
}

export interface CommentReadOnly {
    uuid: string;
    description: string;
    authorUsername: string;
    eventTitle: string;
    eventUuid: string;
    isDeleted: boolean;
}