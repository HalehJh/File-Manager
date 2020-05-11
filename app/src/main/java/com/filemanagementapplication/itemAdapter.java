package com.filemanagementapplication;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.FileViewHolder> {

    List<File> files ;
    List<File> filteredFiles;
    private FileItemEventListener listener;
    ViewType viewType =ViewType.Row;

    public itemAdapter(List<File> files, FileItemEventListener listener){
        this.files =new ArrayList<>(files);
        this.listener = listener;
        this.filteredFiles = new ArrayList<>(files);
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==ViewType.Row.getValue()){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        }else {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_grid, parent, false);
        }

        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.bind(filteredFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredFiles.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_file, iv_file_more;
        TextView  tv_fileName;
        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_file = itemView.findViewById(R.id.iv_file);
            tv_fileName = itemView.findViewById(R.id.tv_file);
            iv_file_more = itemView.findViewById(R.id.iv_file_more);
        }

        private void bind(final File file){
            if (file.isDirectory()){
                iv_file.setImageResource(R.drawable.ic_folder_black_32dp);
            }else
                iv_file.setImageResource(R.drawable.ic_file_black_32dp);

            tv_fileName.setText(file.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(file);
                }
            });

            iv_file_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.menu_delete:
                                    listener.onDeleteItem(file);
                                    break;
                                case R.id.menu_copy:
                                    listener.onCopyFile(file);
                                    break;
                                case R.id.menu_move:
                                    listener.onMoveFile(file);
                                    break;
                            }

                            return false;
                        }
                    });
                }
            });
        }
    }

    public interface FileItemEventListener{
        void onItemClicked(File file);
        void onDeleteItem(File file);
        void onCopyFile(File file);
        void onMoveFile(File file);
    }

    public void addNewFile(File file){
        filteredFiles.add(0,file);
        notifyItemInserted(0);
    }

    public void deleteFile(File file){
        int index = files.indexOf(file);
        if (index>-1){
            filteredFiles.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void search(String query){

        if (query.length()>0){
            List<File> result = new ArrayList<>();
            for (File file: this.files) {
                if (file.getName().toLowerCase().contains(query.toLowerCase())){
                    result.add(file);
                }
            }
            this.filteredFiles = result;
            notifyDataSetChanged();
        }else {
            this.filteredFiles = this.files;
            notifyDataSetChanged();
        }

    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType.getValue();
    }
}
