package int221.kw4.clinics.dtos.events;

import lombok.*;

import javax.validation.constraints.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventPostDTO {
    @NotNull(message = "Name shouldn't be null")
    @Size(max = 100, min = 1, message = "Name should be between 1 and 100 characters in length.")
    private String bookingName;

    @Email(message = "email invalid syntax", regexp = "[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
    @NotNull(message = "Email shouldn't be null")
    @Size(max = 255, min = 5, message = "Email should be between 10 and 45 characters in length.")
    private String bookingEmail;

    @NotNull(message = "startTime shouldn't be blank/null")
    @FutureOrPresent(message = "startTime shouldn't be past")
    private Instant eventStartTime;

    @Size(max = 500, message = "A note should be between 0 and 500 characters in length.")
    private String eventNotes;

    private Integer eventDuration;

    @NotNull(message = "Category shouldn't be null")
    private Integer eventCategoryId;

//    private Integer userId;

}
