package vde.kennet.libraryofk.Security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableMethodSecurity
//permet de config la sécurité avec des méthodes comme le auth manager
@EnableWebSecurity
public class ConfigurationSecurity {
    //afin de vérifier que les infos de login correspondent à l'user
    //spring a besoin que tu lui indiques où récup les infos user, et il donne la classe UserDetailsService pour aider

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtFilter jwtFilter;

    public ConfigurationSecurity(BCryptPasswordEncoder passwordEncoder, JwtFilter jwtFilter) {
        this.passwordEncoder = passwordEncoder;
        this.jwtFilter = jwtFilter;
   }

    //les beans suivants servent à l'authentification du user
    //Concevoir un authentification Configuration Manager (fourni par défaut par Spring)
    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        System.out.println("AuthManager en cours...");
        return authenticationConfiguration.getAuthenticationManager();
    }



    @Bean //classe instanciable
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        System.out.println("SecuFilterChain en cours...");
        return httpSecurity
                        .csrf(AbstractHttpConfigurer::disable)
                        //je veux désactiver toute requete qui n'est pas du même port/nom
                        .authorizeHttpRequests( authorize ->authorize
                                                .requestMatchers(POST,"/connexion").permitAll()
                                                .requestMatchers(GET,"/books").permitAll()
                                                .requestMatchers(GET,"/users/*").permitAll()
                                                .requestMatchers(POST,"/inscription").permitAll()
                                                //autoriser toute request post contenant connexion.
                                                .anyRequest().authenticated()
                                //demander l'auth si ce n'est pas le cas.
                        )
                        .exceptionHandling(ex -> ex
                                .authenticationEntryPoint((req, res, e) -> {
                                    System.out.println("AUTH ERROR: " + e.getMessage());
                                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                                })
                                .accessDeniedHandler((req, res, e) -> {
                                    System.out.println("ACCESS DENIED: " + e.getMessage());
                                    res.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
                                })
                        )
                        .sessionManagement( httpSecuritySessionManagementConfigurer ->
                                        //on va commencer à concevoir les paramètres de gestion de session
                                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                //cette sessionCreationPolicy.Stateless présume que le code ne conserve pas les infos de connexion.
                                //a chaque requete du user, on devra vérifier son token pour s'assurer de son identité

                        )

                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        //par défaut il utilise un username&passwordAuthentificationFilter, mais nous on va créer notre propre filtre
                        //notre filtre créé via OncePerRequestFilter, fera que
                        .build();
    }

    @Bean
    //la classe UserDetailsService ici présente permet de récupérer les infos de la classe user pour donner
    public AuthenticationProvider authenticationProvider (UserDetailsService userDetailsService) {
        //c'est un bean de Spring Secu qui gère l'accès à la bdd
        //le Manager gère la connexion et s'appuie sur le provider qui s'appuie sur la BDD
        //si on ne l'ajoutait pas on aurait eu des soucis lors de la connexion avec la bdd
        System.out.println("DAO AuthProvider en cours...");
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider() ;
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        // avec ceci l'user peut se auth. On doit réencoder le mdp pour que ça corresponde au contenu en bdd

        return daoAuthenticationProvider;
    }
}
