package com.example.tomcat.bakingapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tomcat.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for Recipes on Main View
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private ArrayList<Recipe> recipes;
    private final ReciepeAdapterOnClickHandler mClickHandler;

    MainAdapter(ArrayList<Recipe> recipes, ReciepeAdapterOnClickHandler clickHandler) {
        this.recipes = recipes;
        this.mClickHandler = clickHandler;
    }

    public interface ReciepeAdapterOnClickHandler {
        void onClick(int position);
    }


    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_reciepe_row, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private int id;
        @BindView(R.id.tv_name) TextView tv_name;
        @BindView(R.id.cake_photo) ImageView cakeView;

        ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        holder.tv_name.setText(recipes.get(position).getName());
        holder.id = recipes.get(position).getId();

        String imageUrl = recipes.get(position).getImage();
        if (imageUrl.isEmpty()){
            switch (holder.id) {

                case 1: // ------------------------------------------------------------------------- Nutella Pie
                    Picasso.with(context).load(R.drawable.nutella_pie)
                            .fit().centerCrop().into(holder.cakeView);
                    break;
                case 2: // ------------------------------------------------------------------------- Brownies
                    Picasso.with(context).load(R.drawable.brownies)
                            .fit().centerCrop().into(holder.cakeView);
                    break;
                case 3: // ------------------------------------------------------------------------- Yellow Cake
                    Picasso.with(context).load(R.drawable.yellow_cake)
                            .fit().centerCrop().into(holder.cakeView);
                    break;
                case 4: //-------------------------------------------------------------------------- Cheesecake
                    Picasso.with(context).load(R.drawable.cheescake_1)
                            .fit().centerCrop().into(holder.cakeView);
                    break;
            }
        } else {
            Picasso.with(context).load(imageUrl).fit().into(holder.cakeView);
        }

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
