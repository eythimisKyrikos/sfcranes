package com.safeline.safelinecranes.ui.scada;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.gson.GsonBuilder;
import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.FinalResult;
import com.safeline.safelinecranes.models.FinishedResults;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.Rope;
import com.safeline.safelinecranes.models.ScadaPosition;

import java.util.ArrayList;
import java.util.List;

public class VesselScadaFragment extends Fragment {

    private ViewGroup rootLayout;
    private ImageView img;
    private Context mContext;
    List<Position> posList;
    public String type;
    public String action;
    public String position_name;

    public VesselScadaFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static VesselScadaFragment newInstance(String param1, String param2) {
        VesselScadaFragment fragment = new VesselScadaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vessel_scada, container, false);

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK ) {

                    // leave this blank in order to disable the back press

                    return true;
                } else {
                    return false;
                }
            }
        });

        rootLayout = v.findViewById(R.id.view_root);
        img = v.findViewById(R.id.imageView_r);
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        posList = new ArrayList<>();
        posList = dbHelper.getAllPositions();
        List<ScadaPosition> scadaPositions = getSeverityForPositions(posList);

        for(int i=0; i< posList.size(); i++){
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
            ImageView image = new ImageView(mContext);
            layoutParams.leftMargin = posList.get(i).getX();
            layoutParams.topMargin = posList.get(i).getY();
            final int counter = i;
            action = "";
            type = "";
            position_name = posList.get(i).getName();
            image.setImageResource(R.drawable.ic_position_ok);
            for(ScadaPosition scadaPosition : scadaPositions){
                if(posList.get(i).getId() == scadaPosition.getPosition().getId()) {
                    action = scadaPosition.getAction();
                    type = scadaPosition.getType();
                    if(scadaPosition.getSeverity() > 90) image.setImageResource(R.drawable.ic_position_fault);
                    image.setOnTouchListener(new View.OnTouchListener(){
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            // create the popup window
                            LayoutInflater inflaterPopup = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View popupView = inflaterPopup.inflate(R.layout.position_map_popup, null);

                            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            boolean focusable = true; // lets taps outside the popup also dismiss it
                            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                            if(posList.get(counter).getName() != null)
                                ((TextView)popupWindow.getContentView().findViewById(R.id.popup_position)).setText(posList.get(counter).getName());
                            Rope rope = dbHelper.getRopeByPosition(posList.get(counter));
                            if(rope != null)
                                ((TextView)popupWindow.getContentView().findViewById(R.id.popup_rope)).setText(posList.get(counter).getName());
                            if(scadaPosition.getType() != null)
                                ((TextView)popupWindow.getContentView().findViewById(R.id.popup_outer)).setText(rope.getSerialNr());
                            if(scadaPosition.getAction() != null)
                                ((TextView)popupWindow.getContentView().findViewById(R.id.popup_tail)).setText(scadaPosition.getAction());
                            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
//                            popupWindow.setOutsideTouchable(true);
//                            popupWindow.setBackgroundDrawable(new BitmapDrawable());
//                            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//                                @Override
//                                public boolean onTouch(View v, MotionEvent event) {
//                                    if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                                        popupWindow.dismiss();
//                                    }
//                                    return false;
//                                }
//                            });
                            popupView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    popupWindow.dismiss();
                                    return true;
                                }
                            });
                            rootLayout.invalidate();
                            return true;
                        }
                    });
                }
            }
            if(!(layoutParams.leftMargin == 0 && layoutParams.rightMargin == 0)) {
                image.setLayoutParams(layoutParams);
                rootLayout. addView(image);
            }
        }

        return v;
    }

    private List<ScadaPosition> getSeverityForPositions(List<Position> positions) {
        List<ScadaPosition> scadaPositions = new ArrayList<>();
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        for(int i=0; i<positions.size(); i++) {
            FinishedResults lastResults = dbHelper.getLastFinishedResultForPosition(positions.get(i));
            if(lastResults.getResults() != null) {
                int severity_level = 0;
                FinalResult finalResult = new GsonBuilder().create().fromJson(lastResults.getResults(), FinalResult.class);
                Position position = dbHelper.getPositionById(finalResult.getRope().getPosition_id());
                Rope rope = finalResult.getRope();
                List<FinalResult.Action> actions =finalResult.getActions();
                for(int j=0; j<actions.size(); j++) {
                    if (severity_level < actions.get(j).getSeverity_level()){
                        severity_level = actions.get(j).getSeverity_level();
                        action = actions.get(j).getAction();
                        type = "Rope " + rope.getSerialNr();
                    }
                }

                ScadaPosition scadaPosition = new ScadaPosition();
                scadaPosition.setPosition(position);
                scadaPosition.setType("Rope");
                scadaPosition.setAction(action);
                scadaPosition.setSeverity(severity_level);
                scadaPositions.add(scadaPosition);
            }
        }
        return scadaPositions;
    }
}