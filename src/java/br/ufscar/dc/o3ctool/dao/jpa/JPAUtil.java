package br.ufscar.dc.o3ctool.dao.jpa;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil implements Serializable {
    private static EntityManagerFactory emf = null;
    private static final String PU = "obascidtoolPU";
    public static EntityManager obterConexao() {
        if (emf == null)
            emf = Persistence.createEntityManagerFactory(PU, new PersistenceProperties().get());
        return emf.createEntityManager();
    }            
}
