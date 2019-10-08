package edu.byu.cs240.FamilyMap.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import edu.byu.cs240.FamilyMap.R;
import edu.byu.cs240.FamilyMap.model.Model;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private final List<String> filterTypes;
    private final LayoutInflater filterAdapterInflater;

    FilterAdapter(Context context, List<String> filterTypes) {
        this.filterAdapterInflater = LayoutInflater.from(context);
        this.filterTypes = filterTypes;
    }

    // Inflate row layout from XML
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = filterAdapterInflater.inflate(R.layout.filter_row, parent, false);
        return new ViewHolder(view);
    }

    // Bind the data to each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        final String rowFilterType = filterTypes.get(pos);
        holder.top_row.setText(capitalize(rowFilterType) + " Events");
        holder.bottom_row.setText("SHOW " + rowFilterType.toUpperCase() + " EVENTS");

        // Handle whether the switch should be checked or not
        holder.filterSwitch.setChecked(filterIsOn(rowFilterType));

        // Handle changes to switches
        holder.filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleFilter(rowFilterType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterTypes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView top_row;
        final TextView bottom_row;
        final Switch filterSwitch;

        ViewHolder(View itemView) {
            super(itemView);
            top_row = itemView.findViewById(R.id.filterRowTopRow);
            bottom_row = itemView.findViewById(R.id.filterRowBottomRow);
            filterSwitch = itemView.findViewById(R.id.filterRowSwitch);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // We don't want anything to happen on user click
        }
    }

    // Toggle filter selected by user
    private void toggleFilter(String filterType) {
        switch (filterType) {
            case "Male":
                Model.getInstance().setShowMale(!Model.getInstance().isShowMale());
                break;
            case "Female":
                Model.getInstance().setShowFemale(!Model.getInstance().isShowFemale());
                break;
            case "Father's Side":
                Model.getInstance().setShowFathersSide(!Model.getInstance().isShowFathersSide());
                break;
            case "Mother's Side":
                Model.getInstance().setShowMothersSide(!Model.getInstance().isShowMothersSide());
                break;
            default:
                if (Model.getInstance().getFilteredEventTypes().contains(filterType)) {
                    Model.getInstance().removeFilteredEventType(filterType);
                } else {
                    Model.getInstance().addFilteredEventType(filterType);
                }
                break;
        }
    }

    // Check if a given filter is on
    private boolean filterIsOn(String filterType) {
        switch (filterType) {
            case "Male":
                return Model.getInstance().isShowMale();
            case "Female":
                return Model.getInstance().isShowFemale();
            case "Father's Side":
                return Model.getInstance().isShowFathersSide();
            case "Mother's Side":
                return Model.getInstance().isShowMothersSide();
            default:
                return !Model.getInstance().getFilteredEventTypes().contains(filterType);
        }
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
