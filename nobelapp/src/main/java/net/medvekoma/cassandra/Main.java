package net.medvekoma.cassandra;

public class Main {

    public static void main(final String[] args){

        CassandraConnector connector = new CassandraConnector();
        connector.connect("node1", "nobel");
        connector.close();
    }
}
