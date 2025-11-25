package vde.kennet.libraryofk.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    //cette classe permet de placer un ROLE à l'user, ce qui sert pour
    //authenticationToken qui a besoin des Authorities de l'user
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    //précise de placer les roles en enum dans la BDD
    //la bonne pratique serait de les precharger dans la bdd puis les récup ici
    private TypeRole libelle;

}
