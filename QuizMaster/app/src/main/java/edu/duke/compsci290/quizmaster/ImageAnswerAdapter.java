package edu.duke.compsci290.quizmaster;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;



public class ImageAnswerAdapter extends RecyclerView.Adapter<ImageAnswerAdapter.ViewHolder> {

    private RecyclerViewClickListener listener;
    private List<String> mAnswers;
    private Context mContext;

    public ImageAnswerAdapter(Context context, List<String> answers, RecyclerViewClickListener recyclerViewClickListener) {
        this.mContext = context;
        this.mAnswers = answers;
        this.listener = recyclerViewClickListener;
    }

    @Override
    public ImageAnswerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.image_items_rv, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageAnswerAdapter.ViewHolder holder, int position) {
        String item = mAnswers.get(position);
        final int p = position;
        Glide.with(mContext)
                .load(item)
                .into(holder.imageView);
//        try {
//            InputStream is = mContext.getAssets().open(item);
//            Drawable d = Drawable.createFromStream(is, null);
//            holder.imageView.setImageDrawable(d);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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

        public ImageView imageView;
        public View mView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            mView = itemView;
        }
    }
}
