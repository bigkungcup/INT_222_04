package int221.kw4.clinics.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 50)
    private String email;


    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "createdOn", nullable = false, updatable = false, insertable = false)
    @CreationTimestamp
    private Instant createdOn;

    @Column(name = "updatedOn", nullable = false, updatable = false, insertable = false)
    @CreationTimestamp
    @LastModifiedBy
    private Instant updatedOn;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "event_category_owner",
            joinColumns = { @JoinColumn(name = "userId") },
            inverseJoinColumns = { @JoinColumn(name = "eventCategoryId") })
    private Set<EventCategory> eventCategories = new HashSet<>();

    @Column(name = "password", nullable = false)
    private String password;

    public void addEventCategory(EventCategory eventCategory) {
        this.eventCategories.add(eventCategory);
        eventCategory.getUsers().add(this);
    }

//    public void removeEventCategory(long eventCategoryId) {
//        EventCategory tag = this.eventCategories.stream().filter(t -> t.getId() == eventCategoryId).findFirst().orElse(null);
//        if (tag != null) {
//            this.eventCategories.remove(tag);
//            tag.getUsers().remove(this);
//        }
//    }
}