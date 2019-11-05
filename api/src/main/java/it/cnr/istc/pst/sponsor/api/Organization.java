package it.cnr.istc.pst.sponsor.api;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Organization
 */
public class Organization {

    private long id;
    private String name;
    private List<Affiliation> affiliated;

    @JsonCreator
    public Organization(@JsonProperty("id") long id, @JsonProperty("name") String name,
            @JsonProperty("affiliated") List<Affiliation> affiliated) {
        this.id = id;
        this.name = name;
        this.affiliated = affiliated;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the affiliated
     */
    public List<Affiliation> getAffiliated() {
        return Collections.unmodifiableList(affiliated);
    }
}