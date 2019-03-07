package br.com.fmc.votacao.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"votacao"})
@Configuration
public class Config {

	@Bean
	public Queue hello() {
		return new Queue("votacao.finalizada");
	}


	@Profile("sender")
	@Bean
	public Sender sender() {
		return new Sender();
	}

}
