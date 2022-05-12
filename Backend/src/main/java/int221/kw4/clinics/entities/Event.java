package int221.kw4.clinics.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventId", nullable = false)
    private Integer id;

    @Column(name = "bookingName", nullable = false, length = 100)
    @NotBlank(message = "name shouldn't be null")
    private String bookingName;

    @Column(name = "bookingEmail", nullable = false)
    @Email(message = "email invalid syntax")
    @NotNull(message = "email shouldn't be null")
    private String bookingEmail;

    @Column(name = "eventStartTime", nullable = false)
    @NotNull(message = "startTime shouldn't be null")
    @FutureOrPresent(message = "startTime Invalid")
    private Instant eventStartTime;

    @Column(name = "eventNotes", length = 500)
    private String eventNotes;

    @Column(name = "eventDuration", nullable = false)
    private Integer eventDuration;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "eventCategoryId")
    @NotNull(message = "category shouldn't be null")
    private EventCategory eventCategory;
}