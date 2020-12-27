package com.erayerdem.calories;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.gargoylesoftware.htmlunit.WebClient;


@Configuration
public class Conf {

    @Bean
    public WebClient webClient() {

        WebClient client = new WebClient();

        client.getCookieManager().setCookiesEnabled(true);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        client.getOptions().setRedirectEnabled(true);

        return client;
    }
}
