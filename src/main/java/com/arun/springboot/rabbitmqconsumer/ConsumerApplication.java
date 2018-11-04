package com.arun.springboot.rabbitmqconsumer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import com.arun.springboot.rabbitmqconsumer.client.ProductListener;
import com.arun.springboot.rabbitmqconsumer.config.ConsumerConfig;

@EnableRabbit
@SpringBootApplication
public class ConsumerApplication extends SpringBootServletInitializer implements RabbitListenerConfigurer{
	@Autowired
	private ConsumerConfig applicationConfig;
	
	public ConsumerConfig getConsumerConfig() {
		return applicationConfig;
	}

	public void setConsumerConfig(ConsumerConfig consumerConfig) {
		this.applicationConfig = consumerConfig;
	}
	
	@Bean
	public ConsumerConfig applicationConfig()
	{
		return new ConsumerConfig();
	}
	
	//Get Amazon Queue
	@Bean
	public Queue getAmazonQueue()
	{
		return new Queue(getConsumerConfig().getAmazonQueueName(), false);
	}
	//Get Amazon Exchange
	@Bean
	public TopicExchange getAmazonExchange()
	{
		return new TopicExchange(getConsumerConfig().getAmazonExchangeName());
	}
	//Bind Amazon queue and exchange
	@Bean
	public Binding declareAmazonBinding()
	{
		return BindingBuilder.bind(getAmazonQueue()).to(getAmazonExchange()).with(getConsumerConfig().getAmazonRouteKey());
	}
	
	
	//Get Walmart Queue
		@Bean
		public Queue getWalmartQueue()
		{
			return new Queue(getConsumerConfig().getWalmartQueueName(), false);
		}
		//Get Walmart Exchange
		@Bean
		public TopicExchange getWalmartExchange()
		{
			return new TopicExchange(getConsumerConfig().getWalmartExchangeName());
		}
		//Bind Walmart queue and exchange
		@Bean
		public Binding declareWalmartBinding()
		{
			return BindingBuilder.bind(getWalmartQueue()).to(getWalmartExchange()).with(getConsumerConfig().getWalmartRoutekey());
		}
	
	@Bean
	public MappingJackson2MessageConverter consumerJackson2MessageConverter()
	{
		return new MappingJackson2MessageConverter();
	}
	
	@Bean
	public DefaultMessageHandlerMethodFactory messageHandlerFactory()
	{
		DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
		factory.setMessageConverter(consumerJackson2MessageConverter());
		return factory;
		
	}


	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
	{
		return application.sources(ConsumerApplication.class);
	}
	

	@Override
	public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
		registrar.setMessageHandlerMethodFactory(messageHandlerFactory());
	}
}
