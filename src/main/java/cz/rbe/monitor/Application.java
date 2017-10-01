package cz.rbe.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application main class.
 * Before running the application, you must run selenium chromedriver process on your machine,
 * otherwise Connection refused exception will occur.
 * @author Radek Beran
 */
@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
