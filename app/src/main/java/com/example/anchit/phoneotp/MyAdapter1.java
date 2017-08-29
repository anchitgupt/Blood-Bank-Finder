package com.example.anchit.phoneotp;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Aayush on 8/12/2017.
 */

class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.ViewHolder1> {

    Context context;
    List<ListItem1> listItem;


    public MyAdapter1(Context context, List<ListItem1> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @Override
    public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem1, parent, false);
        return new ViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder1 holder, int position) {
        final ListItem1 list = listItem.get(position);
        holder.h.setText("Hospi tal");
        holder.a.setText("Address");
        holder.c.setText("City");
        holder.d.setText("District");
        holder.s.setText("State");
        holder.p.setText("Pincode");
        holder.ph.setText("Phone");
        holder.w.setText("Website");

        holder.h1.setText(list.getHospital());
        holder.a1.setText(list.getAddress());
        holder.c1.setText(list.getCity());
        holder.d1.setText(list.getDistrict());
        holder.s1.setText(list.getState());
        holder.p1.setText(list.getPincode());
        holder.ph1.setText(list.getPhone());
        holder.w1.setText(list.getWebsite());


        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent geoIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + list.getHospital() + "+" + list.getDistrict() + "+" + list.getState())); // Prepare intent
                geoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(geoIntent);
            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.getPhone().contentEquals("NA")) {
                    Toast.makeText(context, "Phone Number Not Available", Toast.LENGTH_LONG).show();

                } else {
                    if(list.getPhone().contains(",")){
                        String[] phone = new String[2];
                        phone = list.getPhone().split(",");
                        Log.e("Phone no",phone[0]+ phone[1]);
                        Intent intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:+91" + phone[0]));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        /*Dialog d = new Dialog(context);
                        LayoutInflater layoutInflater = d.getLayoutInflater();
                        View view = layoutInflater.inflate(R.layout.dialog_list,null);

                        ListView l = (ListView) view.findViewById(R.id.dialoglist);
                        d.setTitle("Choose Number:");
                        d.setContentView(view);
                        d.setCancelable(true);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(),R.layout.phone_no,R.id.phone_no,phone);
                        l.setAdapter(arrayAdapter);

                        final String[] finalPhone = phone;
                        Log.e("Phone no", String.valueOf(finalPhone));*/
                        /*l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Intent intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:+91"+ finalPhone[position]));
                                Log.e("Phone no", finalPhone[position]);
                                context.startActivity(intent);
                            }
                        });*/
                        /*d.show()*/;
                    }else
                    {
                        Intent intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:+91" + list.getPhone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            }
        });


        holder.web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.getWebsite().contentEquals("NA")) {

                    Toast.makeText(context, "Phone Number Not Available", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.getWebsite()));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(browserIntent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return listItem.size();
    }


    public class ViewHolder1 extends RecyclerView.ViewHolder {
        public TextView h,a,c,d,s,p,ph,w;
        public TextView h1,a1,c1,d1,s1,p1,ph1,w1;
        public LinearLayout l1,l2,l3,l4,l5,l6,l7,l8,mainL;
        ImageButton call, map, web;

        public ViewHolder1(View itemView) {
            super(itemView);
            h =(TextView) itemView.findViewById(R.id.h_name);
            a =(TextView) itemView.findViewById(R.id.a_name);
            c =(TextView) itemView.findViewById(R.id.c_name);
            d =(TextView) itemView.findViewById(R.id.d_name);
            s =(TextView) itemView.findViewById(R.id.s_name);
            p =(TextView) itemView.findViewById(R.id.p_name);
            ph =(TextView) itemView.findViewById(R.id.ph_name);
            w =(TextView) itemView.findViewById(R.id.w_name);

            h1 =(TextView) itemView.findViewById(R.id.h_name_get);
            a1 =(TextView) itemView.findViewById(R.id.a_name_get);
            c1 =(TextView) itemView.findViewById(R.id.c_name_get);
            d1 =(TextView) itemView.findViewById(R.id.d_name_get);
            s1 =(TextView) itemView.findViewById(R.id.s_name_get);
            p1 =(TextView) itemView.findViewById(R.id.p_name_get);
            ph1 =(TextView) itemView.findViewById(R.id.ph_name_get);
            w1 =(TextView) itemView.findViewById(R.id.w_name_get);

            l1 =(LinearLayout) itemView.findViewById(R.id.h_list);
            l2 =(LinearLayout) itemView.findViewById(R.id.a_list);
            l3 =(LinearLayout) itemView.findViewById(R.id.c_list);
            l4 =(LinearLayout) itemView.findViewById(R.id.d_list);
            l5 =(LinearLayout) itemView.findViewById(R.id.s_list);
            l6 =(LinearLayout) itemView.findViewById(R.id.p_list);
            l7 =(LinearLayout) itemView.findViewById(R.id.ph_list);
            l8 =(LinearLayout) itemView.findViewById(R.id.w_list);
            mainL = (LinearLayout) itemView.findViewById(R.id.main_list);

            map = (ImageButton) itemView.findViewById(R.id.map);
            call = (ImageButton)itemView.findViewById(R.id.call);
            web = (ImageButton)itemView.findViewById(R.id.web);

        }
    }
}