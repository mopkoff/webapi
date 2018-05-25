package lab9;

import lab9.Controllers.PointControllerBean;
import lab9.Controllers.UserControllerBean;
import lab9.Controllers.UserFace;
import lab9.Filters.RequestFilter;
import lab9.Filters.ResponseFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("api")
public class MyApplication extends Application{
    @Override
    public Set<Class<?>> getClasses(){
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(PointControllerBean.class);
        classes.add(ResponseFilter.class);
        classes.add(RequestFilter.class);
        classes.add(UserControllerBean.class);
        classes.add(UserFace.class);
        return classes;
    }
}
