package vde.kennet.libraryofk.Controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vde.kennet.libraryofk.DTO.AuthentificationDTO;
import vde.kennet.libraryofk.Models.Lecteur;
import vde.kennet.libraryofk.Security.JwtService;
import vde.kennet.libraryofk.Service.LecteurService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class RESTUserController {
    private LecteurService lecteurService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    @PostMapping(path = "inscription")
    public void inscription(@RequestBody Lecteur user) {
        log.info("Inscription");
        this.lecteurService.inscription(user);
    }

    @PostMapping(path = "connexion")
    public Map<String,String> connexion(@RequestBody AuthentificationDTO authentificationDTO) {
        //le type de la connexion c'est pour renvoyer "token" et sa valeur
        log.info("Connexion en cours...");
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authentificationDTO.username(),
                        authentificationDTO.password(),
                        List.of(new SimpleGrantedAuthority("ROLE_USER")))
                /*authenticate la fonction authentifie en fonction de l'objet passé:
                username et mdp, ou plus selon les goûts, faut juste configurer avec le DTO pour qu'il puisse fournir */
        );
        if (authenticate.isAuthenticated()) {
            //si le user est auth, récupère son username (authDTO.username()) puis
            //crée un token JWT pour ce username
            log.info("Resultat connexion : {} ", authenticate.isAuthenticated());
            //{} permet de rajouter une var après, isAuthenticated est un bool qui te dit si l'authentication a marché
            return this.jwtService.generate(authentificationDTO.username());
        }else{
            log.info("Echec de connexion");
        }
        return null;
    }

}
