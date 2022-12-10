package com.safeline.safelinecranes.ui.sync;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.safeline.safelinecranes.ApiServices;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.FinishedResults;
import com.safeline.safelinecranes.models.Inspection;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.Result;
import com.safeline.safelinecranes.models.Rope;
import com.safeline.safelinecranes.models.RopeType;
import com.safeline.safelinecranes.models.SyncGetter;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
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

public class SyncDataProcess extends IntentService {
    public final static String SYNC_DATA_LISTENER = "sync_data_listener";
    public static MigrationDbHelper dbHelper;
    Bundle syncResponse = new Bundle();

    public SyncDataProcess() { super("SyncDataService"); }

    @Override
    public void onCreate() {
        super.onCreate();
//        android.os.Debug.waitForDebugger();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ResultReceiver receiver = intent.getParcelableExtra(SyncDataProcess.SYNC_DATA_LISTENER);
        dbHelper = MigrationDbHelper.getInstance(this);
        try {
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
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
            String userName = dbHelper.getUser().getUsername();
            Call<ResponseBody> callData = apiServices.syncData(userName);

            try {
                Log.d("Starting sync process...", "Start to sync ...");
                Response data = callData.execute();
                if (!data.isSuccessful()) {
                    syncResponse.putBoolean("data", false);
                    receiver.send(Activity.RESULT_CANCELED, syncResponse);
                    return;
                } else {
                    ResponseBody body = (ResponseBody) data.body();
                    SyncGetter syncGetter = new Gson().fromJson(body.string(), SyncGetter.class);
                    updateDatabase(syncGetter);
                }
            } catch (Exception e) {
                receiver.send(Activity.RESULT_CANCELED, syncResponse);
                return;
            }
        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            receiver.send(Activity.RESULT_CANCELED, syncResponse);
            return;
        }
        receiver.send(Activity.RESULT_OK, syncResponse);
        return;
    }

    private void updateDatabase(SyncGetter syncGetter) {
        boolean positionsUpdated = updatePositions(syncGetter.getPositions());
        syncResponse.putBoolean("positions", positionsUpdated);
        boolean wiretypesUpdated = updateWireTypes(syncGetter.getRopetypes());
        syncResponse.putBoolean("wiretypes", wiretypesUpdated);
        boolean wiresUpdated = updateWires(syncGetter.getRopes());
        syncResponse.putBoolean("wires", wiresUpdated);
        boolean inspectionsUpdated = updateInspections(syncGetter.getInspections());
        syncResponse.putBoolean("inspections", inspectionsUpdated);
        boolean fResultsUpdated = updateFinishedResults(syncGetter.getFinishedResults());
        syncResponse.putBoolean("finishedResults", fResultsUpdated);
        boolean resultsUpdated = updateResults(syncGetter.getResults());
        syncResponse.putBoolean("results", resultsUpdated);
    }

    private boolean updateResults(List<Result> results) {
        boolean created;
        for(int i=0; i<results.size(); i++) {
            created = dbHelper.addResultWithID(results.get(i));
            if(!created) {
                return false;
            }
        }
        return true;
    }
    private boolean updateFinishedResults(List<FinishedResults> finishedResults) {
        boolean created;
        for(int i=0; i<finishedResults.size(); i++) {
            created = dbHelper.addFinishedResultWithID(finishedResults.get(i));
            if(!created) {
                return false;
            }
        }
        return true;
    }
    private boolean updateInspections(List<Inspection> inspections) {
        boolean created;
        for(int i=0; i<inspections.size(); i++) {
            created = dbHelper.addInspectionWithID(inspections.get(i));
            if(!created) {
                return false;
            }
        }
        return true;
    }
    private boolean updatePositions(List<Position> positions) {
        boolean created;
        for(int i=0; i<positions.size(); i++) {
            created = dbHelper.addPositionWithID(positions.get(i));
            if(!created) {
                return false;
            }
        }
        return true;
    }
    private boolean updateWireTypes(List<RopeType> wiretypes) {
        boolean created;
        for(int i=0; i<wiretypes.size(); i++) {
            created = dbHelper.addRopeTypeWithID(wiretypes.get(i));
            if(!created) {
                return false;
            }
        }
        return true;
    }
    private boolean updateWires(List<Rope> wires) {
        boolean created;
        for(int i=0; i<wires.size(); i++) {
            created = dbHelper.addRopeWithID(wires.get(i));
            if(!created) {
                return false;
            }
        }
        return true;
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
