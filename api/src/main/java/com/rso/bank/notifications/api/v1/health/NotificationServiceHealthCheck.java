package com.rso.bank.notifications.api.v1.health;


import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import com.rso.bank.notifications.api.v1.configuration.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

@Health
@ApplicationScoped
public class NotificationServiceHealthCheck implements HealthCheck {

    @Inject
    private RestProperties restProperties;

    private Logger log = Logger.getLogger(NotificationServiceHealthCheck.class.getName());
    @Override
    public HealthCheckResponse call() {

        if (restProperties.isHealthy()) {
            return HealthCheckResponse.named(NotificationServiceHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(NotificationServiceHealthCheck.class.getSimpleName()).down().build();
        }

    }

}
