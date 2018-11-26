package com.inno72.common;

import com.inno72.config.client.AbstractProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "inno72.machineCheckAppBackend")
public class MachineCheckAppBackendProperties extends AbstractProperties {

}
