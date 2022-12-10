package com.safeline.safelinecranes.ui.inspection;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.models.Answer;

import java.util.HashMap;
import java.util.List;

public class InspectionExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Answer> listDataHeader;
    private HashMap<Answer, Answer> listHashMap;

    public InspectionExpandableListAdapter(Context context, List<Answer> listDataHeader, HashMap<Answer, Answer> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }
    @Override
    public int getChildrenCount(int i) {
        return 1;
    }
    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }
    @Override
    public Object getChild(int i, int i1) {
        return null;
    }
    @Override
    public long getGroupId(int i) {
        return 0;
    }
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        final Answer answer = (Answer)getGroup(groupPosition);
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.inspection_list_header , null);
        }
        TextView header = (TextView) view.findViewById(R.id.answer_header);
        header.setText(answer.getId() + ". " + answer.getHeader());
        header.setTypeface(null, Typeface.BOLD);
        TextView details = (TextView) view.findViewById(R.id.answer_details);
        details.setText(answer.getDetails());
        details.setTypeface(null, Typeface.BOLD);
        if (answer.getValue() == 5) {
            view.setBackgroundColor(0xFF00CC08);
        } else if (answer.getValue() == 4) {
            view.setBackgroundColor(0xFF8FCC00);
        } else if (answer.getValue() == 3) {
            view.setBackgroundColor(0xFFECE801);
        } else if (answer.getValue() == 2) {
            view.setBackgroundColor(0xFFEC8201);
        } else if (answer.getValue() == 1) {
            view.setBackgroundColor(0xFFEC0707);
        }

        ImageButton selectAnswer = (ImageButton) view.findViewById(R.id.btn_product_select_answer);
        selectAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView details = (TextView) view.findViewById(R.id.answer_details);
                OnTextClick(String.valueOf(answer.getId()));
            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExpanded) ((ExpandableListView) viewGroup).collapseGroup(groupPosition);
                else ((ExpandableListView) viewGroup).expandGroup(groupPosition, true);
            }
        });
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExpanded) ((ExpandableListView) viewGroup).collapseGroup(groupPosition);
                else ((ExpandableListView) viewGroup).expandGroup(groupPosition, true);
            }
        });
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final Answer answer = listDataHeader.get(i);
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.inspection_list_item, null);
        }
        if(answer.getImage1() == null){
            ImageButton image1 = (ImageButton) view.findViewById(R.id.imageView1);
        } else {
            ImageButton image1 = (ImageButton) view.findViewById(R.id.imageView1);
            int resource = context.getResources().getIdentifier(answer.getImage1(), "drawable", context.getPackageName());
            image1.setImageResource(resource);

            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageView image = (ImageView) view.findViewById(R.id.imageView1);
                    Resources res = context.getResources();
                    int resource = context.getResources().getIdentifier(answer.getImage1(), "drawable", context.getPackageName());
                    zoomImageFromThumb(image, resource);
                }
            });
        }
        return view;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    public void OnTextClick(String id){
    }
    public void zoomImageFromThumb(final View thumbView, int imageResId) {
    }
}
