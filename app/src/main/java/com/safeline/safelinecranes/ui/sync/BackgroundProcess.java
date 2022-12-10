package com.safeline.safelinecranes.ui.sync;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;

import com.safeline.safelinecranes.ApiServices;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.DeletedObject;
import com.safeline.safelinecranes.models.DetailedResultsSynchro;
import com.safeline.safelinecranes.models.FinishedResults;
import com.safeline.safelinecranes.models.Inspection;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.Result;
import com.safeline.safelinecranes.models.ResultsSynchro;
import com.safeline.safelinecranes.models.Rope;
import com.safeline.safelinecranes.models.RopeType;
import com.safeline.safelinecranes.models.Synchronizer;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.safeline.safelinecranes.MainActivity.baseUrl;

public class BackgroundProcess extends IntentService {

    public final static String BUNDLED_LISTENER = "listener";
    Synchronizer synchronizer;

    public BackgroundProcess() {
        super("BackgroundService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        synchronizer = createSynchronizer();
        ResultReceiver receiver = intent.getParcelableExtra(BackgroundProcess.BUNDLED_LISTENER);
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this);
        Bundle syncResponse = new Bundle();
        try{
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    }).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiServices apiServices = retrofit.create(ApiServices.class);
            Call<ResponseBody> callPos = apiServices.syncPositions(synchronizer.getUsername(), synchronizer.getPositions());
            Call<ResponseBody> callTypes = apiServices.syncTypes(synchronizer.getUsername(), synchronizer.getRopeTypes());
            Call<ResponseBody> callRopes = apiServices.syncRopes(synchronizer.getUsername(),synchronizer.getRopes());
            ResultsSynchro inspectionsWithResults = new ResultsSynchro();
            inspectionsWithResults.setfResults(synchronizer.getResults());
            inspectionsWithResults.setInspections(synchronizer.getInspections());
            Call<ResponseBody> callInspections = apiServices.syncInspections(synchronizer.getUsername(), inspectionsWithResults);
            DetailedResultsSynchro inspectionWtihDetails = new DetailedResultsSynchro();
            inspectionWtihDetails.setInspections(synchronizer.getInspections());
            inspectionWtihDetails.setResults(synchronizer.getDetailed_results());
            Call<ResponseBody> callDetails = apiServices.syncDetails(synchronizer.getUsername(),inspectionWtihDetails);
            Call<ResponseBody> callDeleted = apiServices.syncDelete(synchronizer.getUsername(), synchronizer.getDeletedObjects());

