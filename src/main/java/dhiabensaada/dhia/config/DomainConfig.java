package dhiabensaada.dhia.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("dhiabensaada.dhia.domain")
@EnableJpaRepositories("dhiabensaada.dhia.repos")
@EnableTransactionManagement
public class DomainConfig {
}
