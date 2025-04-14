package br.com.thiagosv.usuario.infrastructure.messaging;

import br.com.thiagosv.usuario.domain.events.UsuarioEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher {

    private final KafkaTemplate<String, UsuarioEvent> kafkaTemplate;

    @Value("${app.kafka.topic.usuario-eventos}")
    private String topico;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
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