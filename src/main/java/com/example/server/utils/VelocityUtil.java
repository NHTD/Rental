package com.example.server.utils;

import lombok.experimental.UtilityClass;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

@UtilityClass
public class VelocityUtil {
    public static String generateTemplate(String folder, String fileName, VelocityContext context) {
        try{
            Properties properties = new Properties();
            properties.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, folder);
            Velocity.init(properties);
            StringWriter writer = new StringWriter();
            Velocity.mergeTemplate(fileName, "UTF-8", context, writer);
            return writer.toString();
        }catch (Exception e) {
            throw new RuntimeException("Error while generating template");
        }
    }

    public static String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
