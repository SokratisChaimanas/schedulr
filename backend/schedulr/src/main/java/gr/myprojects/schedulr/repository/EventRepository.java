package gr.myprojects.schedulr.repository;

import gr.myprojects.schedulr.core.enums.Status;
import gr.myprojects.schedulr.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {
    Optional<Event> findByUuid(String uuid);

    List<Event> findByOwnerId(Integer id);
    List<Event> findByOwnerUuid(String uuid);

    List<Event> findByAttendeesUuid(String uuid);

    Page<Event> findByStatus(Status status, Pageable pageable);

}
