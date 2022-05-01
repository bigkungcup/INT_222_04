package int221.kw4.clinics.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EventDTO {
    private Integer id;
    private String bookingName;
    private String bookingEmail;
    private String eventCategoryname;
    private Instant eventStartTime;
    private String eventNotes;
    private Integer eventDuration;
}
