package org.cresplanex.core.commands.common;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnMissingBean(CommandNameMapping.class)
public class CommandsCommonAutoConfiguration {

  @Bean
  public CommandNameMapping commandNameMapping() {
    return new DefaultCommandNameMapping();
  }

}
