package com.chen.filehide.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.chen.filehide.R;
import com.chen.filehide.help.SaveDirsHelper;
import com.chen.filehide.util.Logger;
import com.chen.filehide.util.ToastUtil;
import com.chen.filehide.util.Utils;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {
    private static final String HIDE_STR = ".hide.chen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.btn_welcome_execute_hide)
    public void executeHide() {
        dirExecute(true);
    }

    @OnClick(R.id.btn_welcome_execute_show)
    public void executeShow() {
        dirExecute(false);
    }

    /**
     * @param isHide 是否隐藏
     */
    private void dirExecute(boolean isHide) {
        List<String> dirs = SaveDirsHelper.readDirs();
        if (dirs == null || dirs.isEmpty()) {
            ToastUtil.show("没有要隐藏的文件夹");
            return;
        }
        for (String dirName : dirs) {
            File dir = new File(dirName);
            if (!dir.exists() || dir.isFile()) {
                continue;
            }
            Logger.d("操作的文件夹：" + dir.getAbsolutePath());
            File[] allFile = dir.listFiles();
            for (File file : allFile) {
                if (!file.exists() || file.isDirectory()) {
                    continue;
                }
                String fileName = file.getName();
                if (isHide) {
                    if (!fileName.endsWith(HIDE_STR)) {
                        String newName = file.getAbsoluteFile() + HIDE_STR;
                        file.renameTo(new File(newName));
                        Logger.d("hide:" + newName);
                    }
                } else {
                    if (fileName.endsWith(HIDE_STR)) {
                        String newName = file.getParent() + File.separator + fileName.replace(HIDE_STR, "");
                        file.renameTo(new File(newName));
                        Logger.d("show:" + newName);
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.verifyStoragePermissions(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
         *
         * add()方法的四个参数，依次是：
         *
         * 1、组别，如果不分组的话就写Menu.NONE,
         *
         * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单
         *
         * 3、顺序，那个菜单现在在前面由这个参数的大小决定
         *
         * 4、文本，菜单的显示文本
         */

        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "管理文件夹");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final EditText et = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("为了防止误操作\n请输入123，进入管理")
//                .setIcon(R.drawable.ic_launcher)
                .setView(et)
                //相当于点击确认按钮
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        if (!"123".equals(et.getText().toString().trim())) {
//                            ToastUtil.show("输入错误！");
//                            return;
//                        }
                        switch (item.getItemId()) {
                            case Menu.FIRST + 1:
                                Intent intent = new Intent(WelcomeActivity.this, ManagerDirActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                }).create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
        return false;
    }
}
