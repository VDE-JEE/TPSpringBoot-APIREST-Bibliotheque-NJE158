package vde.kennet.libraryofk.Models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class EmpruntId implements Serializable {
    //Si tu utilises une classe comme clé (@EmbeddedId ou @IdClass),
    // equals() et hashCode() sont obligatoires pour que JPA puisse fonctionner correctement.

    Integer id;
    String ISBN;
    LocalDate dateEmprunt;

    public EmpruntId(){
        this.dateEmprunt = LocalDate.now();
    }//indispensable pour serializer
    public EmpruntId(int id, String ISBN) {
        this.dateEmprunt = LocalDate.now();
        this.id = id;
        this.ISBN = ISBN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmpruntId)) return false;
        EmpruntId that = (EmpruntId) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(ISBN, that.ISBN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ISBN);
    }

}
