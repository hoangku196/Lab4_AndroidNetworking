package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class CustomRecyclerView extends RecyclerView.Adapter<CustomRecyclerView.ViewHolder> {

    private Context context;
    private List<Item> items;

    public CustomRecyclerView(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.tvAlbumId.setText(String.valueOf(item.getAlbumId()));
        holder.tvTitle.setText(item.getTitle());
        Log.e("tvText", holder.tvTitle.getText().toString());
//        Glide.with(holder.imgAvatar.getContext())
//                .load(item.getUrl())
//                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                .into(holder.imgAvatar);
        Picasso.get().load(item.getUrl())
                .error(R.drawable.ic_launcher_background)
                .into(holder.imgAvatar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDetails(v.getContext(), item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void dialogDetails(Context context, Item item) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View al = LayoutInflater.from(context).inflate(R.layout.item_detail, null);

        builder.setView(al);
        TextView tvItemId, tvItemAlbumId, tvItemTitle, tvItemUrl, tvItemThumbnailUrl;

        tvItemId = al.findViewById(R.id.tvItemId);
        tvItemAlbumId = al.findViewById(R.id.tvItemAlbumId);
        tvItemTitle = al.findViewById(R.id.tvItemTitle);
        tvItemUrl = al.findViewById(R.id.tvItemUrl);
        tvItemThumbnailUrl = al.findViewById(R.id.tvItemThumbnailUrl);
        tvItemId.setText(String.valueOf(item.getId()));
        tvItemAlbumId.setText(String.valueOf(item.getAlbumId()));
        tvItemTitle.setText(String.valueOf(item.getTitle()));
        tvItemUrl.setText(String.valueOf(item.getUrl()));
        tvItemThumbnailUrl.setText(String.valueOf(item.getThumbnailUrl()));

        builder.create().show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgAvatar;
        private TextView tvAlbumId, tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvAlbumId = itemView.findViewById(R.id.tvAlbumId);
            tvTitle = itemView.findViewById(R.id.tvTitle);

        }
    }
}
