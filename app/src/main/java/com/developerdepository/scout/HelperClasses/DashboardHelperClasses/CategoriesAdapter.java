package com.developerdepository.scout.HelperClasses.DashboardHelperClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.developerdepository.scout.R;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    ArrayList<CategoriesModel> categories;

    public CategoriesAdapter(ArrayList<CategoriesModel> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_categories_recycler, parent, false);
        CategoriesViewHolder categoriesViewHolder = new CategoriesViewHolder(view);
        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        CategoriesModel categoriesModel = categories.get(position);

        holder.categoriesBackground.setBackgroundResource(categoriesModel.getBackground());
        holder.categoriesTitle.setText(categoriesModel.getTitle());
        holder.categoriesImg.setImageResource(categoriesModel.getImage());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout categoriesItem, categoriesBackground;
        ImageView categoriesImg;
        TextView categoriesTitle;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            categoriesItem = itemView.findViewById(R.id.categories_item);
            categoriesBackground = itemView.findViewById(R.id.categories_background);
            categoriesImg = itemView.findViewById(R.id.categories_img);
            categoriesTitle = itemView.findViewById(R.id.categories_title);
        }
    }
}
