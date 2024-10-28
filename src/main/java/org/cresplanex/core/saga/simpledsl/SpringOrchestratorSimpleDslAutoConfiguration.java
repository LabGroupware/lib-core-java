package org.cresplanex.core.saga.simpledsl;

import org.cresplanex.core.saga.orchestration.SagaOrchestratorConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SagaOrchestratorConfiguration.class)
public class SpringOrchestratorSimpleDslAutoConfiguration {
}
