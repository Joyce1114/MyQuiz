package edu.duke.compsci290.quizmaster;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private RecyclerViewClickListener listener;
    private List<String> mOptions;
    private Context mContext;

    public MainAdapter(Context context, List<String> options, RecyclerViewClickListener recyclerViewClickListener) {
        this.mContext = context;
        this.mOptions = options;
        this.listener = recyclerViewClickListener;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.option_items_rv, parent, false);
        MainAdapter.ViewHolder viewHolder = new MainAdapter.ViewHolder(itemView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, int position) {
        final String item = mOptions.get(position);
        final int p = position;
        holder.textView.setText(item);
        holder.mView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.recyclerViewItemClicked(p);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "You will have a/an "+ item + " displayed and have to choose the right one", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mOptions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public View mView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.options);
            mView = itemView;
        }
    }
}