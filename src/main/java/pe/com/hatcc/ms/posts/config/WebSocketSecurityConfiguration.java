package pe.com.hatcc.ms.posts.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.messaging.access.intercept.ChannelSecurityInterceptor;

@Configuration
public class WebSocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
        .nullDestMatcher().authenticated()
        .simpTypeMatchers(SimpMessageType.DISCONNECT,SimpMessageType.UNSUBSCRIBE).permitAll()
        .simpTypeMatchers(SimpMessageType.CONNECT).hasAnyRole("PACIENTE")
        .simpSubscribeDestMatchers("/topic/registros").hasRole("PACIENTE")
        .anyMessage().denyAll();
        
    }
    
    @Override
    protected boolean sameOriginDisabled() {
    	return true;
    }
}