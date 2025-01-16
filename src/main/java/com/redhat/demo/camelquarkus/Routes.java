package com.redhat.demo.camelquarkus;

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
        .setBody(constant("Hello from Quarkus"));
    }
    
}
