package com.safeline.safelinecranes.ui.register;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.MigrationDbHelper;

import static com.safeline.safelinecranes.MainActivity.ipAddress;
import static com.safeline.safelinecranes.MainActivity.port;

public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    private Context mContext;
    private TextView usernameTxt;
    private Button registerBtn;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.register_fragment, container, false);

        Button gotoLoginBtn = (Button) root.findViewById(R.id.btn_go_to_login);
        gotoLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.action_nav_register_to_nav_login);
            }
        });
        registerBtn = (Button) root.findViewById(R.id.btn_register);
        usernameTxt = (TextView)root.findViewById(R.id.register_username);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ipAddress.equals("000.000.000.000") || port.equals("0000")) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Wrong Settings")
                            .setMessage("There is no configuration for synchronization server. Please go to settings and add your server settings.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                    return;
                }
                if(usernameTxt.getText().toString().trim().equals("")) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Register User")
                            .setMessage("You must fill all fields in form.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                } else {
                    mViewModel.getVerificationCert(usernameTxt.getText().toString()).observe(getViewLifecycleOwner(), user -> {
                        switch (user.status) {
                            case SUCCESS:
                                MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
                                dbHelper.deleteUsers();
                                user.data.setPassword(user.data.getPassword());
                                dbHelper.addUser(user.data);
                                Toast.makeText(mContext,"User registered successfully", Toast.LENGTH_SHORT).show();
//                                Navigation.findNavController(root).navigate(R.id.action_nav_register_to_nav_login);
                                break;
                            case LOADING:
                                break;
                            case ERROR:
                                Toast.makeText(mContext,user.message, Toast.LENGTH_LONG).show();
                                break;
                        }
                        Navigation.findNavController(root).navigate(R.id.action_nav_register_to_nav_login);
                    });
                }
            }
        });
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
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

}