package org.cresplanex.core.commands.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandNameMappingDefaultConfiguration {

    @Bean
    public CommandNameMapping commandNameMapping() {
        return new DefaultCommandNameMapping();
    }
}
