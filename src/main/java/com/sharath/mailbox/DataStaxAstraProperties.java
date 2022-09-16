package com.sharath.mailbox;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@ConfigurationProperties(prefix = "datastax.astra")
public class DataStaxAstraProperties {
   private File SecureConnectBundle;

    public File getSecureConnectBundle() {
        return SecureConnectBundle;
    }

    public void setSecureConnectBundle(File secureConnectBundle) {
        SecureConnectBundle = secureConnectBundle;
    }
}
