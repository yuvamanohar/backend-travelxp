package controllers;

import play.mvc.Controller;
import play.mvc.Http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuva on 17/4/17.
 */
public class BaseController extends Controller {

    /**
     * Constant values
     */
    public static final String NEW_USER = "new_user" ;
    public static final String OLD_USER_OLD_DEVICE = "old_user_old_device" ;
    public static final String OLD_USER_NEW_DEVICE = "old_user_new_device" ;


    public Map<String, String> getAllParams(Http.Request request) {
        Map<String, String[]> queryParams = request.queryString() ;

        Map<String, String[]> postParams = request.body().asFormUrlEncoded() ;

        Map<String, String> allParamsMap = new HashMap<>() ;

        if(queryParams != null && !queryParams.isEmpty()) {
            queryParams.forEach((k, v) -> allParamsMap.put(k, v[0]));
        }

        if(postParams != null && !postParams.isEmpty()) {
            postParams.forEach((k,v) -> allParamsMap.put(k, v[0]));
        }

        return allParamsMap ;
    }
}
