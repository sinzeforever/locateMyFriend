package hkec.yahoo.locatemyfriends;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by sinze on 3/10/15.
 */
public class GroupListAdapter extends ArrayAdapter<GroupObject> {
    public GroupObject[] list;
    MainActivity mainActivity;
    int layoutResourceId;

    public GroupListAdapter(Context context, int layoutResourceId, GroupObject[] list) {
        super(context, layoutResourceId, list);
        this.list = list;
        this.layoutResourceId = layoutResourceId;
        mainActivity = (MainActivity) context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        GroupViewHolder viewHolder = null;
        final GroupObject group = getItem(position);

        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // displaying a new row view
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_list_entry, null);
            viewHolder = new GroupViewHolder();
            viewHolder.groupName = (TextView) convertView.findViewById(R.id.groupName);
            viewHolder.groupVisibility = (CheckBox) convertView.findViewById(R.id.groupVisibility);
            viewHolder.groupWrapper = (RelativeLayout) convertView.findViewById(R.id.groupWrapper);
            viewHolder.groupMemberCount = (TextView) convertView.findViewById(R.id.groupMemberCount);
            viewHolder.groupColor = (TextView) convertView.findViewById(R.id.groupColor);

            convertView.setTag(viewHolder);
        } else {
            // show a existing row view
            viewHolder = (GroupViewHolder) convertView.getTag();
        }

        // bind data with the view
        viewHolder.groupName.setText(group.name);
        viewHolder.groupMemberCount.setText(String.valueOf(group.memberCount));
        viewHolder.groupVisibility.setChecked(group.visibility);
        final boolean isChecked = viewHolder.groupVisibility.isChecked();
        viewHolder.groupVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set group visibility
                if (isChecked) {
                    ((MyGroupFragment)mainActivity.currentFragment).callSetGroupVisibilityAPI(list[position], false);
                } else {
                    ((MyGroupFragment)mainActivity.currentFragment).callSetGroupVisibilityAPI(list[position], true);
                }
            }
        });

        // random set color
        viewHolder.groupColor.setBackgroundColor(Util.pickColor(position));

        viewHolder.groupWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupObject targetGroup = list[position];
                mainActivity.setGroupMemberPage(targetGroup);
            }
        });

        return convertView;
    }

    private class GroupViewHolder {
        CheckBox groupVisibility;
        TextView groupName;
        RelativeLayout groupWrapper;
        TextView groupMemberCount;
        TextView groupColor;
    }
}
