package vde.kennet.libraryofk.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vde.kennet.libraryofk.Models.Emprunt;
import vde.kennet.libraryofk.Models.EtatLivre;
import vde.kennet.libraryofk.Models.Lecteur;
import vde.kennet.libraryofk.Models.Livre;
import vde.kennet.libraryofk.Service.BookService;
import vde.kennet.libraryofk.Service.EmpruntService;
import vde.kennet.libraryofk.Service.LecteurService;

import java.time.LocalDate;
// du texte pour tester le git
@RestController
public class RESTEmpruntController {
    @Autowired EmpruntService service;
    @Autowired BookService bService;
    @Autowired LecteurService lService;

    @GetMapping("/users/{id}/books")
    public Iterable<Livre> getBooksBorrowed(@PathVariable("id") int id) {return service.getBooksEmpruntes(id);}

    @PostMapping("/users/{id}/books/{ISBN}")
    public boolean emprunter ( @PathVariable("id") int id, @PathVariable("ISBN") String ISBN) {
        LocalDate retour = LocalDate.now().plusWeeks(2);
        return service.emprunterLivre(lService.getLecteur(id).get(), bService.getLivre(ISBN), retour);
    }

    @PutMapping("/users/{id}/books/{ISBN}/{date}")
    public Emprunt etendreEmprunt ( @PathVariable("id") int id, @PathVariable("ISBN") String ISBN, @PathVariable("date")long more) {
        Emprunt emprunt = service.thisEmprunt(id,ISBN);
        LocalDate extension = emprunt.getDateRetourAttendue().plusWeeks(more);
        service.allongerEmprunt(emprunt,extension);
        return emprunt;
    }

    @PostMapping("/users/{id}/books/{ISBN}/end")
    public boolean rendreEmprunt ( @PathVariable("id") int id, @PathVariable("ISBN") String ISBN) {
        Emprunt emprunt = service.thisEmprunt(id, ISBN);
        if (emprunt.getLivre().getEtat() != EtatLivre.DISPONIBLE) {
            service.retournerLivre(emprunt);
            return true;
        } else return false;
    }

}
