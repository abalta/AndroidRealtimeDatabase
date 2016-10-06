package com.abdullahbalta.androidrealtimedatabase.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdullahbalta.androidrealtimedatabase.R;
import com.abdullahbalta.androidrealtimedatabase.fragment.AddNewEntryFragment;
import com.abdullahbalta.androidrealtimedatabase.model.InventorModel;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Abdullah on 2.10.2016.
 */

public class InventorAdapter extends RecyclerView.Adapter<InventorAdapter.InventorViewHolder> {

    private Context mContext;
    private List<InventorModel> inventorModels;

    public InventorAdapter(Context mContext, List<InventorModel> inventorModels) {
        this.mContext = mContext;
        this.inventorModels = inventorModels;
    }

    @Override
    public InventorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventor_card_layout, parent, false);

        return new InventorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InventorViewHolder holder, final int position) {
        InventorModel inventor = inventorModels.get(position);
        holder.title.setText(inventor.getInventor_name());
        holder.profession.setText(inventor.join(inventor.getProfession(), ", "));
        // loading cover using Glide library
        Glide.with(mContext).load(inventor.getImage()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, position);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.inventor_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    private void deleteEntry(int position){
        FirebaseDatabase.getInstance().getReference().child("inventors").child(inventorModels.get(position).getInventor_id() + "").removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(mContext, "Kart Başarıyla Kaldırıldı.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;

        public MyMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit:
                    editEntry(position);
                    return true;
                case R.id.action_delete:
                    deleteEntry(position);
                    return true;
                default:
            }
            return false;
        }
    }

    private void editEntry(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("INVENTOR", inventorModels.get(position));
        AddNewEntryFragment addNewEntryFragment = new AddNewEntryFragment();
        addNewEntryFragment.setArguments(bundle);
        ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.scale_small_to_big, R.anim.scale_small_to_big, R.anim.scale_big_to_small, R.anim.scale_big_to_small).add(android.R.id.content, addNewEntryFragment, "ADD").addToBackStack("ADD").commit();
    }

    @Override
    public int getItemCount() {
        return inventorModels.size();
    }

    public class InventorViewHolder extends RecyclerView.ViewHolder {
        public TextView title, profession;
        public ImageView thumbnail, overflow;

        public InventorViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            profession = (TextView) view.findViewById(R.id.profession);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }
}
