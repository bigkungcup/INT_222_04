package int221.kw4.clinics.dtos.eventCategories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EventCategoryDTO {
    private Integer id;
    private String eventCategoryName;
    private String eventCategoryDescription;
    private int eventDuration;
}
