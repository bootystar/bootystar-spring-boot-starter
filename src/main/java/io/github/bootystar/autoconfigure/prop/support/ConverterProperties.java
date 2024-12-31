package io.github.bootystar.autoconfigure.prop.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author bootystar
 */
@Data
public class ConverterProperties {
    private boolean enabled = true;
}