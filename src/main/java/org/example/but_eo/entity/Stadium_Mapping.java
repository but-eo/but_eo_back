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
public class Stadium_Mapping {

    @EmbeddedId
    private Stadium_Mapping_Key stadium_mapping_key;

    @ManyToOne
    @MapsId("stadium_id")
    @JoinColumn(name = "stadium_id", nullable = false)
    private Stadium stadium;

    @ManyToOne
    @MapsId("file_id")
    @JoinColumn(name = "file_id", nullable = false)
    private File file;
}
