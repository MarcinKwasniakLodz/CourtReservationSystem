package pl.lodz.p.it.spjava.e12.crs.config;

import java.sql.Connection;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author marci
 */
@DataSourceDefinition( // Pula połączeń z domyślnym poziomem izolacji transakcji: ReadCommitted
        name = "java:app/jdbc/CourtReservationSystemDS",
        className = "org.apache.derby.jdbc.ClientDataSource",
        serverName = "localhost",
        portNumber = 1527,
        databaseName = "CourtReservationSystemDB",
        user = "crs",
        password = "crs",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@Stateless
public class JDBCConfig {
    // tworzę komponent Stateless i wstrzykuję zarządce encji korzystającego z CourtReservationSystemDB_PU, co powoduje aktywowanie tej jednostki składowania 
    // (zgodnej z JTA - Java Transaction Api), a w konsekwencji utworzenie struktur w bazie danych
    @PersistenceContext(unitName = "CourtReservationSystemPU")
    private EntityManager em;
}
