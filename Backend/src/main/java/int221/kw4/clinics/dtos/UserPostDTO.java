package int221.kw4.clinics.dtos;

import int221.kw4.clinics.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPostDTO {
    @NotBlank(message = "Name shouldn't be null or blank")
    @Size(max = 100, message = "Name should be between 1 and 100 characters in length.")
    private String name;

    @Email(message = "email invalid syntax", regexp = "[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.]+[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
    @NotBlank(message = "Email shouldn't be null or blank")
    @Size(max = 50, message = "Email should be between 5 and 255 characters in length.")
    private String email;

    private Role role;

    @NotBlank(message = "Email shouldn't be null or blank")
    @Size(max = 90, message = "Email should be between 5 and 255 characters in length.")
    private String password;
}
