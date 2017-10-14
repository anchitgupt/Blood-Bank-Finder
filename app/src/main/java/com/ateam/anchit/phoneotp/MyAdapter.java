package com.ateam.anchit.phoneotp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alok Kumar on 05-Aug-17.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    Context context;
    List<ListItem> listItem;

    public MyAdapter(Context context, List<ListItem> listItem) {
        this.context = context;
        this.listItem = listItem;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitems, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItem list = listItem.get(position);
        holder.t1.setText(list.getName());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Main.class);
                intent.putExtra("value", list.getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("value", list.getName());
                context.startActivity(intent);
                //Toast.makeText(context, "you selected "+ list.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

  /*  @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = listItem;
                } else {

                    ArrayList<ListItem> filteredList = new ArrayList<>();

                    for (ListItem androidVersion : listItem) {

                        if (androidVersion.getName().toLowerCase().contains(charString) || androidVersion.getName().toLowerCase().contains(charString) || androidVersion.getName().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<ListItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView t1;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            t1 = (TextView) itemView.findViewById(R.id.tv);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.lineaqrLayout);
        }
    }
}
