package int221.kw4.clinics.advices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class HandleExceptionNotFound extends Exception{
    public HandleExceptionNotFound(String message){
        super(message);
    }

}
