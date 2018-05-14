package com.example.tomcat.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment with Ingriedents Button and preparation steps
 */

public class StepsFragment extends Fragment {
    RecipeActivity recipeActivity;
    StepsAdapter adapter;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.reciepe_fragment_master, container, false);
        recipeActivity = (RecipeActivity) getActivity();
        unbinder = ButterKnife.bind(this, rootView);
        initView(rootView);

        return rootView;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // ********************************************************************************************* init RecyclerView
    private void initView(View view){
        RecyclerView.LayoutManager layoutManager;
        RecyclerView recyclerView = view.findViewById(R.id.steps_card);
        adapter = new StepsAdapter(recipeActivity.getSteps(), getContext());

        layoutManager = new LinearLayoutManager
                (getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    // ********************************************************************************************* Step highlight
    public void highlightStep(){
        adapter.notifyDataSetChanged();
    }
}
