package com.filemanagementapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.File;
import java.nio.file.Path;

public class MainActivity extends AppCompatActivity implements AddNewFolderDialog.AddNewFolderCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPref sharedPref = new SharedPref(this);
        sharedPref.edit("false");

        if(StorageHelper.isExternalStorageReadable()){
            File file = getExternalFilesDir(null);
            listFiles(file.getPath(), false);
        }

        findViewById(R.id.iv_addNewFolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewFolderDialog dialog = new AddNewFolderDialog();
                dialog.show(getSupportFragmentManager(),null);
            }
        });

        EditText et_main = findViewById(R.id.et_main);
        et_main.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
                if (fragment instanceof Fragment_lists){
                    ((Fragment_lists) fragment).searchItem(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        MaterialButtonToggleGroup toggleButton_main =findViewById(R.id.toggleButton_main);
        toggleButton_main.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId ==R.id.btn_main_list && isChecked){
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
                    if (fragment instanceof Fragment_lists){
                        ((Fragment_lists) fragment).setViewType(ViewType.Row);
                    }
                }else if (checkedId==R.id.btn_main_grid && isChecked){
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
                    if (fragment instanceof Fragment_lists){
                        ((Fragment_lists) fragment).setViewType(ViewType.Grid);
                    }
                }
            }
        });

    }

    public void listFiles(String path, boolean addToBackStack){

        Fragment_lists fragment_lists = new Fragment_lists();
        Bundle bundle= new Bundle();
        bundle.putString("path", path);
        fragment_lists.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_fragmentContainer, fragment_lists);

        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    public void listFiles(String path){
        this.listFiles(path, true);
    }

    @Override
    public void onClick(String folderName) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
        if (fragment instanceof Fragment_lists ){
            ((Fragment_lists) fragment).createNewFolder(folderName);
        }
    }
}
