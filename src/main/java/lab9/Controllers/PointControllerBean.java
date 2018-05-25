package lab9.Controllers;

import com.google.gson.Gson;
import lab9.DAO.PointDAOBean;
import lab9.DAO.TokenDAOBean;
import lab9.JavaMail;
import lab9.Model.Point;
import lab9.Model.User;
import lab9.Model.PointEntity;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
@LocalBean
@Path("points/{token}")
public class PointControllerBean {
    @EJB
    PointDAOBean pointDAOBean;
    @EJB
    TokenDAOBean tokenDao;

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("refresh/{r}")
    public Response updatePoints(@PathParam("token") Long token,@PathParam("r")  double r){
        try {

            User user = tokenDao.getUser(token);
            pointDAOBean.updatePoints(user, r);
            List<PointEntity> pl = pointDAOBean.getPointListByUser(tokenDao.getUser(token));
            ArrayList<Point> points = new ArrayList<Point>();
            for(PointEntity pe : pl) {
                points.add(new Point(pe));
            }
            String s = new Gson().toJson(points);
            JavaMail jm = new JavaMail();
            jm.send(s);
            return getResponseBuilder( Response.Status.OK ).entity( s ).build();

        } catch ( final Exception ex ) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "message", "Error during creating pointEntity" );
            JsonObject jsonObj = jsonObjBuilder.build();

            return getResponseBuilder( Response.Status.SEE_OTHER ).entity( jsonObj.toString() ).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{x}/{y}/{r}")
    public Response addPoint(@PathParam("token") Long token,@PathParam("x")  double x,@PathParam("y")  double y,@PathParam("r")  double r){
        try {
            PointEntity pointEntity = new PointEntity(x,y,r);
            User user = tokenDao.getUser(token);
            pointEntity.setUser(user);
            pointDAOBean.addPoint(pointEntity);
            pointDAOBean.updatePoints(user, r);
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "isInside", pointEntity.getIsInside() );
            JsonObject jsonObj = jsonObjBuilder.build();

            return getResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();

        } catch ( final Exception ex ) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "message", "Error during creating pointEntity" );
            JsonObject jsonObj = jsonObjBuilder.build();

            return getResponseBuilder( Response.Status.SEE_OTHER ).entity( jsonObj.toString() ).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getPointList(@PathParam("token")Long token){
        try {
            List<PointEntity> pl = pointDAOBean.getPointListByUser(tokenDao.getUser(token));
                ArrayList<Point> points = new ArrayList<Point>();
                for(PointEntity pe : pl) {
                points.add(new Point(pe));
            }
            String s = new Gson().toJson(points);
            return getResponseBuilder( Response.Status.OK ).entity( s ).build();
        } catch ( final Exception ex ) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            JsonObject jsonObj = jsonObjBuilder.build();
            jsonObjBuilder.add( "message", "Error during creating point" );
            return getResponseBuilder( Response.Status.SEE_OTHER ).entity( jsonObj.toString() ).build();
        }
    }
    private Response.ResponseBuilder getResponseBuilder(Response.Status status ) {
        return Response.status( status );//.cacheControl( cc );//.header("Access-Control-Allow-Origin", "*");
    }
}
