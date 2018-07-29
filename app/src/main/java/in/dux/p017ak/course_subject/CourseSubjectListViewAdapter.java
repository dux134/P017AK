package in.dux.p017ak.course_subject;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import in.dux.p017ak.R;

public class CourseSubjectListViewAdapter extends ArrayAdapter<CourseSubjectListDataModel> {

    private Activity activity;
    private List<CourseSubjectListDataModel> friendList;


    public CourseSubjectListViewAdapter(Activity context, int resource, List<CourseSubjectListDataModel> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.friendList = objects;
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public CourseSubjectListDataModel getItem(int position) {
        return friendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.recycler_item_course_subject_list, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.friendName.setText(getItem(position).getSubject_title());


        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(getItem(position).getTitle());
        //int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(friendList.get(position).getIcon(), color); // radius in px

        holder.imageView.setImageDrawable(drawable);

        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView friendName;

        public ViewHolder(View v) {
            imageView = (ImageView) v.findViewById(R.id.image_view);
            friendName = (TextView) v.findViewById(R.id.text);
        }
    }
}
