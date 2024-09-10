package com.example.HarmonyVault.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;


@Entity // Marks class as JPA entity, meaning it maps to a database table.
@Getter // Lombok annotations that automatically generate getter and setter methods for all fields.
@Setter
@NoArgsConstructor // Generates a no-argument constructor.
@AllArgsConstructor // Generates a constructor that accepts all fields as arguments.
@JsonInclude(JsonInclude.Include.NON_DEFAULT) // Ensures that only non-defaul/empty values are included in JSON serialization.
@Table(name = "artists") // database associated is named "artists"
public class Artist {
    @Id // id is the primary key
    @UuidGenerator // auto generate UUID for id field
    @Column(name="id", unique = true, updatable = false) // maps the 'id' field to a column in db, no 2 rows can be the same, immutable
    private String id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Genre genre; // same package, no need to import
    private String country;
    @Lob
    private String biography;
    private String photoUrl;

}
