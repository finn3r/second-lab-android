package com.example.mydbapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {

    private final List<Songs> songs;

    public CustomRecyclerAdapter(List<Songs> songs) {
        this.songs = songs;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView author;
        public TextView duration;
        public TextView albumName;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            author = view.findViewById(R.id.author);
            duration = view.findViewById(R.id.duration);
            albumName = view.findViewById(R.id.albumName);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Songs songs = this.songs.get(position);
        holder.name.setText(songs.getName());
        holder.author.setText(songs.getAuthor());
        holder.albumName.setText(songs.getAlbumName());
        holder.duration.setText(String.format("%d:%02d", (songs.getDuration() / 60), songs.getDuration() % 60));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}
