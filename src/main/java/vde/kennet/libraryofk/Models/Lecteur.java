package vde.kennet.libraryofk.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class Lecteur implements UserDetails {
    @Id @GeneratedValue
    private int id;

    @Column(name = "nom") private String nomLecteur;
    @Column(name = "prenom") private String prenomLecteur;
    @Column private String mail;
    @Column(name = "tel") private String telephone;
    @Column private String password;
    private boolean actif = false;
    @OneToOne (cascade = CascadeType.ALL)
    private Role role;

    @OneToMany (mappedBy = "lecteur", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Emprunt> emprunts = new LinkedList<Emprunt>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // les rôles d'un user : Admin, client, ...
        //ça sert pour authenticationToken qui a besoin des authorities
        // (raison pour laquelle l'authority créée commence par ROLE_)
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+this.role.getLibelle()));
    }

    @Override
    public String getUsername() {
        return this.mail;
    }

    @Override
    public String getPassword() {return this.password;}

    @Override
    public boolean isAccountNonExpired() {
        return this.actif;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.actif;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.actif;
    }

    @Override
    public boolean isEnabled() {
        return this.actif;
    }
}
