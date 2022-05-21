package int221.kw4.clinics.advice;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString

public class HandleExceptionOverlap extends Exception{
    public HandleExceptionOverlap(String message){
        super(message);
    }
}
