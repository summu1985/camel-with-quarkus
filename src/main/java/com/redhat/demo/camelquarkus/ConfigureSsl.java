package com.redhat.demo.camelquarkus;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.eclipse.microprofile.config.ConfigProvider;

public class ConfigureSsl {
    
    private String password = ConfigProvider.getConfig().getValue("truststore.password", String.class);
    private String resource = ConfigProvider.getConfig().getValue("truststore.file", String.class);
    private String url = ConfigProvider.getConfig().getValue("sso.token.endpoint", String.class);

     public Endpoint setupSSLContext(CamelContext camelContext) throws Exception {

            KeyStoreParameters keyStoreParameters = new KeyStoreParameters();
            keyStoreParameters.setResource(resource);
            keyStoreParameters.setPassword(password);

            KeyManagersParameters keyManagersParameters = new KeyManagersParameters();
            keyManagersParameters.setKeyStore(keyStoreParameters);
            keyManagersParameters.setKeyPassword(password);

            TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
            trustManagersParameters.setKeyStore(keyStoreParameters);

            SSLContextParameters sslContextParameters = new SSLContextParameters();
            sslContextParameters.setKeyManagers(keyManagersParameters);
            sslContextParameters.setTrustManagers(trustManagersParameters);
            System.out.println("sslContextParameters "+ sslContextParameters);

            HttpComponent httpComponent = camelContext.getComponent("https", HttpComponent.class);
            httpComponent.setSslContextParameters(sslContextParameters);

            return httpComponent.createEndpoint(url);
        }

}
