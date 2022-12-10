package com.safeline.safelinecranes.ui.register;

import android.util.Base64;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.safeline.safelinecranes.ApiServices;
import com.safeline.safelinecranes.Resource;
import com.safeline.safelinecranes.models.User;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.safeline.safelinecranes.MainActivity.baseUrl;

public class RegisterViewModel extends ViewModel {
    final MutableLiveData<Resource<User>> newUser = new MutableLiveData<>();

    public LiveData<Resource<User>> getVerificationCert(String username) {
        String deviceInfo = XORENC(android.os.Build.DEVICE);
        addUserToDatabase(username, deviceInfo);
        return newUser;
    }

    private void addUserToDatabase(String username, String deviceInfo) {
        try {
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

            ApiServices services = retrofit.create(ApiServices.class);
            byte[] deviceInfobytes = deviceInfo.getBytes("UTF-8");
            Call<ResponseBody> registerUser = services.registerUser(username, deviceInfobytes);
            registerUser.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        User registeredUser = new User();
                        try {
                            String license = new String(response.body().bytes());
                            byte[] data = Base64.decode(license, Base64.DEFAULT);
                            String text = new String(data, "UTF-8");
                            String lic = XORENC(text);
                            if(isCertValid(XORENC(text).split("\n")[3])){
                                registeredUser.setUsername(XORENC(text).split("\n")[0]);
                                registeredUser.setPassword(XORENC(text).split("\n")[1]);
                                registeredUser.setDevice(deviceInfo);
                                registeredUser.setVessel(XORENC(text).split("\n")[2]);
                                registeredUser.setLicense(text);
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = format.parse(XORENC(text).split("\n")[3]);
                                registeredUser.setDatetime(date);
                                newUser.setValue(Resource.success(registeredUser));
                            }
                        } catch (IOException | ParseException e) {
                            if(e instanceof ParseException) {
                                newUser.setValue(Resource.error("Certificate is not valid", null));
                                return;
                            } else {
                                newUser.setValue(Resource.error("There is a problem with the certificate.", null));
                                return;
                            }
                        }
                    } else {
                        newUser.setValue(Resource.error("Certificate is not found. Please give a correct username", null));
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    newUser.setValue(Resource.error(t.getMessage(), null));
                }
            });
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String XORENC(String input)
    {
        char[] key = { 'K', 'C', 'Q' };
        char[] output = new char[input.length()];
        for (int i = 0; i < input.length(); i++)
        {
            output[i] = (char)(input.charAt(i) ^ key[i % key.length]);
        }
        return new String(output);
    }

    private boolean isCertValid(String dateString){
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = format.parse(dateString);
            if (new Date().before(date)) {
                return true;
            }
            else{
                return false;
            }
        }catch(ParseException ex){
            return false;
        }

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