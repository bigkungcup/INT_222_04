package int221.kw4.clinics.dtos;

import int221.kw4.clinics.entities.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EventCategoryDTO {
    private Integer id;
    private String eventCategoryDescription;
    private String eventCategoryName;
    private int eventDuration;
}
