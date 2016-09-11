package net.medvekoma.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;

import static java.lang.System.out;

public class Main {

    public static void main(final String[] args){

        CassandraConnector connector = new CassandraConnector();
        connector.connect("node1", "nobel");

        runExampleQuery(connector);
        runAccessorQueries(connector);

        connector.close();
    }

    private static void runExampleQuery(CassandraConnector connector){

        Session session = connector.getSession();
        MappingManager mappingManager = new MappingManager(session);

        Mapper<Laureate> mapper = mappingManager.mapper(Laureate.class);

        ResultSet results = session.execute("SELECT * FROM nobel_laureates WHERE year=2002");
        Result<Laureate> laureates = mapper.map(results);

        printLaureates(laureates, "manual mapping");
    }

    private static void printLaureates(Result<Laureate> laureates, String header) {

        out.printf("--- %s ---\n", header);

        for (Laureate laureate : laureates) {
            out.print(laureate);
        }

        out.printf("---------------------\n");
    }

    private static void runAccessorQueries(CassandraConnector connector){

        Session session = connector.getSession();
        MappingManager mappingManager = new MappingManager(session);

        LaureateAccessor accessor = mappingManager.createAccessor(LaureateAccessor.class);

        Result<Laureate> laureates = accessor.getYear2002();
        printLaureates(laureates, "year 2002");

        laureates = accessor.getByYear(2010);
        printLaureates(laureates, "year 2010");

        laureates = accessor.getByCountry("HU");
        printLaureates(laureates, "from Hungary");
    }
}
