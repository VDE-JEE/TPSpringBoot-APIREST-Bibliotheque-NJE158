package vde.kennet.libraryofk.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.Filter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import vde.kennet.libraryofk.Service.LecteurService;

import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {
    private LecteurService lecteurService;
    private JwtService jwtService;
    public JwtFilter (LecteurService lecteurService, JwtService jwtService) {
        this.lecteurService = lecteurService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username = null;
        boolean isTokenExpired = true;
        System.out.println("internalFilter en cours...");
        final String authorization = request.getHeader("Authorization"); // va récup le contenu du Header (donc notre token)

        if (authorization !=null) {
            System.out.println("Auth exists...");
            if (authorization.startsWith("Bearer ")){
                System.out.println("Auth exists. token substring en cours...");
                token = authorization.substring(7); //afin de récupérer à partir du 7e char le token
                //on peut aussi stocker le token en bdd pour fluidifier les échanges tkt
            }
            isTokenExpired = jwtService.isTokenExpired(token);
            username = jwtService.extractUsername(token);
            //les fonctions créées dans le JwtService
        }

        if (!isTokenExpired && username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //en gros on dit, s'il y a personne d'authentifié, et un username avec un token non expiré
            System.out.println("user found but non auth yet...");
            UserDetails userDetails =  lecteurService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                    (userDetails, null, userDetails.getAuthorities());
            //on va placer le contenu des userdetails dans le usernamepasswordAuth token et mettre le tout dans le
            // SecuContextHolder
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //
            System.out.println("Token auth details set...");
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        System.out.println("filterchain en cours...");
        filterChain.doFilter(request,response);
        //pur JSE à l'ancienne, récupère la requête et renvoie la réponse pour permettre aux filtres de se suivre en chaîne
    }
}
