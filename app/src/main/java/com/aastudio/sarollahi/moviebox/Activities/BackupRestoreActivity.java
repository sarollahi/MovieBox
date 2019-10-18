package com.aastudio.sarollahi.moviebox.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.aastudio.sarollahi.moviebox.Data.DbHelper;
import com.aastudio.sarollahi.moviebox.Util.LocalBackup;
import com.aastudio.sarollahi.moviebox.Util.Permissions;
import com.android.sarollahi.moviebox.R;

import java.io.File;

public class BackupRestoreActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PERMISSIONS = 2;

    private LocalBackup localBackup;

    private BackupRestoreActivity activity;

    private Button backupbtn;
    private Button restorebtn;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);
        activity = this;

        Permissions.verifyStoragePermissions(activity);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        localBackup = new LocalBackup(this);

        final DbHelper db = new DbHelper(getApplicationContext());

        backupbtn = findViewById(R.id.backupbtn);
        restorebtn = findViewById(R.id.restorebtn);
        list = findViewById(R.id.backlist);

        backupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + File.separator;
                localBackup.performBackup(db, outFileName);
            }
        });

        restorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteDB();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));
                if (folder.exists()) {

                    final File[] files = folder.listFiles();

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.select_dialog_item);
                    for (File file : files)
                        arrayAdapter.add(file.getName());


                    list.setAdapter(arrayAdapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                db.importDB(files[position].getPath());
                            } catch (Exception e) {
                                Toast.makeText(activity, "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            AlertDialog dialog = new AlertDialog.Builder(BackupRestoreActivity.this).create();
                            dialog.setTitle("Delete backup");
                            dialog.setMessage("are you sure?");
                            dialog.setCancelable(false);
                            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int buttonId) {
                                    File dfile = new File(files[position].getPath());
                                    boolean deleted = dfile.delete();
                                    arrayAdapter.remove(arrayAdapter.getItem(position));
                                    arrayAdapter.notifyDataSetChanged();
                                    Intent intent = new Intent(BackupRestoreActivity.this,BackupRestoreActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int buttonId) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.setIcon(android.R.drawable.ic_dialog_alert);
                            dialog.show();

                            return true;
                        }
                    });

                }

            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
            }
        }




    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
