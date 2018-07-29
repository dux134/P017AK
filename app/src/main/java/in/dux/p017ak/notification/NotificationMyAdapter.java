package in.dux.p017ak.notification;

import android.app.Activity;
import android.companion.WifiDeviceFilter;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import in.dux.p017ak.R;
import in.dux.p017ak.programming_lang.ProgrammingLangDataModel;

import static java.security.AccessController.getContext;

public class NotificationMyAdapter extends RecyclerView.Adapter<NotificationMyAdapter.NotificationViewHolder> {
    private ArrayList<NotificationDataModel> list;
    private NotificationMyAdapter.RecyclerItemListener listener;
    private Activity context;
    public static int width;

    public NotificationMyAdapter(ArrayList<NotificationDataModel> list1, Activity context1, NotificationMyAdapter.RecyclerItemListener listener1) {
        list = list1;
        listener = listener1;
        context = context1;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_notification,parent,false);
        NotificationViewHolder holder = new NotificationViewHolder(view,listener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecyclerItemListener {
        void onClick(View view, int adapterPosition);
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private RecyclerItemListener listener2;

        public NotificationViewHolder(View itemView, RecyclerItemListener listener) {
            super(itemView);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;

            title = itemView.findViewById(R.id.notificationCardTitle);

            title.setMinWidth(width-100);
            title.setMaxWidth(width-100);
            listener2 = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener2.onClick(view,getAdapterPosition());
        }
    }
}
