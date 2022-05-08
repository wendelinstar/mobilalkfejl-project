package com.example.vizora;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsThingAdapter extends RecyclerView.Adapter<NewsThingAdapter.ViewHolder> implements Filterable {
    private ArrayList<NewsThing> mNewsThingsData;
    private ArrayList<NewsThing> mNewsThingsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    NewsThingAdapter(Context context, ArrayList<NewsThing> thingsData){
        this.mNewsThingsData = thingsData;
        this.mNewsThingsDataAll = thingsData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsThingAdapter.ViewHolder holder, int position) {
        NewsThing aktualisHir = mNewsThingsData.get(position);

        holder.bindTo(aktualisHir);

        if(holder.getAbsoluteAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAbsoluteAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {return mNewsThingsData.size();}

    @Override
    public Filter getFilter() {
        return newsFilter;
    }
    private Filter newsFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<NewsThing> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null ||charSequence.length() == 0) {
                results.count = mNewsThingsDataAll.size();
                results.values = mNewsThingsDataAll;
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(NewsThing news : mNewsThingsDataAll) {
                    if(news.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(news);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mNewsThingsData = (ArrayList<NewsThing>) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleText;
        private TextView mDateText;
        private TextView mInfoText;

        public ViewHolder(View itemView) {
            super(itemView);

            mTitleText = itemView.findViewById(R.id.newsTitleText);
            mDateText = itemView.findViewById(R.id.newsDateText);
            mInfoText = itemView.findViewById(R.id.newsInfoText);
        }

        public void bindTo(NewsThing aktualisHir) {
            mTitleText.setText(aktualisHir.getName());
            mDateText.setText(aktualisHir.getDate());
            mInfoText.setText(aktualisHir.getInfo());


        }
    };
};


