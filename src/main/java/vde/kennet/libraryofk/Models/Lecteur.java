package vde.kennet.libraryofk.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@Table
public class Lecteur {
    @Id @GeneratedValue private Integer id;

    @Column private String nomLecteur;
    @Column private String prenomLecteur;
    @Column private String mail;
    @Column private String telephone;
    @Column private String password;

    @OneToMany (mappedBy = "lecteur", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Emprunt> emprunts = new LinkedList<Emprunt>();
}
