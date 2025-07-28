package vde.kennet.libraryofk.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vde.kennet.libraryofk.Models.*;
import vde.kennet.libraryofk.Repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class LecteurService {
    @Autowired private UserRepository readerRepo;

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

}
