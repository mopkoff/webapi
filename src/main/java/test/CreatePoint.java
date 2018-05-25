package test;

import lab9.Model.PointEntity;

import javax.persistence.*;

public class CreatePoint {
    public static void main(String[] args) {

        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");

        EntityManager entitymanager = emfactory.createEntityManager();
        entitymanager.getTransaction().begin();

        PointEntity pointEntity = new PointEntity(-1,-2,3);

        entitymanager.persist(pointEntity);
        entitymanager.getTransaction().commit();

        entitymanager.close();
        emfactory.close();
    }
}
