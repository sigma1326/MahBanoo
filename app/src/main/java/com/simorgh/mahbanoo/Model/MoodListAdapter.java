package com.simorgh.mahbanoo.Model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.simorgh.mahbanoo.R;
import com.simorgh.moodview.MoodView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class MoodListAdapter extends ListAdapter<MoodItem, MoodListAdapter.ViewHolder> {
    public MoodListAdapter(@NonNull ItemDiffCallBack diffCallback) {
        super(diffCallback);
    }

    protected MoodListAdapter(@NonNull AsyncDifferConfig<MoodItem> config) {
        super(config);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_mood, parent, false);
        return new ViewHolder(v, null);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.moodItem = getItem(position);
        ImageView imageView = (ImageView) holder.itemView;
        if (holder.moodItem != null) {
            switch (holder.moodItem.getMoodType()) {
                case MoodView.TYPE_BLEEDING:
                    switch (holder.moodItem.getMoodSelectedIndex()) {
                        case 0:
                            imageView.setImageResource(R.drawable.item_bleeding1);
                            break;
                        case 1:
                            imageView.setImageResource(R.drawable.item_bleeding2);
                            break;
                        case 2:
                            imageView.setImageResource(R.drawable.item_bleeding3);
                            break;
                        case 3:
                            imageView.setImageResource(R.drawable.item_bleeding4);
                            break;
                    }
                    break;
                case MoodView.TYPE_EMOTION:
                    switch (holder.moodItem.getMoodSelectedIndex()) {
                        case 0:
                            imageView.setImageResource(R.drawable.item_emotion1);
                            break;
                        case 1:
                            imageView.setImageResource(R.drawable.item_emotion2);
                            break;
                        case 2:
                            imageView.setImageResource(R.drawable.item_emotion3);
                            break;
                        case 3:
                            imageView.setImageResource(R.drawable.item_emotion4);
                            break;
                    }
                    break;
                case MoodView.TYPE_PAIN:
                    switch (holder.moodItem.getMoodSelectedIndex()) {
                        case 0:
                            imageView.setImageResource(R.drawable.item_pain1);
                            break;
                        case 1:
                            imageView.setImageResource(R.drawable.item_pain2);
                            break;
                        case 2:
                            imageView.setImageResource(R.drawable.item_pain3);
                            break;
                        case 3:
                            imageView.setImageResource(R.drawable.item_pain4);
                            break;
                    }
                    break;
                case MoodView.TYPE_EATING_DESIRE:
                    switch (holder.moodItem.getMoodSelectedIndex()) {
                        case 0:
                            imageView.setImageResource(R.drawable.item_eating_desire1);
                            break;
                        case 1:
                            imageView.setImageResource(R.drawable.item_eating_desire2);
                            break;
                        case 2:
                            imageView.setImageResource(R.drawable.item_eating_desire3);
                            break;
                        case 3:
                            imageView.setImageResource(R.drawable.item_eating_desire4);
                            break;
                    }
                    break;
                case MoodView.TYPE_HAIR_STYLE:
                    switch (holder.moodItem.getMoodSelectedIndex()) {
                        case 0:
                            imageView.setImageResource(R.drawable.item_hair_style1);
                            break;
                        case 1:
                            imageView.setImageResource(R.drawable.item_hair_style2);
                            break;
                        case 2:
                            imageView.setImageResource(R.drawable.item_hair_style3);
                            break;
                        case 3:
                            imageView.setImageResource(R.drawable.item_hair_style4);
                            break;
                    }
                    break;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MoodItem moodItem;

        public ViewHolder(@NonNull View itemView, MoodItem moodItem) {
            super(itemView);
            this.moodItem = moodItem;
        }
    }

    public static class ItemDiffCallBack extends DiffUtil.ItemCallback<MoodItem> {

        @Override
        public boolean areItemsTheSame(@NonNull MoodItem oldItem, @NonNull MoodItem newItem) {
            return oldItem.getMoodType() == newItem.getMoodType() && oldItem.getMoodSelectedIndex() == newItem.getMoodSelectedIndex();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MoodItem oldItem, @NonNull MoodItem newItem) {
            return oldItem.getMoodType() == newItem.getMoodType() && oldItem.getMoodSelectedIndex() == newItem.getMoodSelectedIndex()
                    && oldItem.getId().getTimeInMillis() == newItem.getId().getTimeInMillis();
        }
    }
}
