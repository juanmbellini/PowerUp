package ar.edu.itba.paw.webapp.scheduled;

import ar.edu.itba.paw.webapp.interfaces.JwtService;
import ar.edu.itba.paw.webapp.model.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

/**
 * Scheduled tasks that are meant to be run automatically in predetermined intervals. See each method to see what each
 * one does.
 */
@Component
public class ScheduledTasks {

    @Autowired
    private JwtService jwtService;

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * Deletes expired JWTs from the JWT blacklist. Since expired tokens are rejected, there is no need to keep tracking
     * them in the DB.
     */
    @Scheduled(fixedDelay = 1000 * 3600 * 24, initialDelay = 0)   //Run on app start and then every 24 hours
    @Transactional
    public void cleanExpiredJwts() {
        LOG.info("Cleaning expired JWTs...");
        long count = 0;
        //TODO consider writing everything in a single query?
        Calendar now = Calendar.getInstance();
        for(Jwt token : jwtService.all()) {
            if(token.getValidUntil() != null && token.getValidUntil().before(now)) {
                jwtService.delete(token);
                count++;
            }
        }
        LOG.info("Cleaned {} expired JWT{}", count, count == 1 ? "" : "s");
    }
}
