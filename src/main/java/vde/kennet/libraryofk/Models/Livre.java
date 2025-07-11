package vde.kennet.libraryofk.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table
public class Livre {
    @Id private String ISBN;
    //generatedValue ne marche que pour les numériques
    @Column private String titre;
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'DISPONIBLE'")
    private EtatLivre etat=EtatLivre.DISPONIBLE ;
    @Column private String auteur;

    @OneToMany (mappedBy = "livre", cascade = CascadeType.ALL)
    @JsonIgnore //pour éviter que JSON ne tourne entre le livre et l'emprunt sans fin
    private List<Emprunt> emprunts = new LinkedList<Emprunt>();

    public Livre(){}

    @PrePersist //permet de créer l'id juste avant que l'objet entre en BDD
    public void prePersist() {
        if (this.ISBN == null) {
            this.ISBN = UUID.randomUUID().toString();
        }
    }
}
