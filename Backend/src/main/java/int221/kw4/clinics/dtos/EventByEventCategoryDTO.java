package int221.kw4.clinics.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EventByEventCategoryDTO {
    private Integer id;
    private String bookingName;
    private String bookingEmail;
    private Instant eventStartTime;
    private String eventNotes;
    private Integer eventDuration;
}
