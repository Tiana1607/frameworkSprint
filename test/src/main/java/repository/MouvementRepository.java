package repository;

import models.Mouvement;
import org.springframework.stereotype.Repository;
import java.util.Arrays;
import java.util.List;

@Repository
public class MouvementRepository {

    public List<Mouvement> findAll() {
        return Arrays.asList(
                new Mouvement("Salaire", 1500.0),
                new Mouvement("Loyer", -800.0),
                new Mouvement("Courses", -150.0)
        );
    }
}
