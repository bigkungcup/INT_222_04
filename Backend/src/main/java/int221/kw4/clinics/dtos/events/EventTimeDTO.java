package int221.kw4.clinics.dtos.events;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventTimeDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Instant eventStartTime;
    private Integer eventCategoryId;
}
