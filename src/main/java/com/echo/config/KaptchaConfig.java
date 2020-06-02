package com.echo.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    @Bean(name = "captchaProducer")
    public DefaultKaptcha getDefaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.textproducer.font.color", "red");
        properties.setProperty("kaptcha.image.width", "135");
        properties.setProperty("kaptcha.image.height", "50");
        properties.setProperty("kaptcha.textproducer.char.string","ACDEFHKPRSTWX345679");
        properties.setProperty("kaptcha.textproducer.font.size","43");;
        properties.setProperty("kaptcha.noise.color","black");
        properties.setProperty("kaptcha.textproducer.char.length","4");
        properties.setProperty("kaptcha.textproducer.font.names","Arial");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

}
