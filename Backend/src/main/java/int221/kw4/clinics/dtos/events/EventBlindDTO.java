package int221.kw4.clinics.dtos.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import int221.kw4.clinics.dtos.eventCategories.EventCategoryDTO;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventBlindDTO {
    private EventCategoryDTO eventCategory;
    private Instant eventStartTime;
}
