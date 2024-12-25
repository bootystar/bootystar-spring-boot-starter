package io.github.bootystar.starter.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author bootystar
 */
@Data
@ConfigurationProperties(prefix = "bootystar.jackson")
public class MybatisPlusProperties {
    private boolean enabled = true;
}
