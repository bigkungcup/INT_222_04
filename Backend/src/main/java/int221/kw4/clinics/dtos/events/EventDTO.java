package int221.kw4.clinics.dtos.events;

import int221.kw4.clinics.dtos.eventCategories.EventCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;

import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.Map;

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
    private String fileName;
//    private String fileUrl;
}
