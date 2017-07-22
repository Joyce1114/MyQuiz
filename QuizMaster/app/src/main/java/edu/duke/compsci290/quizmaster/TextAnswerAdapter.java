package edu.duke.compsci290.quizmaster;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;



public class TextAnswerAdapter extends RecyclerView.Adapter<TextAnswerAdapter.ViewHolder>{

    private RecyclerViewClickListener listener;
    private List<String> mAnswers;
    private Context mContext;

    public TextAnswerAdapter(Context context, List<String> answers, RecyclerViewClickListener recyclerViewClickListener) {
            this.mContext = context;
            this.mAnswers = answers;
            this.listener = recyclerViewClickListener;
    }

    @Override
    public TextAnswerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.text_items_rv, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TextAnswerAdapter.ViewHolder holder, int position) {
        String answer = mAnswers.get(position);
        final int p = position;
        holder.tvName.setText(answer);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listener.recyclerViewItemClicked(p);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAnswers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public View mView;
        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.itemNameText);
            mView = itemView;
        }
    }
}