package br.com.fmc.votacao.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fmc.votacao.dto.VotacaoDto;

@Component
public class Sender {

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private Queue queue;
	
	public void send(VotacaoDto v) {
		this.template.convertAndSend(queue.getName(), v);
	}

}
