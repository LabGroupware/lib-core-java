package org.cresplanex.core.saga.participant;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SagaParticipantConfiguration.class)
public class SpringParticipantAutoConfiguration {
}
