package com.chen.filehide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.chen.filehide.R;
import com.chen.filehide.help.SaveDirsHelper;
import com.chen.filehide.util.Logger;
import com.chen.filehide.util.ToastUtil;
import com.chen.filehide.util.Utils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hui on 2017/4/23.
 */

public class ManagerDirActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dir);
        ButterKnife.bind(this);
    }

    @BindView(R.id.tv_welcome_execute_dirs)
    TextView tvWelcomeExecuteDirs;


    @OnClick(R.id.btn_welcome_select_directory)
    public void doSelectsDirectory() {
        Intent intent = new Intent(this, AddDirectoryActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_welcome_remove_directory)
    public void doRemoveDirectory() {
        Intent intent = new Intent(this, DeleteDirectoryActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        showManagerDirs();
        Utils.verifyStoragePermissions(this);
    }

    private void showManagerDirs() {
        List<String> dirs = SaveDirsHelper.readDirs();
        if (dirs == null || dirs.isEmpty()) {
            ToastUtil.show("没有要操作的文件夹");
            tvWelcomeExecuteDirs.setText("【null】");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String dirName : dirs) {
            File dir = new File(dirName);
            if (!dir.exists() || dir.isFile()) {
                continue;
            }
            Logger.d("操作的文件夹：" + dir.getAbsolutePath());
            sb.append("\n" + dir.getAbsoluteFile());
        }
        if (sb.length() > 0) {
            Logger.d("tvWelcomeExecuteDirs=" + tvWelcomeExecuteDirs);
            Logger.d("sb=" + sb);
            tvWelcomeExecuteDirs.setText(sb.toString().replaceFirst("\n", ""));
        } else {
            tvWelcomeExecuteDirs.setText("【null】");
        }
    }

}
