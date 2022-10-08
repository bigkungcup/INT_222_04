package int221.kw4.clinics.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "createdOn", nullable = false, updatable = false, insertable = false)
    @CreationTimestamp
    private Instant createdOn;

    @Column(name = "updatedOn", nullable = false, updatable = false, insertable = false)
    @CreationTimestamp
    @LastModifiedBy
    private Instant updatedOn;

    @Column(name = "password", nullable = false, length = 90)
    private String password;

    @ManyToMany(mappedBy = "users")
    private Set<EventCategory> eventCategories = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Event> events = new LinkedHashSet<>();
}


