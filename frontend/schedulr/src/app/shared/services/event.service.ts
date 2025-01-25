import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs';
import { SuccessResponse } from '../interfaces/Response';
import {
    EventAttend,
    EventCancel,
    EventCreate, EventFilter, EventReadOnly
} from '../interfaces/Event';
import {Paginated} from "../interfaces/Paginated";

@Injectable({
    providedIn: 'root',
})
export class EventService {
    private http = inject(HttpClient);
    private API_URL_EVENTS = 'http://localhost:8080/api/events';
    
    createEvent(createEvent: EventCreate, uuid: string, imageFile?: File) {
        const formData = new FormData();
        formData.append('createDTO', JSON.stringify(createEvent));
        formData.append('uuid', uuid);
        if (imageFile) {
            formData.append('imageFile', imageFile);
        }
        
        // console.log(formData)
        return this.http.post<SuccessResponse<EventReadOnly>>(`${this.API_URL_EVENTS}/create`, formData).pipe(
            map((response) => response)
        );
    }
    
    cancelEvent(cancelDTO: EventCancel) {
        return this.http.put<SuccessResponse<EventReadOnly>>(`${this.API_URL_EVENTS}/cancel`, cancelDTO).pipe(
            map((response) => response)
        );
    }
    
    attendEvent(attendDTO: EventAttend) {
        return this.http.post<SuccessResponse<EventReadOnly>>(`${this.API_URL_EVENTS}/attend`, attendDTO).pipe(
            map((response) => response)
        );
    }
    
    getPaginatedEvents(page = 0, size = 10, status: String = 'PENDING') {
        return this.http.get<SuccessResponse<Paginated<EventReadOnly>>>(`${this.API_URL_EVENTS}?page=${page}&size=${size}&status=${status}`).pipe(
            map((response) => response)
        );
    }
    
    getPaginatedFilteredEvents(filterDTO: EventFilter, page = 0, size = 10) {
        return this.http.post<SuccessResponse<Paginated<EventReadOnly>>>(`${this.API_URL_EVENTS}/filters/paginated?page=${page}&size=${size}`, filterDTO).pipe(
            map((response) => response)
        );
    }
    
    getFilteredEvents(filterDTO: EventFilter) {
        return this.http.post<SuccessResponse<EventReadOnly[]>>(`${this.API_URL_EVENTS}/filters`, filterDTO).pipe(
            map((response) => response)
        );
    }
}
