package com.delivery.payment.config;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaypalConfig {

    @Value("${paypal.clientId}")
    private String clientId;

    @Value("${paypal.clientSecret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;
   @Bean
    public Map<String,String> paypalSDKConfig() {
        Map<String,String> configPaypal = new HashMap<>();
        configPaypal.put("mode",mode);
        return configPaypal;
    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential(){
       return new OAuthTokenCredential(clientId,clientSecret,paypalSDKConfig());
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
       APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
       context.setConfigurationMap(paypalSDKConfig());
       return context;
    }
}
