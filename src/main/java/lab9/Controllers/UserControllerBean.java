package lab9.Controllers;

import lab9.DAO.UserDAOBean;
import lab9.Model.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("user")
public class UserControllerBean {
    @EJB
    UserDAOBean userDAOBean = new UserDAOBean();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addUser(User user){
        try{
            userDAOBean.addUser(user);
        }
        catch (Exception e ){
            e.printStackTrace();
            System.out.println("user adding error");
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateUser(User user){
        try{
            userDAOBean.updateUser(user);
        }
        catch (Exception e ){
            e.printStackTrace();
            System.out.println("user updating error");
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(String username){
        User result = null;
        try{
             result = userDAOBean.getUserByName(username );
        }
        catch (Exception e ){
            e.printStackTrace();
            System.out.println("user getting error");
        }
        return result;

    }


}
