package int221.kw4.clinics.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
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
    @NotBlank(message = "Name shouldn't be blank")
    @NotEmpty(message = "Name shouldn't be empty")
    @NotNull(message = "Name shouldn't be null")
    @Size(max = 100, min = 1, message = "Name should be between 1 and 100 characters in length.")
    private String bookingName;

    @Column(name = "bookingEmail", nullable = false)
    @Email(message = "email invalid syntax", regexp = "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
    @NotBlank(message = "Email shouldn't be blank")
    @NotEmpty(message = "Email shouldn't be empty")
    @NotNull(message = "Email shouldn't be null")
    @Size(max = 45, min = 10, message = "Email should be between 10 and 45 characters in length.")
    private String bookingEmail;

    @Column(name = "eventStartTime", nullable = false)
    @NotNull(message = "startTime shouldn't be blank/null")
    @FutureOrPresent(message = "startTime shouldn't be past")
    private Instant eventStartTime;

    @Column(name = "eventNotes", length = 500)
    @Size(max = 500, message = "A note should be between 0 and 500 characters in length.")
    private String eventNotes;

    @Column(name = "eventDuration", nullable = false)
    private Integer eventDuration;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "eventCategoryId")
    @NotNull(message = "Category shouldn't be null")
    private EventCategory eventCategory;
}