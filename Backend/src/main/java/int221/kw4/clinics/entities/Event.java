package int221.kw4.clinics.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventId", nullable = false)
    private Integer id;

    @Column(name = "bookingName", nullable = false,length = 100)
    private String bookingName;

    @Column(name = "bookingEmail",  nullable = false)
    private String bookingEmail;

    @Column(name = "eventStartTime", nullable = false)
    private Instant eventStartTime;

    @Column(name = "eventNotes", length = 500)
    private String eventNotes;

    @Column(name = "eventDuration", nullable = false)
    private Integer eventDuration;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "eventCategoryId", nullable = false)
    private EventCategory eventCategory;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = true)
    private User user;

}

