package com.aastudio.sarollahi.moviebox.Util;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.aastudio.sarollahi.moviebox.Activities.BackupRestoreActivity;
import com.aastudio.sarollahi.moviebox.Data.DbHelper;
import com.android.sarollahi.moviebox.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LocalBackup {

    private BackupRestoreActivity activity;

    public LocalBackup(BackupRestoreActivity activity) {
        this.activity = activity;
    }

    //ask to the user a name for the backup and perform it. The backup will be saved to a custom folder.
    public void performBackup(final DbHelper db, final String outFileName) {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));

        boolean success = true;
        if (!folder.exists())
            success = folder.mkdirs();
        if (success) {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                String formattedDate = df.format(c);


                String out = outFileName +"Movie Box ("+ formattedDate +")" +".db";
                db.backup(out);
                Intent intent = new Intent(activity,BackupRestoreActivity.class);
                activity.startActivity(intent);
                activity.finish();
        } else
        {Toast.makeText(activity, "Unable to create directory. Retry", Toast.LENGTH_SHORT).show();}
    }

    //ask to the user what backup to restore
    public void performRestore(final DbHelper db) {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));
        if (folder.exists()) {

            final File[] files = folder.listFiles();

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.select_dialog_item);
            for (File file : files)
                arrayAdapter.add(file.getName());

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
            builderSingle.setTitle("Restore:");
            builderSingle.setNegativeButton(
                    "cancel",
                    (dialog, which) -> dialog.dismiss());
            builderSingle.setAdapter(
                    arrayAdapter,
                    (dialog, which) -> {
                        try {
                            db.importDB(files[which].getPath());
                        } catch (Exception e) {
                            Toast.makeText(activity, "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
                        }
                    });
            builderSingle.show();
        } else
            Toast.makeText(activity, "Backup folder not present.\nDo a backup before a restore!", Toast.LENGTH_SHORT).show();
    }

}