            try {
                Response resultPos = callPos.execute();
                if(!resultPos.isSuccessful()) {
                    syncResponse.putBoolean("positions", false);
                } else {
                    try{
                        ResponseBody body = (ResponseBody) resultPos.body();
                        JSONArray myResponse = new JSONArray(body.string());
                        for (int i = 0; i < myResponse.length(); i++) {
                            int positionID = (int) myResponse.get(i);
                            Position position = dbHelper.getPositionById(positionID);
                            dbHelper.updatePositionMakeSync(position);
                            syncResponse.putBoolean("positions", true);
                        }
                    } catch (JSONException e) {
                        syncResponse.putBoolean("positions", false);
                    }
                }
                Response resultTypes = callTypes.execute();
                if(!resultTypes.isSuccessful()) {
                    syncResponse.putBoolean("rope_types", false);
                } else {
                    try{
                        ResponseBody body = (ResponseBody) resultTypes.body();
                        JSONArray myResponse = new JSONArray(body.string());
                        for (int i = 0; i < myResponse.length(); i++) {
                            int ropeTypeID = (int) myResponse.get(i);
                            RopeType ropeType = dbHelper.getRopeTypeById(ropeTypeID);
                            dbHelper.updateRopeTypeMakeSync(ropeType);
                            syncResponse.putBoolean("rope_types", true);
                        }
                    } catch (JSONException e) {
                        syncResponse.putBoolean("rope_types", false);
                    }
                }
                Response resultRopes = callRopes.execute();
                if(!resultRopes.isSuccessful()) {
                    syncResponse.putBoolean("ropes", false);
                } else {
                    try{
                        ResponseBody body = (ResponseBody) resultRopes.body();
                        JSONArray myResponse = new JSONArray(body.string());
                        for (int i = 0; i < myResponse.length(); i++) {
                            int ropeID = (int) myResponse.get(i);
                            Rope rope = dbHelper.getRopeById(ropeID);
                            dbHelper.updateRopeMakeSync(rope);
                            syncResponse.putBoolean("ropes", true);
                        }
                    } catch (JSONException e) {
                        syncResponse.putBoolean("ropes", false);
                    }
                }
                Response resultDetails = callDetails.execute();
                if(!resultDetails.isSuccessful()) {
                    syncResponse.putBoolean("resultDetails", false);
                }
                Response resultInspections = callInspections.execute();
                if(!resultInspections.isSuccessful()) {
                    return;
                } else {
                    try{
                        ResponseBody body = (ResponseBody) resultInspections.body();
                        JSONArray myResponse = new JSONArray(body.string());
                        for (int i = 0; i < myResponse.length(); i++) {
                            int inspectionID = (int) myResponse.get(i);
                            FinishedResults fResult = dbHelper.getFinishedResultByInspection(inspectionID);
                            fResult.setSync(true);
                            dbHelper.updateFinishedResult(fResult);
                            syncResponse.putBoolean("resultDetails", true);
                        }
                    } catch (JSONException e) {
                        syncResponse.putBoolean("resultDetails", false);
                    }
                }
                Response resultDeleted = callDeleted.execute();
                if(!resultDeleted.isSuccessful()) {
                    syncResponse.putBoolean("deleted", false);
                } else {
                    try{
                        ResponseBody body = (ResponseBody) resultDeleted.body();
                        JSONArray myResponse = new JSONArray(body.string());
                        for (int i = 0; i < myResponse.length(); i++) {
                            int deleted = (int) myResponse.get(i);
                            dbHelper.deleteObjectToDelete(deleted);
                            syncResponse.putBoolean("deleted", true);
                        }
                    } catch (JSONException e) {
                        syncResponse.putBoolean("deleted", false);
                    }
                }
            } catch (IOException ex) {
                receiver.send(Activity.RESULT_CANCELED, syncResponse);
                return;
            }
        } catch (Exception e) {
            receiver.send(Activity.RESULT_CANCELED, syncResponse);
            return;
        }
        receiver.send(Activity.RESULT_OK, syncResponse);
    }


    private Synchronizer createSynchronizer() {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this);
        String userName = dbHelper.getUser().getUsername();
        List<Position> unSyncedPositions = dbHelper.getAllNonSyncPositions();
        List<RopeType> unSyncedRopeTypes = dbHelper.getAllNonSyncRopeTypes();
        List<Rope> unSyncedRopes = dbHelper.getAllNonSyncRopes();
        List<FinishedResults> unSyncedResults = dbHelper.getUnsyncedFinishedResults();
        List<Inspection> unSyncedInspections = new ArrayList<>();
        if(unSyncedResults.size() > 0){
            unSyncedInspections = dbHelper.getAllInspectionsOfUnsynced(unSyncedResults);
        }
        List<Result> unSyncedDetailedResults = new ArrayList<>();
        if(unSyncedInspections.size() > 0) {
            unSyncedDetailedResults =dbHelper.getAllResultsOfNonSyncedInspections(unSyncedInspections);
        }
        List<DeletedObject> deletedObjects = dbHelper.getDeletedObjects();

        Synchronizer synchronizer = new Synchronizer();
        synchronizer.setUsername(userName);
        synchronizer.setPositions(unSyncedPositions);
        synchronizer.setRopeTypes(unSyncedRopeTypes);
        synchronizer.setRopes(unSyncedRopes);
        synchronizer.setInspections(unSyncedInspections);
        synchronizer.setResults(unSyncedResults);
        synchronizer.setDetailed_results(unSyncedDetailedResults);
        synchronizer.setDeletedObjects(deletedObjects);
        return synchronizer;
    }
    final TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
    };
}
