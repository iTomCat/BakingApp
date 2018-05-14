package com.example.tomcat.bakingapp;

import android.content.Context;
import android.graphics.Color;
import  android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tomcat.bakingapp.models.Steps;
import com.example.tomcat.bakingapp.utilitis.Tools;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for Ingriedents and Recipes Steps
 */

public class StepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Steps[] mSteps;
    private StepsAdapterOnClickHandler mCallback;
    private static final int INGRIEDENTS_ITEM = 1;
    private static final int PREP_STEPS_ITEM = 2;
    private Context mContext;


    StepsAdapter(Steps[] steps, Context context) {
        this.mSteps = steps;
        this.mContext = context;

        try {
            mCallback = (StepsAdapterOnClickHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    public interface StepsAdapterOnClickHandler {
        void onClick(int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == INGRIEDENTS_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingriedents_row,
                    parent, false);
            return new IngriedentsViewHolder(view);
        } else if (viewType == PREP_STEPS_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_row,
                    parent, false);
            return new StepsViewHolder(view);
        } else {
            throw new RuntimeException("The type has to be INGRIEDENTS_ITEM or PREP_STEPS_ITEM");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return INGRIEDENTS_ITEM;
        } else {
            return PREP_STEPS_ITEM;
        }
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.step_txt) TextView tv_text;
        @BindView(R.id.step_number) TextView tv_stepNumber;

        StepsViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCallback.onClick(adapterPosition);
        }
    }

    public class IngriedentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.apla_backgr) ImageView ingriedentsBackgr;

       IngriedentsViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCallback.onClick(adapterPosition);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case INGRIEDENTS_ITEM:
                initLayoutIngriedents((IngriedentsViewHolder)holder);
                break;
            case PREP_STEPS_ITEM:
                initLayoutSteps((StepsViewHolder) holder, position);
                break;
            default:
                break;
        }
    }

    private void initLayoutIngriedents(IngriedentsViewHolder holder) {
        Picasso.with(mContext)
                .load(R.drawable.reciepe_backgr)
                .fit()
                .centerCrop()
                .into(holder.ingriedentsBackgr);

    }

    private void initLayoutSteps(StepsViewHolder holder, int position) {
        int stepsPos = position - 1;
        String actStep = mSteps[stepsPos].getShortDescription();
        holder.tv_text.setText(actStep);
        String stepNumber = Integer.toString(stepsPos);
        holder.tv_stepNumber.setText(stepNumber);

        if (stepsPos == 0){
            //holder.tv_stepNumber.setVisibility(View.INVISIBLE);
            holder.tv_stepNumber.setText(mContext.getResources().getString(R.string.intro));
        }

        // ----------------------------------------------------------------------------------------- Highlighting pos on list
        if (Tools.tableteMode(mContext)){
            if (position == RecipeActivity.currentStep + 1){
                holder.itemView.setBackgroundColor
                        (mContext.getResources().getColor(R.color.colorPrimary_light));
            }else{
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mSteps.length + 1;
    }
}
