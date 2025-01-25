package gr.myprojects.schedulr.repository;

import gr.myprojects.schedulr.model.Comment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {
    Optional<Comment> findByUuid(String uuid);
    List<Comment> findByEventId(Integer eventId);
//    List<Comment> findByUserId(Integer userId);
    List<Comment> findByEventUuid(String eventUuid);
//    List<Comment> findByUserUuid(String userUuid);
}
