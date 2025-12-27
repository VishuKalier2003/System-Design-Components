package ladder.pager_duty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PagerDutyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagerDutyApplication.class, args);
	}

}
