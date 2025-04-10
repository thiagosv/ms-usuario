package br.com.thiagosv.usuario.infrastructure.messaging;

import br.com.thiagosv.usuario.domain.events.UsuarioEvent;
import br.com.thiagosv.usuario.domain.ports.out.EventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisherPort {

    private final KafkaTemplate<String, UsuarioEvent> kafkaTemplate;
    
    @Value("${app.kafka.topic.usuario-eventos}")
    private String topico;

    @Override
    public void publicar(UsuarioEvent evento) {
        log.info("Publicando evento {} para o tópico {}", evento.getEvento(), topico);
        
        try {
            kafkaTemplate.send(topico, evento.getEmail(), evento);
            log.info("Evento {} publicado com sucesso", evento.getEvento());
        } catch (Exception e) {
            log.error("Erro ao publicar evento {} para o tópico {}: {}", 
                      evento.getEvento(), topico, e.getMessage(), e);
        }
    }
}