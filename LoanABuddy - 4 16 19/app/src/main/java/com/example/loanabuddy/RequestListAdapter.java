package com.example.loanabuddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RequestListAdapter extends BaseAdapter implements ListAdapter
{
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> docList = new ArrayList<String>();
    private Context context;

    public RequestListAdapter(ArrayList<String> list, ArrayList<String> docList, Context context) {
        this.list = list;
        this.docList = docList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.request_item, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button btn= (Button)view.findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, View_Request.class);
                intent.putExtra("com.example.loanabuddy.Req", docList.get(position));
                context.startActivity(intent);
            }
        });


        return view;
    }




}
