package com.example.tomcat.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.tomcat.bakingapp.R;
import com.example.tomcat.bakingapp.models.Ingredient;
import com.example.tomcat.bakingapp.IngriedentsAdapter;

/**
 * Ingriedent List in Widget
 */

public class ListWidgetService extends RemoteViewsService {

    Ingredient[] ingriedentsWidget;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    // ********************************************************************************************* RemoteViewFactory
    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mContext;

        ListRemoteViewsFactory(Context applicationContext) {
            mContext = applicationContext;
            ingriedentsWidget = IngredientsWidget.mIngriedents;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            ingriedentsWidget = IngredientsWidget.mIngriedents;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (ingriedentsWidget == null) return 0;
            return ingriedentsWidget.length;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(),
                    R.layout.ingriedents_widget_row);

            // ------------------------------------------------------------------------------------- Ingriedent
            views.setTextViewText(R.id.text_view_recipe_widget, ingriedentsWidget[i].getIngredient());

            // ------------------------------------------------------------------------------------- Measure
            String ingriedent = ingriedentsWidget[i].getMeasure();
            double quantity = ingriedentsWidget[i].getQuantity();
            String currentquantity = IngriedentsAdapter.doubleFormat(quantity) + " "
                    + IngriedentsAdapter.getPluralMeasure(ingriedent, quantity, mContext);
            views.setTextViewText(R.id.tv_quantity_widget, currentquantity);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
