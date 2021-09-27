package mx.edu.utez.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ApiError {

    private  String message;
    private	 String code;
    private ZonedDateTime time;

    public ApiError(String message, String code) {
        super();
        this.message = message;
        this.time = ZonedDateTime.now(ZoneId.of("America/Mexico_City"));
        this.code = code;
    }

    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
