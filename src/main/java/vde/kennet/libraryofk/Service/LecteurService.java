package vde.kennet.libraryofk.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vde.kennet.libraryofk.Models.*;
import vde.kennet.libraryofk.Repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LecteurService implements UserDetailsService {
    private UserRepository readerRepo;
    private BCryptPasswordEncoder passwordEncoder;

    public Optional<Lecteur> getLecteur (final Integer idLecteur) {return readerRepo.findById(idLecteur); }
    public Iterable<Lecteur> getLecteurs () {return readerRepo.findAll();}
    public Lecteur           saveLecteur (Lecteur lecteur) {return readerRepo.save(lecteur);}
    public void            deleteLecteur (final Integer idLecteur) { readerRepo.deleteById(idLecteur);}


    public Iterable<Livre> getBooksEmpruntes (final Integer idLecteur) {

       return getLecteur(idLecteur).get().getEmprunts().stream()
               .map(Emprunt::getLivre)
               //on est passé de Emprunt à Livre
       //        .filter(livre -> livre.getEtat()!=EtatLivre.DISPONIBLE)
               .toList()
       ;
    }

    public void inscription(Lecteur user) {
        if (!(user.getMail().contains("@") && user.getMail().contains("."))){
            throw new RuntimeException("Le mail est invalide");
        }
        System.out.println("mail valide");

        Optional<Lecteur> userOptional = this.readerRepo.findByMail(user.getMail());
        if (userOptional.isPresent()) {
            throw new RuntimeException("Le mail est déjà utilisé");
        }

        String cryptedPass = this.passwordEncoder.encode(user.getPassword());
        //la fonction permet de crypter un mdp
        user.setPassword(cryptedPass);
        //on crypte le mot de passe reçu et on le place dans la BDD ainsi

        Role roleUser = new Role();
        roleUser.setLibelle(TypeRole.USER);
        user.setRole(roleUser);
        //on définit le rôle du user et on le lui colle aux fesses
        user.setActif(true);
        this.readerRepo.save(user);
        System.out.println("L'individu " + user + " est enregistré.");
    }

    @Override
    public Lecteur loadUserByUsername(String username) throws UsernameNotFoundException {
        //le fonction de base retourne userDetails sauf si tu fais en sorte que ton entité
        //implémente UserDetails
        System.out.println("Chargement du user...");
        return this.readerRepo.findByMail(username)
                .orElseThrow( () -> new UsernameNotFoundException("Aucun utilisateur ne correspond à ce username"));
    }
}
