package com.example.team9.treasurehunt;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mantas
 */

public class ClueAdapter extends ArrayAdapter<String> {

    private int FIRST_CLUE_TEXT_COLOR = Color.BLACK;

    ClueAdapter(Context context, int resource, List<String> treasures) {
        super(context, resource, treasures);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        String text = getItem(position);

        if (view == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.cluelist_item, null);
        }

       // clue container
        TextView container = (TextView) view.findViewById(R.id.list_clue_item);

        if (container != null) {
            container.setText(text); // show clue
        }

        if(position == 0 && container != null){  // set 1st clue color
            container.setTextColor(FIRST_CLUE_TEXT_COLOR);
        }
        return view;
    }
}
