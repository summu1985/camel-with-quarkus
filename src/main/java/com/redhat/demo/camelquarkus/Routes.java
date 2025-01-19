package com.redhat.demo.camelquarkus;

import java.nio.charset.StandardCharsets;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

public class Routes extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        // TODO Auto-generated method stub
        restConfiguration().bindingMode(RestBindingMode.json);
        rest("/token")
                .post().type(input.class)
                .to("direct:getToken");

        from("direct:getToken")
        .log("Recieved request with body : ${body}")
        //.convertBodyTo(String.class)
        //.unmarshal(new JacksonDataFormat(input.class))
        .removeHeaders("*")
        .process(exchange->{
            input customerInput = exchange.getIn().getBody(input.class);
            exchange.getIn().setHeader("client_id", customerInput.getClientid());
            exchange.getIn().setHeader("client_secret", customerInput.getClientsecret());
        })
        //.to("log:DEBUG?showBody=true&showHeaders=true")
        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
        .setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
            // ensure that you use correct client if and client secret
            //.setBody(simple("grant_type=client_credentials&client_id={{client.id}}&client_secret={{client.secret}}"))
        .setBody(simple("grant_type=client_credentials&client_id=${header.client_id}&client_secret=${header.client_secret}"))
            // Change the below url to your keycloak token endpoint
            // show the body
        //.log("${body}")
        .toD("{{sso.token.endpoint}}")
            //.unmarshal().base64().log("${body}");
        .process(exchange->{
            byte[] ssoResponseBytes = (byte[]) exchange.getIn().getBody();
                String ssoResponseBytesToStrings = new String(ssoResponseBytes, StandardCharsets.UTF_8);
                System.out.println("sso response : " + ssoResponseBytesToStrings);
                exchange.getIn().setBody(ssoResponseBytesToStrings, String.class);


        })
        .unmarshal()
        .json(JsonLibrary.Jackson, SsoResponse.class)
        .to("log:DEBUG?showBody=true&showHeaders=true")
        .setHeader(Exchange.CONTENT_TYPE, constant("application/json"));
    }
    
}
