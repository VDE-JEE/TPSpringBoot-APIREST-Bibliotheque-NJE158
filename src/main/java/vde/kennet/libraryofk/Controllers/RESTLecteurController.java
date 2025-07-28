package vde.kennet.libraryofk.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vde.kennet.libraryofk.Models.Lecteur;
import vde.kennet.libraryofk.Models.Livre;
import vde.kennet.libraryofk.Service.LecteurService;

@RestController
public class RESTLecteurController {
    @Autowired private LecteurService service;
    
    @GetMapping ("/users")
    public Iterable<Lecteur> getUsers() {return service.getLecteurs();}

    @GetMapping("/users/{id}")
    public Lecteur getUser (@PathVariable("id") int id) { return service.getLecteur(id).get();}

    @PutMapping("/users")
    public Lecteur putUser (Lecteur lecteur) { return service.saveLecteur(lecteur); }

    @PostMapping("/users")
    public Lecteur postUser (Lecteur lecteur) { return service.saveLecteur(lecteur); }

    @DeleteMapping("/users/{id}")
    public void deleteUser (@PathVariable("id") int id) { service.deleteLecteur(id); }

}
