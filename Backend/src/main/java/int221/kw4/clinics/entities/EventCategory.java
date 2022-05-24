package int221.kw4.clinics.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Table(name = "eventCategory")
public class EventCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventCategoryId", nullable = false)
    private Integer id;

    @Column(name = "eventCategoryName", nullable = false, length = 100 ,unique = true)
    private String eventCategoryName;

    @Column(name = "eventCategoryDescription", length = 500)
    private String eventCategoryDescription;

    @Column(name = "eventDuration", nullable = false)
    private Integer eventDuration;
}