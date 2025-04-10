package engine.configuration;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureServiceExceptionEvent;
import org.springframework.stereotype.Component;

@Component
public class Listeners {

    @EventListener
    public void on(AuthenticationFailureServiceExceptionEvent event) {
        System.out.println("AuthenticationFailureServiceExceptionEvent");
        System.out.println(event.getException().getMessage());
        System.out.println(event.getAuthentication().getPrincipal().toString());
        System.out.println(event.getAuthentication().getDetails().toString());
    }
}
