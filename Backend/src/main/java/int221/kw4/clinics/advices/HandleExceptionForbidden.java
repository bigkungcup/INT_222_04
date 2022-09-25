package int221.kw4.clinics.advices;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class HandleExceptionForbidden extends Exception{
    public HandleExceptionForbidden(String message){
        super(message);
    }

}
