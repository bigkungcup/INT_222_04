package int221.kw4.clinics.dtos.events;

import int221.kw4.clinics.dtos.eventCategories.EventCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Integer id;
    private String bookingName;
    private String bookingEmail;
    private EventCategoryDTO eventCategory;
    private Instant eventStartTime;
    private String eventNotes;
    private Integer eventDuration;
}
