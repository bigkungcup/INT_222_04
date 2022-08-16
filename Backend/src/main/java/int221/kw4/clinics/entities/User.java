package int221.kw4.clinics.entities;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    @Lob
    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "createdOn", nullable = false, updatable = false, insertable = false)
    private Instant createdOn;

    @Column(name = "updatedOn", nullable = false, updatable = false, insertable = false)
    private Instant updatedOn;
}