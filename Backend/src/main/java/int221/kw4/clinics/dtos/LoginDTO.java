package int221.kw4.clinics.dtos;

import int221.kw4.clinics.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @Size(max = 50, message = "Email should be between 1 and 50 characters in length.")
    private String email;

    @Size(min = 8,max = 14, message = "Password should be between 8 and 14 characters in length.")
    private String password;

    private Role role;
}
