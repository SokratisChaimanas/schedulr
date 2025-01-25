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
    private LocalDateTime dateFrom;  // Filter events starting from this date.

    @Nullable
    private LocalDateTime dateTo;    // Filter events until this date.

    @Nullable
    private String location;         // Filter events by location.

    @Nullable
    private Double minPrice;         // Minimum price filter.

    @Nullable
    private Double maxPrice;         // Maximum price filter.

    @Nullable
    private Category category;       // Category filter (e.g., "Workshop", "Conference").

    @Nullable
    private String title;            // Partial match for the title of the event.

    @Nullable
    private Pageable pageable;       // Pagination information for paginated queries.
}
