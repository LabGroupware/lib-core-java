package org.cresplanex.core.common.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreSchemaConfiguration {

    @Bean
    public CoreSchema coreSchema(@Value("${core.database.schema:#{null}}") String coreDatabaseSchema) {
        return new CoreSchema(coreDatabaseSchema);
    }

}
