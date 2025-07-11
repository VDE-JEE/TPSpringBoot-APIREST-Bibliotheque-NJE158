package vde.kennet.libraryofk.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table
public class Emprunt {
@EmbeddedId private EmpruntId id;

    @ManyToOne @MapsId ("ISBN") @JoinColumn (name = "ISBN") private Livre livre;

    @ManyToOne @MapsId ("id") @JoinColumn (name = "id") private Lecteur lecteur;

    @Column private LocalDate dateRetourAttendue ;
    @Column private LocalDate dateRenduReelle;

    public Emprunt() {
        this.dateRetourAttendue = LocalDate.now().plusWeeks(2);
    }
    public Emprunt(Lecteur lecteur, Livre livre, LocalDate dateRetourAttendue) {
        this.dateRetourAttendue = dateRetourAttendue;
        this.lecteur = lecteur; this.livre = livre;
        this.id = new EmpruntId(lecteur.getId(), livre.getISBN()); //ne pas oublier.
        livre.setEtat(EtatLivre.EMPRUNTE);
    }
}
