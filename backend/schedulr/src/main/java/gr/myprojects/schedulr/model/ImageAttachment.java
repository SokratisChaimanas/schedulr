package gr.myprojects.schedulr.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "image_attachements")
public class ImageAttachment extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String savedName;
    private String filePath;
    private String contentType;
    private String extension;
}
