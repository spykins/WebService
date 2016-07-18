package com.andela.webservice;

import com.andela.webservice.model.Flower;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Spykins on 21/06/16.
 */
public interface FlowersAPI {
    /*
        The first step in using retrofix is to define an interface tha describes the feilds that you are going to use
        Now we have FlowersApi...
        Within the API, we declare one Abstract method.. for each field we want to use
        The declaration will start with an annotation, indicating the method of the request, get post and so on
     */
    @GET("/feeds/flower.json")
    //When you type, at get, the import the annotation from retrofix
    //In the bracket, give the url of the feed, excluding the base url,
    //After, we declare the methods that we want to use
              //public void getFeed(Callback<>);
    // When declaring the method, we pass an instance of something call
    //The call back class, It is also a memeber of the retrofit library
    //In the generic, indicate the type of data, you want to get back
    //Retrofit knows how to take a Json formatted content and transform it into strongly typed plain java objects
    //The java object class, in the case Flower class must have names that exactly match the property names in the JSOn content
    //If we want to get back an Array of Objects, then it must be declared as an Array of Object in the Json feed

    public void getFeed(Callback<List<Flower>> response) ;
    //name the object that's being returned response and that's the end of the declaration

}
