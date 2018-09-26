package com.inno72.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.inno72.config.client.AbstractProperties;

@ConfigurationProperties(value = "inno72.machineChange")
public class MachineChangeProperties extends AbstractProperties {

}
