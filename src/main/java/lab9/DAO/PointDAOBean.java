package lab9.DAO;

import lab9.Model.Point;
import lab9.Model.PointEntity;
import lab9.Model.User;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

@Stateless
@LocalBean
public class PointDAOBean {
    @PersistenceContext(unitName = "Eclipselink_JPA")
    private EntityManager em;

    public PointEntity getPoint(int id) { return em.find(PointEntity.class, id);}

    public List<PointEntity> getPointListByUser(User user){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<PointEntity> cq = cb.createQuery(PointEntity.class).where();
        Root<PointEntity> pointRoot = cq.from(PointEntity.class);

        cq.select(pointRoot)
                .where(pointRoot.get("user").in(user));
        TypedQuery<PointEntity> pointQuery = em.createQuery(cq);
        List<PointEntity> list = pointQuery.getResultList();
        List<Point> result = new ArrayList<Point>();
        for (PointEntity pointEntity : list){
            result.add(new Point(pointEntity.getX(),pointEntity.getY(),pointEntity.getIsInside()));
        }
        return list;
    }

    public void addPoint(PointEntity pointEntity){em.persist(pointEntity);}

    public void updatePoints (User user, double r) {
        List<PointEntity> pointEntityList = getPointListByUser(user);
        try{
            if (!pointEntityList.isEmpty()) {
                for (PointEntity curPointEntity : pointEntityList) {
                    curPointEntity.check(r);
                    em.merge (curPointEntity);
                }
            }
        } catch (Exception e) {
            System.out.println("My WARNING: " + e.getMessage());
        }
    }

}

