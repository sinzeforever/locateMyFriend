package hkec.yahoo.locatemyfriends;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by sinze on 3/10/15.
 */
public class GroupListAdapter extends ArrayAdapter<GroupObject> {
    public GroupObject[] list;
    Context context;
    int layoutResourceId;

    public GroupListAdapter(Context context, int layoutResourceId, GroupObject[] list) {
        super(context, layoutResourceId, list);
        this.list = list;
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        GroupViewHolder viewHolder = null;
        final GroupObject group = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // displaying a new row view
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_list, null);
            viewHolder = new GroupViewHolder();
            viewHolder.groupName = (TextView) convertView.findViewById(R.id.groupName);
            viewHolder.groupMemberCount = (TextView) convertView.findViewById(R.id.groupMemberCount);
            viewHolder.groupVisibility = (CheckBox) convertView.findViewById(R.id.groupVisibility);
            convertView.setTag(viewHolder);
        } else {
            // show a existing row view
            viewHolder = (GroupViewHolder) convertView.getTag();
        }

        // bind data with the view
        viewHolder.groupName.setText(group.name);
        viewHolder.groupMemberCount.setText(String.valueOf(group.memberList.size()));
        viewHolder.groupVisibility.setChecked(group.visibility);
        final boolean isChecked = viewHolder.groupVisibility.isChecked();
        viewHolder.groupVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String targetGroupName = list[position].name;
                if (isChecked) {
                    UserProfile.instance.groupList.get(targetGroupName).visibility = false;
                } else {
                    UserProfile.instance.groupList.get(targetGroupName).visibility = true;
                }
            }
        });

        return convertView;
    }

    private class GroupViewHolder {
        CheckBox groupVisibility;
        TextView groupName;
        TextView groupMemberCount;
    }

}
