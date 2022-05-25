package int221.kw4.clinics.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EventCategoryPostDTO {
    @NotNull(message = "CategoryName shouldn't be Null")
    @NotBlank(message = "CategoryName shouldn't be blank")
    @NotEmpty(message = "CategoryName shouldn't be Empty")
    private String eventCategoryName;

    @Size(max = 500, message = "the event duration is out of range 0 - 500.")
    private String eventCategoryDescription;

    @Min(value = 1, message = "the event duration is out of range 0 - 480.")
    @Max(value = 480, message = "the event duration is out of range 0 - 480.")
    private int eventDuration;
}
