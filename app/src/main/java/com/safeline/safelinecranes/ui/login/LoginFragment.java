package com.safeline.safelinecranes.ui.login;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.safeline.safelinecranes.MainActivity;
import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.MigrationDbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.safeline.safelinecranes.MainActivity.ipAddress;
import static com.safeline.safelinecranes.MainActivity.loggedUser;
import static com.safeline.safelinecranes.MainActivity.loginNavigationText;
import static com.safeline.safelinecranes.MainActivity.navController;
import static com.safeline.safelinecranes.MainActivity.userIsLogged;
import static com.safeline.safelinecranes.MainActivity.port;


public class LoginFragment extends Fragment {

    private Context mContext;
    private TextView usernameTxt;
    private TextView passwordTxt;
    private Button btnLogin;
    private Button btnRegister;

    DrawerLayout drawer;

    public LoginFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        userIsLogged = false;
        //loginNavigationText.setTitle(R.string.login);
        drawer = view.findViewById(R.id.drawer_layout);

        usernameTxt = view.findViewById(R.id.register_username);
        passwordTxt = view.findViewById(R.id.register_password);
        btnRegister = view.findViewById(R.id.btn_go_to_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_nav_login_to_nav_register);
            }
        });
        btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                navController.navigate(R.id.nav_scada);
//                loginNavigationText.setTitle("Logout");
                if (ipAddress.equals("000.000.000.000") || port.equals("0000")) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Wrong Settings")
                            .setMessage("There is no configuration for synchronization server. Please go to settings and add your server settings.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                    return;
                }
                MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
                loggedUser = dbHelper.getUser();
                if (loggedUser.getUsername() == null) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Wrong Username")
                            .setMessage("The username you entered is wrong or the user is not registered yet. Please give a correct username or register your user first.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                    return;
                }
                if (!loggedUser.getUsername().equals(usernameTxt.getText().toString())) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Wrong Username")
                            .setMessage("The username you entered is wrong or the user is not registered yet. Please give a correct username or register your user first.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                    return;
                }
                if (!passwordTxt.getText().toString().equals(loggedUser.getPassword())) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Wrong Password")
                            .setMessage("The password for the user " + loggedUser.getUsername() + " is not correct.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                    return;
                }
                String[] license = XORENC(new String(loggedUser.getLicense())).split("\n");
                if (!isCertValid(license[3])) {
                    navController.navigate(R.id.nav_scada);
                    loginNavigationText.setTitle("Logout");
                } else {
                    new AlertDialog.Builder(mContext)
                            .setTitle("License has expired")
                            .setMessage("The license for this device has expired. Please renew your license.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                }
            }
        });
        ((MainActivity)getActivity()).setDrawer_locked();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity)getActivity()).setDrawer_unlocked();
    }
    private String XORENC(String input) {
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
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date date = format.parse(dateString);
            if (new Date().after(date)) {
                return false;
            }
            else{
                return true;
            }
        }catch(ParseException ex){
            return false;
        }
    }
}