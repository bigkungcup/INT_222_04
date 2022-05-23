package int221.kw4.clinics.advices;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HandleException {
    private Date timestamp = new Date();
    private Integer status ;
    private String message ;
    private String error ;
    private String path;
}
