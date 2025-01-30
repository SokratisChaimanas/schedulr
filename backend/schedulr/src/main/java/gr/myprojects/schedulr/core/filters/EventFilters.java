package gr.myprojects.schedulr.core.filters;

import gr.myprojects.schedulr.core.enums.Category;
import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFilters extends GenericFilters {

    @Nullable
    private LocalDateTime dateFrom;

    @Nullable
    private LocalDateTime dateTo;

    @Nullable
    private String location;

    @Nullable
    private Double minPrice;

    @Nullable
    private Double maxPrice;

    @Nullable
    private Category category;

    @Nullable
    private String title;

    @Nullable
    private Pageable pageable;
}
