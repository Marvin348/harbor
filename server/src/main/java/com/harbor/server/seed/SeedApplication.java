package com.harbor.server.seed;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.harbor.server.common.security.PasswordConfig;

@SpringBootApplication(scanBasePackages = "com.harbor.server.seed")
@EntityScan(basePackages = "com.harbor.server.features")
@EnableJpaRepositories(basePackages = "com.harbor.server.features")
@Import(PasswordConfig.class)
public class SeedApplication {

  public static void main(String[] args) {
    try (ConfigurableApplicationContext context =
        new SpringApplicationBuilder(SeedApplication.class)
            .web(WebApplicationType.NONE)
            .run(args)) {

      SeedDataService seedDataService = context.getBean(SeedDataService.class);

      seedDataService.seed();
    }
  }
}
