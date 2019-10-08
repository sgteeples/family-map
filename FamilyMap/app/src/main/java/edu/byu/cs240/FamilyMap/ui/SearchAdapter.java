package edu.byu.cs240.FamilyMap.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.byu.cs240.FamilyMap.R;
import edu.byu.cs240.FamilyMap.model.Event;
import edu.byu.cs240.FamilyMap.model.Model;
import edu.byu.cs240.FamilyMap.model.Person;
import edu.byu.cs240.FamilyMap.utils.Serializer;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final List<Person> personData;
    private final List<Event> eventData;
    private final LayoutInflater inflater;
    private final Context context;

    SearchAdapter(Context context, List<Person> personData, List<Event> eventData) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.personData = personData;
        this.eventData = eventData;
    }

    // Inflate the row layout from XML when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.search_row, parent, false);
        return new ViewHolder(view);
    }

    // Bind the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (positionRowIsPerson(position)) {
            setUpViewHolderWithPerson(holder, position);
        } else {
            setUpViewHolderWithEvent(holder, position);
        }
    }

    private boolean positionRowIsPerson(int position) {
        return position < personData.size();
    }

    private void setUpViewHolderWithPerson(ViewHolder holder, int position) {
        Person p = personData.get(position);
        holder.top_row.setText(p.getFirstName() + " " + p.getLastName());
        holder.bottom_row.setText("");
        if (p.getGender().equals("m")) {
            holder.row_image.setImageResource(R.drawable.male_icon);
        } else {
            holder.row_image.setImageResource(R.drawable.female_icon);
        }
    }

    private void setUpViewHolderWithEvent(ViewHolder holder, int position) {
        Event e = eventData.get(position - personData.size());
        holder.top_row.setText(e.getEventType() + ": " + e.getCity() + ", " +
                e.getCountry() + " (" + e.getYear() + ")");
        Person associated_person = Model.getInstance().getPersonByPersonID(e.getPersonID());
        holder.bottom_row.setText(associated_person.getFirstName() + " " +
                associated_person.getLastName());
        holder.row_image.setImageResource(R.drawable.marker);
    }

    @Override
    public int getItemCount() {
        return personData.size() + eventData.size();
    }

    // Store and recycle views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView top_row;
        final TextView bottom_row;
        final ImageView row_image;

        ViewHolder(View itemView) {
            super(itemView);
            top_row = itemView.findViewById(R.id.searchRowTop);
            bottom_row = itemView.findViewById(R.id.searchRowBottom);
            row_image = itemView.findViewById(R.id.searchRowImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();

            if (positionRowIsPerson(pos)) {
                // Start a new person activity
                Model.getInstance().setCurrentlySelectedPersonID(personData.get(pos).getPersonID());
                Intent i = new Intent(context, PersonActivity.class);
                i.putExtra("activityPerson", new Serializer().serialize(personData.get(pos)));
                context.startActivity(i);
            } else {
                // Start a new event activity
                Intent i = new Intent(context, EventActivity.class);
                i.putExtra("focusEvent", new Serializer().serialize(eventData.get(pos - personData.size())));
                context.startActivity(i);
            }
        }
    }
}