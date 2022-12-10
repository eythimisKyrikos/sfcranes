package com.safeline.safelinecranes;

import com.safeline.safelinecranes.models.DeletedObject;
import com.safeline.safelinecranes.models.DetailedResultsSynchro;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.ResultsSynchro;
import com.safeline.safelinecranes.models.Rope;
import com.safeline.safelinecranes.models.RopeType;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiServices {
    @POST("cranes/login/{username}/register")
    Call<ResponseBody> registerUser(@Path("username") String username, @Body byte[] deviceInfo);

    @POST("cranes/sync/{username}/positions")
    Call<ResponseBody> syncPositions(@Path("username") String username, @Body List<Position> positions);

    @POST("cranes/sync/{username}/ropetypes")
    Call<ResponseBody> syncTypes(@Path("username") String username, @Body List<RopeType> types);

    @POST("cranes/sync/{username}/ropes")
    Call<ResponseBody> syncRopes(@Path("username") String username, @Body List<Rope> ropes);

    @POST("cranes/sync/{username}/inspections")
    Call<ResponseBody> syncInspections(@Path("username") String username, @Body ResultsSynchro inspections);

    @POST("cranes/sync/{username}/detailedresults")
    Call<ResponseBody> syncDetails(@Path("username") String username, @Body DetailedResultsSynchro detailed_results);

    @POST("cranes/sync/{username}/deleted")
    Call<ResponseBody> syncDelete(@Path("username") String username,@Body List<DeletedObject> deletedObjects);

    @GET("cranes/sync/{username}/sync")
    Call<ResponseBody> syncData(@Path("username") String username);
}
