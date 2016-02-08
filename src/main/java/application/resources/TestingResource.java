package application.resources;

import aspects.GraphiteCounted;
import aspects.GraphiteExceptionMetered;
import aspects.GraphiteTimed;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.security.token.TokenGenerator;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by omryhay on 01/11/2015.
 */
@Path("/api/v1/test")
@Api("Test")
@Produces(MediaType.APPLICATION_JSON)
public class TestingResource
{
    private static final String fireBaseSecretKey = "0GgopQEBikNCfeqcWFO6CVQRXyWzaN5iJckgdEJS";
    private static final Firebase fireBase = new Firebase("https://glowing-inferno-7180.firebaseio.com/");

//    @Path("/pubnub/auth")
//    public Response getPubNubKey()
//    {
//        pubnub.pamGrant("my_channel", true, true, 0, new Callback()
//        {
//            @Override
//            public void successCallback(String s, Object o)
//            {
//                super.successCallback(s, o);
//            }
//
//            @Override
//            public void errorCallback(String s, PubnubError pubnubError)
//            {
//                super.errorCallback(s, pubnubError);
//            }
//        });
//    }

    @GET
    @ApiOperation(value = "Returns a firebase token")
    @Path("/firebase/auth")
    public Response getFireBaseAuth()
    {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("uid", "1");

        TokenGenerator tokenGenerator = new TokenGenerator(fireBaseSecretKey);
        String token = tokenGenerator.createToken(payload);

        Map<String, String> result = new HashMap<String, String>();
        result.put("key", token);
        return Response.ok(result).build();
    }

    @POST
    @ApiOperation(value = "Creates a new data in the user data")
    @Path("/firebase/data")
    public Response createNewData()
    {
        fireBase.authWithCustomToken(fireBaseSecretKey, new Firebase.AuthResultHandler()
        {
            public void onAuthenticated(AuthData authData)
            {
                Random randomGenerator = new Random();
                Firebase userData = fireBase.child("/users/1/" + randomGenerator.nextInt(1000));
                userData.setValue(randomGenerator.nextInt(1000));
            }

            public void onAuthenticationError(FirebaseError firebaseError)
            {

            }
        });
        return Response.ok().build();
    }

    @GET
    @ApiOperation(value = "testing graphite")
    @Path("/test")
    @GraphiteTimed
    public Response test()
    {
        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @GET
    @ApiOperation(value = "testing graphite")
    @Path("/test2")
    @GraphiteTimed
    @GraphiteExceptionMetered
    public Response test2()
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        throw new BadRequestException();
    }

    @GET
    @ApiOperation(value = "testing graphite")
    @Path("/test3")
    @GraphiteCounted
    public Response test3()
    {
        return Response.ok().build();
    }

    @GET
    @ApiOperation(value = "testing graphite")
    @Path("/test4")
    @GraphiteCounted
    public Response test4()
    {
        throw new InternalServerErrorException();
    }
}