package int221.kw4.clinics.dtos;

import int221.kw4.clinics.entities.Event;
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

public class EventByEventCategoryDTO {
    private List<Event> events;
}
