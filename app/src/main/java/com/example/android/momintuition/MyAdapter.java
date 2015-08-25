package com.example.android.momintuition;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    final View.OnClickListener optionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//String s = v.findViewById(R.id.title).toString();
            Log.i("this view was clicked", "GET THE ID ");
        }
    };
    private String[] mDataset;


    public MyAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.title.setText(mDataset[position]);
        holder.activity.setText(mDataset[1]);
        if (position % 2 == 0) {
            holder.img.setCropToPadding(true);
            holder.img.setImageResource(R.drawable.bear);

        }
        holder.itemView.setOnClickListener(optionListener);


    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spot, parent, false);

        return new MyAdapter.ViewHolder(v);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }


    // Provide a suitable constructor (depends on the kind of dataset)


    // Create new views (invoked by the layout manager)

    // Replace the contents of a view (invoked by the layout manager)

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView activity;
        public ImageView img;

        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);
            activity = (TextView) v.findViewById(R.id.activity);
            img = (ImageView) v.findViewById(R.id.image);

        }
    }
}