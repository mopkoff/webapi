package lab9.Filters;

import lab9.DAO.TokenDAOBean;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class RequestFilter  implements ContainerRequestFilter {

    @Override
    public void filter( ContainerRequestContext requestCtx ) throws IOException {

        TokenDAOBean tokenDao = new TokenDAOBean();
        String path = requestCtx.getUriInfo().getPath();

        // IMPORTANT!!! First, Acknowledge any pre-flight test from browsers for this case before validating the headers (CORS stuff)
        if ( requestCtx.getRequest().getMethod().equals( "OPTIONS" ) ) {
            requestCtx.abortWith( Response.status( Response.Status.OK ).build() );
            return;
        }

        // Get Acesss token from the Authorization header
        /*String accessToken = requestCtx.getHeaderString("Authorization");
        accessToken = requestCtx.getHeaders().getFirst("Bearer").replace("*","");
        if (tokenDao.isTokenExist(Long.valueOf(accessToken))) {
            // Authorized
            // Create a Security Principal
            //requestCtx.setSecurityContext(new MySecurityContext(accessToken));
            requestCtx.abortWith( Response.status( Response.Status.OK ).build() );
        } else {
            // Unauthorized
            requestCtx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Requires authorization.").build());
        }*/
        return;


/*
        // For any other methods besides login, the authToken must be verified
        if ( !path.startsWith( "/login/signin/" ) ) {
            String authToken = requestCtx.getHeaderString( "auth_token" );

            // if it isn't valid, just kick them out.
            if ( tokenDAOBean.isTokenExist( Long.valueOf(authToken )) ) {
                requestCtx.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() );
            }
        }*/
    }
}