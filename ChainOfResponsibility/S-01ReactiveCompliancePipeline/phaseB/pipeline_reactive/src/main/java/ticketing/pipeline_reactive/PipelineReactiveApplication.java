package ticketing.pipeline_reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PipelineReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(PipelineReactiveApplication.class, args);
	}

}
