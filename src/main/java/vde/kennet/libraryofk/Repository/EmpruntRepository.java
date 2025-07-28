package vde.kennet.libraryofk.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vde.kennet.libraryofk.Models.Emprunt;
import vde.kennet.libraryofk.Models.EmpruntId;

@Repository
public interface EmpruntRepository extends CrudRepository<Emprunt, EmpruntId> {
}
