import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs';
import { SuccessResponse } from '../interfaces/Response';
import { EventCreateDTO, EventReadOnlyDTO, EventCancelDTO, EventAttendDTO, EventFilterDTO, Paginated } from '../interfaces/Event';
import { Status } from '../interfaces/Enums';

@Injectable({
    providedIn: 'root',
})
export class EventService {
    private http = inject(HttpClient);
    private API_URL_EVENTS = 'http://localhost:8080/api/events';
    
    createEvent(createDTO: EventCreateDTO, uuid: string, imageFile?: File) {
        const formData = new FormData();
        formData.append('createDTO', JSON.stringify(createDTO));
        formData.append('uuid', uuid);
        if (imageFile) {
            formData.append('imageFile', imageFile);
        }
        
        return this.http.post<SuccessResponse<EventReadOnlyDTO>>(`${this.API_URL_EVENTS}/create`, formData).pipe(
            map((response) => response)
        );
    }
    
    cancelEvent(cancelDTO: EventCancelDTO) {
        return this.http.put<SuccessResponse<EventReadOnlyDTO>>(`${this.API_URL_EVENTS}/cancel`, cancelDTO).pipe(
            map((response) => response)
        );
    }
    
    attendEvent(attendDTO: EventAttendDTO) {
        return this.http.post<SuccessResponse<EventReadOnlyDTO>>(`${this.API_URL_EVENTS}/attend`, attendDTO).pipe(
            map((response) => response)
        );
    }
    
    getPaginatedEvents(page = 0, size = 10, status: Status = 'PENDING') {
        return this.http.get<SuccessResponse<Paginated<EventReadOnlyDTO>>>(`${this.API_URL_EVENTS}?page=${page}&size=${size}&status=${status}`).pipe(
            map((response) => response)
        );
    }
    
    getPaginatedFilteredEvents(filterDTO: EventFilterDTO, page = 0, size = 10) {
        return this.http.post<SuccessResponse<Paginated<EventReadOnlyDTO>>>(`${this.API_URL_EVENTS}/filters/paginated?page=${page}&size=${size}`, filterDTO).pipe(
            map((response) => response)
        );
    }
    
    getFilteredEvents(filterDTO: EventFilterDTO) {
        return this.http.post<SuccessResponse<EventReadOnlyDTO[]>>(`${this.API_URL_EVENTS}/filters`, filterDTO).pipe(
            map((response) => response)
        );
    }
}
