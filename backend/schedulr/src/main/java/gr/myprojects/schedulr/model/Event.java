package gr.myprojects.schedulr.model;

import gr.myprojects.schedulr.core.enums.Category;
import gr.myprojects.schedulr.core.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "events")
public class Event extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private LocalDateTime date;

    private String location;

    private Double price;

    private String uuid;

    @Column(name = "max_seats", nullable = false)
    private Integer maxSeats;

    @Column(name = "booked_seats", nullable = false)
    @ColumnDefault("0")
    private Integer bookedSeats;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_attachment_id")
    private ImageAttachment imageAttachment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToMany(mappedBy = "eventsToAttend")
    private Set<User> attendees = new HashSet<>();

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @PrePersist
    private void onCreate() {
        if (uuid == null) uuid = UUID.randomUUID().toString();
    }
}
