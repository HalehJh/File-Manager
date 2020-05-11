package com.filemanagementapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.util.Arrays;

public class Fragment_lists extends Fragment implements itemAdapter.FileItemEventListener {

    private static final String TAG = "Fragment_lists";
    String path;
    itemAdapter adapter;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString("path");

        SharedPref sharedPref = new SharedPref(getContext());
        String create = sharedPref.getCreate();
        if (create=="false"){
            createNewFolder("Destination");
            sharedPref.edit("true");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_files, container, false);
        recyclerView = view.findViewById(R.id.rv_files_path);
        gridLayoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        File currentFile = new File(path);

        if (StorageHelper.isExternalStorageReadable()){
            File[] file = currentFile.listFiles();
            adapter = new itemAdapter(Arrays.asList(file), this);
            recyclerView.setAdapter(adapter);
        }

        TextView textView = view.findViewById(R.id.tv_files_path);
        textView.setText(currentFile.getName().equalsIgnoreCase("files")? "External Storage": currentFile.getName());
        view.findViewById(R.id.im_files_path).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        return view;
    }

    @Override
    public void onItemClicked(File file) {
        if (file.isDirectory()){
            ((MainActivity)getActivity()).listFiles(file.getPath());
        }
    }

    @Override
    public void onDeleteItem(File file) {
        if (file.delete()){
            adapter.deleteFile(file);
        }
    }

    @Override
    public void onCopyFile(File file) {
        if (StorageHelper.isExternalStorageWritable()){
            try {
                copy(file, getDestinationFile(file.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMoveFile(File file) {
        if (StorageHelper.isExternalStorageWritable()){
            try {
                copy(file, getDestinationFile(file.getName()));
                onDeleteItem(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createNewFolder(String folderName){

        if(StorageHelper.isExternalStorageWritable()) {
            File newFolder = new File(path+File.separator+folderName);
            if (!newFolder.exists()){
                if (newFolder.mkdir()){
                    adapter.addNewFile(newFolder);
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        }

    }

    private void copy(File source, File destination) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(source);
        FileOutputStream fileOutputStream = new FileOutputStream(destination);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fileInputStream.read(buffer))>0){
            fileOutputStream.write(buffer,0, length);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }

    private File getDestinationFile(String fileName){
       return new File(getContext().getExternalFilesDir(null)+File.separator+"Destination"+File.separator+fileName);
    }

    public void searchItem(String query){
        if (adapter!=null)
        adapter.search(query);
    }

    public void setViewType(ViewType viewType){
        if (adapter!=null){
            adapter.setViewType(viewType);
        if (viewType == ViewType.Row){
            gridLayoutManager.setSpanCount(1);
        }else {
            gridLayoutManager.setSpanCount(2);
        }
        }
    }
}
