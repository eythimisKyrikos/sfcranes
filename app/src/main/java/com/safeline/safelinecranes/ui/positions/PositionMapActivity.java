package com.safeline.safelinecranes.ui.positions;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.Position;

import java.util.ArrayList;
import java.util.List;

public class PositionMapActivity extends AppCompatActivity {

    private ImageView img;
    private Position position;
    private boolean editMode;
    private ViewGroup rootLayout;
    private int _xDelta;
    private int _yDelta;
    private int leftMargin;
    private int topMargin;
    FloatingActionButton fabSave;
    List<Position> posList;
    Intent meIintent;
    Position editedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_map);

        meIintent = getIntent();
        setTitle("Mooring Plan");
        editMode = meIintent.getBooleanExtra("editMode", false);
        position = meIintent.getParcelableExtra("Position");

        rootLayout = findViewById(R.id.view_root);
        img = findViewById(R.id.imageView_r);
        fabSave = findViewById(R.id.save_position);

        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this);
        posList = new ArrayList<>();
        posList = dbHelper.getAllPositions();

        if(editMode) {
            for(int i=0; i< posList.size(); i++){
                if(posList.get(i).getId() != position.getId()){
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
                    ImageView image = new ImageView(this);
                    layoutParams.leftMargin = posList.get(i).getX();
                    layoutParams.topMargin = posList.get(i).getY();
                    image.setImageResource(R.drawable.ic_position_disabled);
                    image.setLayoutParams(layoutParams);
                    rootLayout.addView(image);
                }
            }
            editedPosition = findPosition(position, posList);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
            ImageView image = new ImageView(this);
            layoutParams.leftMargin = editedPosition.getX();
            layoutParams.topMargin = editedPosition.getY();
            image.setImageResource(R.drawable.ic_position_adjust);
            image.setLayoutParams(layoutParams);
            image.setOnTouchListener(new ChoiceTouchListener(this.position.getName()));
            img = image;
            rootLayout.addView(image);
            leftMargin = editedPosition.getX();
            topMargin = editedPosition.getY();
        } else {
            for(int i=0; i< posList.size(); i++){
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
                ImageView image = new ImageView(this);
                layoutParams.leftMargin = posList.get(i).getX();
                layoutParams.topMargin = posList.get(i).getY();
                image.setImageResource(R.drawable.ic_position_disabled);
                image.setLayoutParams(layoutParams);
                rootLayout. addView(image);
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
            layoutParams.leftMargin = position.getX();
            layoutParams.topMargin = position.getY();
            ImageView image = new ImageView(this);
            image.setImageResource(R.drawable.ic_position_adjust);
            image.setLayoutParams(layoutParams);
            image.setOnTouchListener(new ChoiceTouchListener(null));
            img = image;
            rootLayout.addView(image);
            leftMargin = position.getX();
            topMargin = position.getY();
        }

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resIntent = new Intent();
                resIntent.putExtra("x", leftMargin);
                resIntent.putExtra("y", topMargin);
                setResult(RESULT_OK, resIntent);
                finish();
            }
        });
    }

    private final class ChoiceTouchListener implements View.OnTouchListener {
        String position;
        String tail;

        public ChoiceTouchListener(String position) {
            this.position = position;
        }
        public boolean onTouch(View view, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = -500;
                    layoutParams.bottomMargin = -500;
                    leftMargin = X - _xDelta;
                    topMargin = Y - _yDelta;
                    view.setLayoutParams(layoutParams);
                    break;
            }
            rootLayout.invalidate();
            return true;
        }
    }
    private Position findPosition(Position position, List<Position> list) {
        for(Position p : list) {
            if(p.getId() == position.getId()){
                return p;
            }
        }
        return null;
    }
}