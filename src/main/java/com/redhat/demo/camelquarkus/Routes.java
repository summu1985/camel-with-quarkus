package com.redhat.demo.camelquarkus;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;



public class Routes extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        // TODO Auto-generated method stub
        restConfiguration().bindingMode(RestBindingMode.json);
        rest("/token")
                .post()
                .to("direct:getToken");

        from("direct:getToken")
        .log("Recieved request with body : ${body}")
        .to("log:DEBUG?showBody=true&showHeaders=true")
        .removeHeaders("*")
        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
            // ensure that you use correct client if and client secret
            .setBody(simple("grant_type=client_credentials&client_id=1233232&client_secret=xxxxxx"))
            // Change the below url to your keycloak token endpoint
            .to("http://google.com");
        //.setBody(constant("Hello from Quarkus"));
    }
    
}
