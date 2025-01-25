package gr.myprojects.schedulr.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "Read-only DTO representing an image attachment")
public class ImageAttachmentReadOnlyDTO {
    @Schema(description = "File path of the image attachment", example = "/uploads/images/event1.jpg")
    private String filepath;

    @Schema(description = "Saved name of the image file", example = "event1.jpg")
    private String savedName;

    @Schema(description = "File extension of the image", example = ".jpg")
    private String extension;
}
