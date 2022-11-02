package int221.kw4.clinics.dtos.events;

import int221.kw4.clinics.dtos.eventCategories.EventCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EventEditDTO {
    @NotNull(message = "startTime shouldn't be null")
    @FutureOrPresent(message = "startTime shouldn't be past")
    private Instant eventStartTime;

    @Size(max = 500, message = "A note should be between 0 and 500 characters in length.")
    private String eventNotes;

    private EventCategoryDTO eventCategory;

    private Integer eventDuration;

    private MultipartFile file;
}
