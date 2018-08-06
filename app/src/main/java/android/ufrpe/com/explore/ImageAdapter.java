package android.ufrpe.com.explore;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Marcelo on 05/08/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<User> mUsers;
    public ImageAdapter(Context context, List<User> users){
        this.mContext = context;
        this.mUsers = users;
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        User userCurrent = mUsers.get(position);
        holder.textViewName.setText(userCurrent.getEmail());
        Picasso.with(mContext)
                .load(userCurrent.getImgUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(View itemView){
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.text_view_name);
            imageView = (ImageView) itemView.findViewById(R.id.image_view_upload);
        }
    }
}
