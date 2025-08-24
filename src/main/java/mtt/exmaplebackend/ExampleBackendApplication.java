package mtt.exmaplebackend;

import mtt.exmaplebackend.exceptioHandler.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(GlobalExceptionHandler.class)
public class ExampleBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleBackendApplication.class, args);
    }

}
