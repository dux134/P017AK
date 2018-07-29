package in.dux.p017ak.programming_lang;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.dux.p017ak.R;

public class ProgrammingLangMyAdapter extends RecyclerView.Adapter<ProgrammingLangMyAdapter.ProgrammingLangViewHolder> {
    private ArrayList<ProgrammingLangDataModel> list;
    private RecyclerItemListener recyclerItemListener;
    private Context context;

    public ProgrammingLangMyAdapter(ArrayList<ProgrammingLangDataModel> mList, Context context1, RecyclerItemListener listener1) {
        list = mList;
        recyclerItemListener = listener1;
        context = context1;
    }
    @NonNull
    @Override
    public ProgrammingLangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_top_programming,parent,false);
        ProgrammingLangViewHolder holder = new ProgrammingLangViewHolder(view,recyclerItemListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgrammingLangViewHolder holder, int position) {
        holder.text.setText(list.get(position).getTitle());
        Glide.with(context)
                .load(list.get(position).getImage_url())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecyclerItemListener {
        void onClick(View view, int adapterPosition);
    }

    public class ProgrammingLangViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView text;
        private ProgrammingLangMyAdapter.RecyclerItemListener listener;

        public ProgrammingLangViewHolder(View itemView, RecyclerItemListener recyclerItemListener) {
            super(itemView);

            image = itemView.findViewById(R.id.subject_image);
            text = itemView.findViewById(R.id.subject_title);
            listener = recyclerItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());
        }
    }
}
