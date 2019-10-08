package edu.byu.cs240.FamilyMap.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.byu.cs240.FamilyMap.R;
import edu.byu.cs240.FamilyMap.model.Event;
import edu.byu.cs240.FamilyMap.model.Model;
import edu.byu.cs240.FamilyMap.model.Person;
import edu.byu.cs240.FamilyMap.utils.Serializer;

class PersonExpListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final ArrayList<String> headers;
    private final List<Event> eventData;
    private final List<Person> personData;
    // Events are the first group in the expandable list view
    private final int eventGroupPosition = 0;

    public PersonExpListAdapter(Context context, ArrayList<String> headers,
                                List<Event> eventData, List<Person> personData) {
        this.headers = headers;
        this.context = context;
        this.eventData = eventData;
        this.personData = personData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        if (groupPosition == eventGroupPosition) {
            return eventData.get(childPosititon);
        } else {
            return personData.get(childPosititon);
        }
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_row, null);
        }

        TextView topRow = convertView.findViewById(R.id.expandableListViewTopRow);
        TextView bottomRow = convertView.findViewById(R.id.expandableListViewBottomRow);
        ImageView rowImage = convertView.findViewById(R.id.expandableListViewRowIcon);
        LinearLayout rowBox= convertView.findViewById(R.id.listItemLinearLayout);

        rowBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Serializer s = new Serializer();
                Intent i;

                if (groupPosition == eventGroupPosition) {
                    // Make an event activity
                    Event childEvent = ((Event)getChild(groupPosition, childPosition));
                    i = new Intent(context, EventActivity.class);
                    i.putExtra("focusEvent", s.serialize(childEvent));
                } else {
                    // Make a new person activity
                    Person p = ((Person)getChild(groupPosition, childPosition));
                    Model.getInstance().setCurrentlySelectedPersonID(p.getPersonID());
                    i = new Intent(context, PersonActivity.class);
                    i.putExtra("activityPerson", new Serializer().serialize(p));
                }
                context.startActivity(i);
            }
        });

        if (groupPosition == eventGroupPosition) {
            Event childEvent = ((Event)getChild(groupPosition, childPosition));
            // The icon should be the red marker icon
            rowImage.setImageResource(R.drawable.marker);
            topRow.setText(childEvent.getEventType() + ": " + childEvent.getCity() + ", " +
                    childEvent.getCountry() + " (" + childEvent.getYear() + ")");
            Person childPerson = Model.getInstance().getPersonByPersonID(childEvent.getPersonID());
            bottomRow.setText(childPerson.getFirstName() + " " + childPerson.getLastName());
        } else {
            Person childPerson = ((Person)getChild(groupPosition, childPosition));

            // Determine icon based on gender
            if (childPerson.getGender().equals("m")) {
                rowImage.setImageResource(R.drawable.male_icon);
            } else {
                rowImage.setImageResource(R.drawable.female_icon);
            }

            topRow.setText(childPerson.getFirstName() + " " + childPerson.getLastName());
            Model m = Model.getInstance();
            // Figure out what the relationship is
            bottomRow.setText(Model.getInstance().
                    getRelationship(m.getPersonByPersonID(
                            m.getCurrentlySelectedPersonID()), childPerson));
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == eventGroupPosition) {
            return eventData.size();
        } else {
            return personData.size();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headers.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_group, null);
        }

        TextView lblListHeader = convertView
                .findViewById(R.id.expandableListViewHeaderTextView);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}