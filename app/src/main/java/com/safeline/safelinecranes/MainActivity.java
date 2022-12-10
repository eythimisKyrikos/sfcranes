package com.safeline.safelinecranes;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.Rope;
import com.safeline.safelinecranes.models.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DrawerController{

    private AppBarConfiguration mAppBarConfiguration;
    public static NavController navController;
    public static MenuItem loginNavigationText;
    public static TextView navigationHeaderTitle;
    public static TextView navigationHeaderSubtitle;

    public static SharedPreferences sharedPreferences;
    public static String ipAddress;
    public static String port;
    public static String baseUrl = "";
    public static boolean userIsLogged = false;
    public static User loggedUser = new User();

    DrawerLayout drawer;
    Toolbar toolbar;

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;

    // list of NFC technologies detected:
    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        loginNavigationText = menu.findItem(R.id.nav_login);
        View header = navigationView.getHeaderView(0);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ipAddress = sharedPreferences.getString("ip_address", "000.000.000.000");
        port = sharedPreferences.getString("port", "0000");
        if(ipAddress.equals("000.000.000.000") || port.equals("0000")) {
            new AlertDialog.Builder(this)
                    .setTitle("No Settings")
                    .setMessage("There is no configuration for synchronization server. Please go to settings and add your server settings.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.ok, null).show();
        } else {
            baseUrl = "https://" + ipAddress + ":" + port + "/";
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferencesChangeListener);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navigationHeaderTitle = header.findViewById(R.id.nav_header_title);
        navigationHeaderSubtitle = header.findViewById(R.id.nav_header_subtitle);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                /*R.id.nav_login,*/ R.id.nav_positions, R.id.nav_scada, R.id.nav_inspection, R.id.nav_resume, R.id.nav_products,
                R.id.nav_sync_options, R.id.nav_retirement)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        initNfcAdapter();
    }
    private void initNfcAdapter() {
        //Initialise NfcAdapter
        NfcManager nfcManager = (NfcManager) getSystemService(Context.NFC_SERVICE);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //If no NfcAdapter, display that the device has no NFC
        if (nfcAdapter == null){
            Toast.makeText(this,"NO NFC Capabilities",
                    Toast.LENGTH_SHORT).show();
//            finish();
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),PendingIntent.FLAG_MUTABLE);
            }
            else
            {
                pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
            }

            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            try {
                nfcAdapter.disableForegroundDispatch(this);
            } catch (IllegalStateException ex) {
                Log.e("ATHTAG", "Error disabling NFC foreground dispatch", ex);
            }
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Use NFC to read tag
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            Toast.makeText(this,"NFC Tag was found.", Toast.LENGTH_LONG).show();
            MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this);
            Rope rope = dbHelper.getRopeByTagId(bytesToHexString(tag.getId()));
            Position position = dbHelper.getPositionById(rope.getPosition_id());
            ArrayList<Position> positions = new ArrayList<>();
            positions.add(position);
            Bundle args = new Bundle();
            args.putParcelableArrayList("selectedPositions", positions);
            args.putParcelableArrayList("selectedPositions", positions);
            args.putBoolean("nfc", true);
            navController.navigate(R.id.nav_new_inspection_parameters, args);
        } else {
            Toast.makeText(this,"No NFC Tag was found.", Toast.LENGTH_LONG).show();
        }
    }
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        int j = src.length;
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if(!outRect.contains((int)ev.getRawX(), (int)ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public void setDrawer_locked() {
        if(drawer != null) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toolbar.setNavigationIcon(null);
        }
    }
    @Override
    public void setDrawer_unlocked() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        navigationHeaderTitle.setText("Welcome " + loggedUser.getUsername());
        navigationHeaderSubtitle.setText(loggedUser.getVessel());
        navigationHeaderSubtitle.setVisibility(View.VISIBLE);
    }
    SharedPreferences.OnSharedPreferenceChangeListener sharedPreferencesChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            ipAddress = sharedPreferences.getString("ip_address", "000.000.000.000");
            port = sharedPreferences.getString("port", "0000");
            baseUrl = "http://" + ipAddress + ":" + port + "/";
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}