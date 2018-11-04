package com.arun.springboot.rabbitmqconsumer.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arun.springboot.rabbitmqconsumer.config.ConsumerConfig;
import com.arun.springboot.rabbitmqconsumer.dto.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProductListener{

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductListener.class);
	private ConsumerConfig consumerConfig;
	
	
	
	public ConsumerConfig getConsumerConfig() {
		return consumerConfig;
	}


	@Autowired
	public void setConsumerConfig(ConsumerConfig consumerConfig) {
		this.consumerConfig = consumerConfig;
	}


	@RabbitListener(queues = "${amazon.queue.name}")
	public void getMessageFromQueue(final Product product)
	{
		LOGGER.info("New Product availabile in Stock "+ product.getName() + " Quantity " + product.getQuantity());
	}
	

}
