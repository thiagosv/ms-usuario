package br.com.thiagosv.usuario.config;

import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.distribution.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MongoConfig {

    @Bean
    @Primary
    public IFeatureAwareVersion embeddedMongoVersion() {
        // Você pode escolher outra versão se necessário
        return Version.Main.V6_0;
    }
}