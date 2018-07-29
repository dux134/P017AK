package in.dux.p017ak.subject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.dux.p017ak.R;

public class SubjectMyAdapter extends RecyclerView.Adapter<SubjectMyAdapter.SubjectViewHolder>{
    private SubjectMyAdapter.RecyclerClickListener listener;
    private ArrayList<SubjectDataModel> list;
    private Context context;

    public SubjectMyAdapter(ArrayList<SubjectDataModel> mList,Context context1, SubjectMyAdapter.RecyclerClickListener listener1) {
        list = mList;
        listener = listener1;
        context = context1;
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_subject,parent,false);
        SubjectViewHolder holder = new SubjectViewHolder(view, listener);
        return  holder;
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder holder, int position) {

        holder.subjectTitle.setText(list.get(position).getTitle());
        Glide.with(context)
                .load(list.get(position).getImage_url())
                .into(holder.subjectImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecyclerClickListener {
        void onClick(View view, int adapterPosition);
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView subjectTitle;
        private ImageView subjectImage;
        private SubjectMyAdapter.RecyclerClickListener lv;

        public SubjectViewHolder(View itemView, RecyclerClickListener listener) {
            super(itemView);
            subjectTitle = itemView.findViewById(R.id.subject_title);
            subjectImage = itemView.findViewById(R.id.subject_image);
            lv = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            lv.onClick(view,getAdapterPosition());
        }
    }
}
