package int221.kw4.clinics.dtos.eventCategories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCategoryEditDTO {
    @NotNull(message = "CategoryName shouldn't be Null")
    @NotBlank(message = "CategoryName shouldn't be blank")
    @NotEmpty(message = "CategoryName shouldn't be Empty")
    private String eventCategoryName;

    private String eventCategoryDescription;

    @Min(value = 1, message = "the event duration is out of range 1 - 500.")
    @Max(value = 480, message = "the event duration is out of range 1 - 500.")
    private int eventDuration;
}
