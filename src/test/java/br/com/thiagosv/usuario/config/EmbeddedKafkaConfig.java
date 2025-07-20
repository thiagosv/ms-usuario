package br.com.thiagosv.usuario.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EmbeddedKafkaConfig {

    @Bean
    public static BeanFactoryPostProcessor embeddedKafkaConfigurer() {
        return beanFactory -> {
            try {
                ConfigurableEnvironment environment = beanFactory.getBean(ConfigurableEnvironment.class);

                EmbeddedKafkaBroker broker = new EmbeddedKafkaBroker(1, true, "autenticacao-topic", "usuario-eventos-v1");
                broker.afterPropertiesSet();

                Map<String, Object> kafkaProps = new HashMap<>();
                kafkaProps.put("spring.kafka.bootstrap-servers", broker.getBrokersAsString());

                MapPropertySource propertySource = new MapPropertySource(
                        "embeddedKafkaProperties", kafkaProps);
                environment.getPropertySources().addFirst(propertySource);

                System.setProperty("spring.kafka.bootstrap-servers", broker.getBrokersAsString());

                beanFactory.registerSingleton("embeddedKafkaBroker", broker);

                Runtime.getRuntime().addShutdownHook(new Thread(broker::destroy));
            } catch (Exception e) {
                throw new RuntimeException("Falha ao configurar o Kafka embarcado", e);
            }
        };
    }
}