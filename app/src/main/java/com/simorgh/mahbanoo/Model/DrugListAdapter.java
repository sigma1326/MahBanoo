package com.simorgh.mahbanoo.Model;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simorgh.databaseutils.CycleRepository;
import com.simorgh.databaseutils.model.DayMood;
import com.simorgh.mahbanoo.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class DrugListAdapter extends ListAdapter<DrugItem, DrugListAdapter.ViewHolder> {
    private CycleRepository cycleRepository;

    public DrugListAdapter(@NonNull DrugDiffCallBack diffCallback) {
        super(diffCallback);
    }

    protected DrugListAdapter(@NonNull AsyncDifferConfig<DrugItem> config) {
        super(config);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (cycleRepository == null) {
            cycleRepository = new CycleRepository((Application) parent.getContext().getApplicationContext());
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_item, parent, false);
        return new ViewHolder(v, null);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.drugItem = getItem(position);
        ((TextView) holder.itemView.findViewById(R.id.tv_drug_name)).setText(holder.drugItem.getDrugName());

        if (getItem(position) != null && getItem(position).getDrugName() != null) {
            holder.drugItem.setDrugName(getItem(position).getDrugName());
            holder.drugItem.setId(getItem(position).getId());
            holder.imgRemove.setOnClickListener(v -> {


                DayMood dayMood = cycleRepository.getDayMood(holder.drugItem.getId());
                if (dayMood != null) {
                    int index = -1;
                    for (int i = 0; i < dayMood.getDrugs().size(); i++) {
                        if (i < dayMood.getDrugs().size() && dayMood.getDrugs().get(i).equals(holder.drugItem.getDrugName())) {
                            index = i;
                            break;
                        }
                    }
                    if (index != -1) {
                        dayMood.getDrugs().remove(index);
                        cycleRepository.insertDayMood(dayMood);
                        notifyItemRemoved(index);
                    }
                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private DrugItem drugItem;
        private ImageView imgRemove;

        public ViewHolder(@NonNull View itemView, DrugItem drugItem) {
            super(itemView);
            this.drugItem = drugItem;
            this.imgRemove = itemView.findViewById(R.id.img_remove);
        }
    }

    public static class DrugDiffCallBack extends DiffUtil.ItemCallback<DrugItem> {

        @Override
        public boolean areItemsTheSame(@NonNull DrugItem oldItem, @NonNull DrugItem newItem) {
            return oldItem.getDrugName().equals(newItem.getDrugName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DrugItem oldItem, @NonNull DrugItem newItem) {
            return oldItem.getDrugName().equals(newItem.getDrugName());
        }
    }
}
