package vde.kennet.libraryofk.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import vde.kennet.libraryofk.Models.Lecteur;
import vde.kennet.libraryofk.Service.LecteurService;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@AllArgsConstructor
@Service
public class JwtService {
    private final String CRYPTAGE_KEY = "74270db670e510ac4b196b2375d2956eef5f51c237946e3b578e917eccb72e6f";
    private LecteurService lecteurService;

    private Key getKey() {
        final byte[] decode = Decoders.BASE64.decode(CRYPTAGE_KEY);
        System.out.println("Génération de Key en cours...");
        //tu mets une clé de cryptage dedans et ça te sort une clé jwt
        return Keys.hmacShaKeyFor(decode);
    }

    public Map<String, String> generate(String username) {
        System.out.println("Génération de token en cours...");
        Lecteur lecteur = this.lecteurService.loadUserByUsername(username);
        return this.generateJWT(lecteur);
    }

    public String extractUsername (String token) {
       // return Jwts.parser().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getSubject();
        return this.getClaim(token, Claims::getSubject);
        //on récup le subject qu'on avait placé dans le token (le mail)
    }

    public boolean isTokenExpired (String token) {
        // return Jwts.parser().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
        System.out.println("vérification d'expiration du token en cours...");
        Date expirationDate = this.getClaim(token, Claims::getExpiration); //on récupère la date d'exp qu'on avait placé dans le claim
        return expirationDate.before(new Date());
    }

    private <T> T getClaim(String token, Function<Claims, T> function) { //au vu des paramètres, ce qu'on veut c'est une fonction
        Claims claims = getAllClaims(token);
        //si on récupère tous les claims
        return function.apply(claims);
        //on veut appliquer à ces claims une fonction (en soit getExpiration).
        //la fonction a été créée pour récupérer un contenu d'un claim (subject, expiration, ...)
    }

    private Claims getAllClaims(String token) { //si tu lui donne un token, il renvoie tous les Claims associés
        return Jwts.parser()
                //contrebuilder, donc récupérer le contenu d'un token à partir du token
                .setSigningKey(this.getKey())//depuis la clé
                .build()
                //concevoir l'objet contenu dans ce type de token
                .parseClaimsJws(token)
                //obtenir le type claims depuis ce token précis
                .getBody(); //extraire lesdits claims
        //grossomodo avec la fonction juste en dessous le token contient : claims: nom, EXPIRATION, SUBJECT
    }


    private Map<String, String> generateJWT(Lecteur lecteur) {
        //fonction de génération de token
        System.out.println("Génération du JWT en cours...");
        final long currentTime = System.currentTimeMillis();
        final long expired = currentTime + (30 * 60 * 1000) ; //la durée de validité du token

        final Map<String,Object> claims = Map.of(
                "nom", lecteur.getNomLecteur(),
                Claims.EXPIRATION, new Date(expired),
                Claims.SUBJECT, lecteur.getMail()
        ); //on crée un truc qui dit que ceci c'est les identifiants d'un user

        final String bearer = Jwts.builder()
                //c'est un concepteur de JWT. On les appelle souvent bearer (le porteur)
                .setIssuedAt(new Date(currentTime))     .setExpiration(new Date(expired))
                .setSubject(lecteur.getMail())            .setClaims(claims) //les données qu'on veut use
                .signWith(getKey(), SignatureAlgorithm.HS256)
                //signature du token (clé de cryptage et algo de cryptage)
                .compact();
        System.out.println("JWT généré. passage du jwt en token en cours...");
        return Map.of("bearer",bearer); //on retourne le porteur du token
    }
}
