package lab9.Controllers;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.json.Json;
import javax.security.auth.login.LoginException;

import lab9.DAO.TokenDAOBean;
import lab9.DAO.UserDAOBean;
import lab9.Model.User;
import org.apache.oltu.oauth2.client.OAuthClient;

import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.jwt.ClaimsSet;
import org.apache.oltu.oauth2.jwt.JWT;
import org.apache.oltu.oauth2.jwt.io.JWTReader;

@Path("login")
@Stateless
@LocalBean
public class UserFace {
    @EJB
    private TokenDAOBean tokenDao;
    @EJB
    private UserDAOBean userDao;
    @Context
    private UriInfo uriInfo;
    @POST
    @Path("google")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response google(JsonObject code) throws OAuthProblemException, OAuthSystemException {

        String stringCode = code.getString("code");
        String clientId = code.getString("clientId");
        String redirectUri =code.getString("redirectUri");
        String clientSecret = "2zAIfmE9lynYYrlARUh1vPIY";
        OAuthClientRequest request = OAuthClientRequest
                .tokenProvider(OAuthProviderType.GOOGLE)
                .setCode(stringCode)
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectURI(redirectUri)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .buildBodyMessage();

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request);

        // Get the access token from the response
        String accessToken = oAuthResponse.getParam("id_token");
        JWT jwt = new JWTReader().read(accessToken);
        ClaimsSet claimsSet = jwt.getClaimsSet();
        String subject = claimsSet.getSubject();
        Long time = claimsSet.getExpirationTime();

        try {
            User user = new User(subject);
            if (userDao.getUserByName(subject) == null)
                userDao.addUser(user);
            Long authToken =  tokenDao.getToken(user);
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "auth_token", authToken );
            JsonObject jsonObj = jsonObjBuilder.build();

            return getResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();

        } catch ( final Exception ex ) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "message", "Error during login with google" );
            JsonObject jsonObj = jsonObjBuilder.build();

            return getResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        }
    }

    @POST
    @Path("signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(User user) {
        try {
            long authToken =  tokenDao.getToken(user);
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "auth_token", authToken );
            JsonObject jsonObj = jsonObjBuilder.build();

            return getResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();

        } catch ( final LoginException ex ) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "message", "Error during matching username and password" );
            JsonObject jsonObj = jsonObjBuilder.build();

            return getResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        }
    }

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(User user) {
        try {
            userDao.addUser(user);
            Long authToken =  tokenDao.getToken(user);
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "auth_token", authToken );
            JsonObject jsonObj = jsonObjBuilder.build();

            return getResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();

        } catch ( final Exception ex ) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "message", "Error during creating user: User already exist" );
            JsonObject jsonObj = jsonObjBuilder.build();

            return getResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteToken(Long token) {
        try {
            tokenDao.removeToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("token delete error");
        }
    }
    private Response.ResponseBuilder getResponseBuilder(Response.Status status ) {
        return Response.status( status );//.cacheControl( cc );//.header("Access-Control-Allow-Origin", "*");
    }
/*
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public UserInformation getTicket(UserInformation userInfo) {
        if (userInfo == null)
            return new UserInformation("", "", -1L);
        User user = userDao.getUserByName(userInfo.getUsername());
        if (user == null) {
            userInfo.setToken(-1L);
        }
        else if (user.getPasswordHash() == userInfo.getPass().hashCode()) {
            userInfo.setToken(tokenDao.getToken(user));
        }
        else {
            userInfo.setToken(-1L);
        }
        return userInfo;
    }
*/
}
