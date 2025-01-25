package gr.myprojects.schedulr.repository;

import gr.myprojects.schedulr.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUuid(String uuid);

    // Returns a List of Users that attend an event based on the event's id
    List<User> findByEventsToAttendId(Integer eventId);

    // Returns a List of Users that attend an event based on the event's uuid
    List<User> findByEventsToAttendUuid(String eventUuid);

    // Returns the owner of the event with the specific uuid
    Optional<User> findByOwnedEventsUuid(String eventUuid);
}
