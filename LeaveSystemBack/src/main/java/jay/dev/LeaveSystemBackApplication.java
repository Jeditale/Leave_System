package jay.dev;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
@OpenAPIDefinition(
		info = @Info(
				title = "LeaveSystemBackend",
				version = "v1",
				description = "LeaveSystemAPI Tester",
				contact = @Contact(name = "JAY", email = "Jeditale@hotmail.com")
		)

)

public class LeaveSystemBackApplication {
	public static void main(String[] args) {
		SpringApplication.run(LeaveSystemBackApplication.class, args);
	}

}
