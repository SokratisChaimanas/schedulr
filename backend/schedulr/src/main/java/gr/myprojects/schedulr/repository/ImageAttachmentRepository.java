package gr.myprojects.schedulr.repository;

import gr.myprojects.schedulr.model.ImageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ImageAttachmentRepository extends JpaRepository<ImageAttachment, Integer> {

}
