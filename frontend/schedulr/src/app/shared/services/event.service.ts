import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs';
import { SuccessResponse } from '../interfaces/Response';
import {
    EventAttend,
    EventCancel,
    EventCreate,
    EventFilter,
    EventReadOnly,
} from '../interfaces/Event';
import { Paginated } from '../interfaces/Paginated';

@Injectable({
    providedIn: 'root',
})
export class EventService {
    private http = inject(HttpClient);
    private API_URL_EVENTS = 'http://localhost:8080/api/events';
    private API_URL_ADMIN = 'http://localhost:8080/api/admin'
    
    createEvent(createEvent: EventCreate, uuid: string, imageFile?: File) {
        const formData = new FormData();
        formData.append('createDTO', JSON.stringify(createEvent));
        formData.append('uuid', uuid);
        if (imageFile) {
            formData.append('imageFile', imageFile);
        }
        
        return this.http
            .post<SuccessResponse<EventReadOnly>>(`${this.API_URL_EVENTS}/create`, formData)
            .pipe(map((response) => response));
    }
    
    cancelEvent(cancelDTO: EventCancel) {
        return this.http
            .put<SuccessResponse<EventReadOnly>>(`${this.API_URL_EVENTS}/cancel`, cancelDTO)
            .pipe(map((response) => response));
    }
    
    attendEvent(attendDTO: EventAttend) {
        return this.http
            .post<SuccessResponse<EventReadOnly>>(`${this.API_URL_EVENTS}/attend`, attendDTO)
            .pipe(map((response) => response));
    }
    
    getPaginatedEvents(page = 0, size = 5, uuid: string) {
        return this.http
            .get<SuccessResponse<Paginated<EventReadOnly>>>(
                `${this.API_URL_EVENTS}?page=${page}&size=${size}&userUuid=${uuid}`
            )
            .pipe(
                map((response) => ({
                    ...response.data,
                    items: this.sortEventsByClosestDate(response.data.content),
                }))
            );
    }
    
    getEventsByStatus(userUuid: string, status: string) {
        return this.http
            .get<SuccessResponse<EventReadOnly[]>>(
                `${this.API_URL_ADMIN}/events/status?userUuid=${userUuid}&status=${status}`
            )
            .pipe(map((response) => this.sortEventsByClosestDate(response.data)));
    }
    
    getPaginatedFilteredEvents(filterDTO: EventFilter, page = 0, size = 10) {
        return this.http
            .post<SuccessResponse<Paginated<EventReadOnly>>>(
                `${this.API_URL_EVENTS}/filters/paginated?page=${page}&size=${size}`,
                filterDTO
            )
            .pipe(
                map((response) => ({
                    ...response.data,
                    items: this.sortEventsByClosestDate(response.data.content),
                }))
            );
    }
    
    getFilteredEvents(filterDTO: EventFilter) {
        return this.http
            .post<SuccessResponse<EventReadOnly[]>>(`${this.API_URL_EVENTS}/filters`, filterDTO)
            .pipe(map((response) => this.sortEventsByClosestDate(response.data)));
    }
    
    getEventByUuid(eventUuid: string) {
        return this.http
            .get<SuccessResponse<EventReadOnly>>(`${this.API_URL_EVENTS}/${eventUuid}`)
            .pipe(map((response) => response.data));
    }
    
    getOwnedEvents(userUuid: string) {
        return this.http
            .get<SuccessResponse<EventReadOnly[]>>(`${this.API_URL_EVENTS}/user/${userUuid}/owned`)
            .pipe(map((response) => this.sortEventsByClosestDate(response.data)));
    }
    
    getPendingAttendedEvents(userUuid: string) {
        return this.http
            .get<SuccessResponse<EventReadOnly[]>>(
                `${this.API_URL_EVENTS}/user/${userUuid}/attended-pending`
            )
            .pipe(map((response) => this.sortEventsByClosestDate(response.data)));
    }
    
    getCompletedAttendedEvents(userUuid: string) {
        return this.http
            .get<SuccessResponse<EventReadOnly[]>>(
                `${this.API_URL_EVENTS}/user/${userUuid}/attended-completed`
            )
            .pipe(map((response) => this.sortEventsByClosestDate(response.data)));
    }
    
    getCanceledAttendedEvents(userUuid: string) {
        return this.http
            .get<SuccessResponse<EventReadOnly[]>>(
                `${this.API_URL_EVENTS}/user/${userUuid}/attended-canceled`
            )
            .pipe(map((response) => this.sortEventsByClosestDate(response.data)));
    }
    
    private sortEventsByClosestDate(events: EventReadOnly[]): EventReadOnly[] {
        return events.sort(
            (a, b) =>
                Math.abs(new Date(a.date).getTime() - Date.now()) -
                Math.abs(new Date(b.date).getTime() - Date.now())
        );
    }
}
