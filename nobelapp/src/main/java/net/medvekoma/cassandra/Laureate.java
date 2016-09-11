package net.medvekoma.cassandra;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import static java.lang.System.out;

@Table(name = "nobel_laureates")
public class Laureate {

    @PartitionKey
    private int year;

    @ClusteringColumn
    @Column(name = "laureateid")
    private int id;

    private String bornCity;

    private String bornCountryCode;

    private String category;

    private String firstName;

    private String surName;

    public int getYear() {
        return year;
    }

    public int getId() {
        return id;
    }

    public String getBornCity() {
        return bornCity;
    }

    public String getBornCountryCode() {
        return bornCountryCode;
    }

    public String getCategory() {
        return category;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurName() {
        return surName;
    }

    public String toString() {
        return String.format("%4d. %d %s %s %s (%s)\n",
                getId(), getYear(), getBornCountryCode(), getFirstName(), getSurName(), getCategory());

    }
}
