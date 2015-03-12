package hkec.yahoo.locatemyfriends;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by sinze on 3/12/15.
 */
public class MemberListAdapter extends ArrayAdapter<MemberObject> {

    public MemberObject[] list;
    Context context;
    int layoutResourceId;

    public MemberListAdapter(Context context, int layoutResourceId, MemberObject[] list) {
        super(context, layoutResourceId, list);
        this.list = list;
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MemberViewHolder viewHolder = null;
        final MemberObject member = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // displaying a new row view
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.member_list_entry, null);
            viewHolder = new MemberViewHolder();
            viewHolder.memberName = (TextView) convertView.findViewById(R.id.memberName);
            viewHolder.memberVisibility = (CheckBox) convertView.findViewById(R.id.memberVisibility);
            convertView.setTag(viewHolder);

        } else {
            // show a existing row view
            viewHolder = (MemberViewHolder) convertView.getTag();
        }

        // bind data with the view
        viewHolder.memberName.setText(member.name);
        viewHolder.memberVisibility.setChecked(member.visibility);
        final boolean isChecked = viewHolder.memberVisibility.isChecked();
        viewHolder.memberVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myLog", String.valueOf(member.visibility));
                if (isChecked) {
                    member.visibility = false;
                } else {
                    member.visibility = true;
                }
            }
        });

        return convertView;
    }

    private class MemberViewHolder {
        CheckBox memberVisibility;
        TextView memberName;
    }
}