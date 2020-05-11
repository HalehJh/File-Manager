package com.filemanagementapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewFolderDialog extends DialogFragment {

    private AddNewFolderCallback addNewFolderCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addNewFolderCallback = (AddNewFolderCallback) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_new_folder, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        final TextInputEditText et_addNewFolder = view.findViewById(R.id.et_addNewFolder);
        final TextInputLayout etl_addNewFolder = view.findViewById(R.id.etl_addNewFolder);
        view.findViewById(R.id.btn_addNewFolder_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_addNewFolder.length()>0){
                    addNewFolderCallback.onClick(et_addNewFolder.getText().toString());
                    dismiss();
                }else etl_addNewFolder.setError("Folder Name cannot be empty");
            }
        });

        return alertDialog.setView(view).create();
    }

    public interface AddNewFolderCallback{
        void onClick(String folderName);
    }
}
