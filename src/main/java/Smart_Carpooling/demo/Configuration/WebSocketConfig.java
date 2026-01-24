package Smart_Carpooling.demo.Configuration;

import ch.qos.logback.classic.pattern.MessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker //enables STOMP over websocket which allows messagemapping, sendto,etc
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final WebSocketAuthInterceptor webSocketAuthInterceptor;
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
       registration.interceptors(webSocketAuthInterceptor);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { //this methods defines where clients connect
        registry.addEndpoint("/ws").
                setAllowedOriginPatterns("*");
               // addInterceptors(webSocketAuthInterceptor).
             //   frontend connects to https://localhost:8080/ws basically a websocket endpoint

    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) { //this controlls routing of messages
        registry.setApplicationDestinationPrefixes("/app");
      //  registry.setUserDestinationPrefix("/user");
        registry.enableSimpleBroker("/topic", "/queue"); //message sent from server to clinets
        //topic-> broadcast (ride chat) /queue-> private messages /user->user specific queue

    }
    @Override
    public boolean configureMessageConverters(List<org.springframework.messaging.converter.MessageConverter> messageConverters) {
        //this controls json->java conversion equivalent to @RequestBody
        DefaultContentTypeResolver resolver=new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);

        MappingJackson2MessageConverter converter=new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);
        return false;
    }
}
