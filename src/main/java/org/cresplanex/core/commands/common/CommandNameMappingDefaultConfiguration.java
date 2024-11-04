package org.cresplanex.core.commands.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandNameMappingDefaultConfiguration {

    @Bean("org.cresplanex.core.commands.common.CommandNameMapping")
    public CommandNameMapping commandNameMapping() {
        return new DefaultCommandNameMapping();
    }
}
