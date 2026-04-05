package thanhanh.job_recruitment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
		org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration.class,
})
public class JobRecruitmentApplication {
	public static void main(String[] args) {
		SpringApplication.run(JobRecruitmentApplication.class, args);
	}

}
