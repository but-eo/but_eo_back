package org.example.but_eo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StadiumMapping {

    @EmbeddedId
    private StadiumMappingKey stadiumMappingKey;

    @ManyToOne
    @MapsId("stadiumId")
    @JoinColumn(name = "stadium_id", nullable = false)
    private Stadium stadium;

    @ManyToOne
    @MapsId("fileId")
    @JoinColumn(name = "file_id", nullable = false)
    private File file;
}
