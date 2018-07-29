package in.dux.p017ak.course_subject;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;

import in.dux.p017ak.R;

public class PreviousYearMyAdapter extends RecyclerView.Adapter<PreviousYearMyAdapter.PreviousYearViewHolder> {

    private ArrayList<PreviousYearDataModel> list;
    private PreviousYearMyAdapter.RecyclerItemListener listener;

    public PreviousYearMyAdapter(ArrayList<PreviousYearDataModel> mList,PreviousYearMyAdapter.RecyclerItemListener listener1) {
        listener = listener1;
        list = mList;
    }

    @NonNull
    @Override
    public PreviousYearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_previous_year,parent,false);
        PreviousYearViewHolder holder = new PreviousYearViewHolder(view,listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousYearViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        String firstLetter = String.valueOf(list.get(position).getIcon());

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(list.get(position).getTitle());
        //int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color); // radius in px

        holder.imageView.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecyclerItemListener {
        void onClick(View view, int adapterPosition);
    }

    public class PreviousYearViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView title;
        private RecyclerItemListener recyclerItemListener;

        public PreviousYearViewHolder(View itemView, RecyclerItemListener listener) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.previous_year_image);
            title = (TextView) itemView.findViewById(R.id.previous_year_text);
            recyclerItemListener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerItemListener.onClick(view,getAdapterPosition());
        }
    }
}
