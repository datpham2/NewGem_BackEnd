package project.source.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MultipleException extends RuntimeException {
    List<Exception> exceptions;

    public MultipleException(List<Exception> exceptions) {
        super("Multiple exceptions occurred: " + exceptions.size());
        this.exceptions = exceptions;
    }
}