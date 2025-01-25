package gr.myprojects.schedulr.specifications;

import gr.myprojects.schedulr.core.enums.Category;
import gr.myprojects.schedulr.model.Event;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class EventSpecification {

    private EventSpecification() {}

    public static Specification<Event> eventDateBetween(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return (root, query, criteriaBuilder) -> {
            if (dateFrom == null && dateTo == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            if (dateFrom != null && dateTo != null) {
                return criteriaBuilder.between(root.get("date"), dateFrom, dateTo);
            }
            if (dateFrom != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("date"), dateFrom);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("date"), dateTo);
        };
    }

    public static Specification<Event> eventTitleLike(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.upper(root.get("title")), "%" + title.toUpperCase() + "%");
        };
    }

    public static Specification<Event> eventLocationLike(String location) {
        return (root, query, criteriaBuilder) -> {
            if (location == null || location.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.upper(root.get("location")), "%" + location.toUpperCase() + "%");
        };
    }

    public static Specification<Event> eventPriceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    public static Specification<Event> eventCategoryIs(Category category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // Always true if category is null.
            }
            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

}
