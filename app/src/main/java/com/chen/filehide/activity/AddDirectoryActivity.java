package com.chen.filehide.activity;

import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.chen.filehide.R;
import com.chen.filehide.adapter.ShowDirsAdapter;
import com.chen.filehide.bean.ContentBean;
import com.chen.filehide.help.SaveDirsHelper;
import com.chen.filehide.util.Logger;
import com.chen.filehide.util.ToastUtil;
import com.chen.filehide.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加文件夹
 * Created by hui on 2017/4/23.
 */

public class AddDirectoryActivity extends BaseActivity {
    @BindView(R.id.tv_select_directory_current)
    TextView tvSelectDirectoryCurrent;
    @BindView(R.id.lv_select_directory_dirs)
    ListView lvSelectDirectoryDirs;
    @BindView(R.id.et_select_directory_search)
    EditText etSelectDirectorySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_directory);
        ButterKnife.bind(this);
        initViews();
        requestPermissionSD();
    }

    private void requestPermissionSD() {
        Utils.verifyStoragePermissions(this);
    }

    private void initViews() {
        setTitle("请选择目录");
        String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        reShowDirs(rootDir);
        etSelectDirectorySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                doSearch();
            }
        });
    }

    private void reShowDirs(String rootDir) {
        String rootD = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (TextUtils.isEmpty(rootDir)) {
            return;
        } else if (rootDir.startsWith(rootD)) {
        } else if (!rootDir.startsWith(rootD)) {
            rootDir = tvSelectDirectoryCurrent.getText().toString() + File.separator + rootDir;
        }
        File file = new File(rootDir);
        if (!file.exists() || file.isFile()) {
            return;
        }
        tvSelectDirectoryCurrent.setText(rootDir);
        openDir(tvSelectDirectoryCurrent.getText().toString(), null);
    }

    private void openDir(String directoryName, String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            keyword = keyword.toLowerCase();
        }
        File file = new File(directoryName);
        if (!file.exists()) {
            Logger.d("目录不存在！");
            ToastUtil.show("目录不存在\n" + directoryName);
        } else if (!file.isDirectory()) {
            Logger.d("不是目录！");
            ToastUtil.show(directoryName + "不是目录！");
        } else {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                Logger.d("目录为空或没有权限！");
                ToastUtil.show("目录为空或没有权限！\n" + directoryName);
            } else {
                List<ContentBean> list = new ArrayList<>();
                Arrays.sort(files);
                for (File f : files) {
                    if (f == null) {
                        continue;
                    }
                    if (!TextUtils.isEmpty(keyword)) {
                        String fName = f.getName().toLowerCase();
                        if (!fName.contains(keyword)) {
                            continue;
                        }
                    }

                    //搜索可以搜索搜出文件，.开头的文件夹
                    if (TextUtils.isEmpty(keyword)) {
                        if (f.isFile()) {
                            continue;
                        }
                        if (f.getName().startsWith(".")) {
                            continue;
                        }
                    }

                    ContentBean item = new ContentBean();
                    if (f.isFile()) {
                        item.setTitle("--" + f.getName());
                        list.add(item);
                    } else {
                        item.setTitle(f.getName());
                        list.add(0, item);
                    }
                }
                showDirs(list);
            }
        }
    }

    private void changeToFirst(String s, List<ContentBean> files) {
        if (files == null || TextUtils.isEmpty(s)) {
            return;
        }
        Iterator<ContentBean> it = files.iterator();
        List<ContentBean> pre = new ArrayList<>();
        while (it.hasNext()) {
            ContentBean item = it.next();
            if (s.contains(item.getTitle())) {
                it.remove();
                pre.add(item);
            }
        }
        files.addAll(0, pre);
    }

    private void showDirs(final List<ContentBean> list) {
        Logger.d("size A " + list.size());
        changeToFirst("QQBrowser,UCDownloads", list);
        Logger.d("size B " + list.size());
        ShowDirsAdapter adapter = new ShowDirsAdapter(this, list);
        lvSelectDirectoryDirs.setAdapter(adapter);
        lvSelectDirectoryDirs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtil.show("position=" + list.get(position).getTitle());
                String name = list.get(position).getTitle();
                if (!name.startsWith("--")) {
                    reShowDirs(name);
                }
            }
        });
    }

    @OnClick(R.id.btn_select_directory_pre)
    public void preDir() {
        String curr = tvSelectDirectoryCurrent.getText().toString();
        if (TextUtils.isEmpty(curr)) {
            return;
        }
        if (curr.indexOf(File.separator) == -1) {
            return;
        }
        String rootD = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (TextUtils.equals(rootD, curr)) {
            ToastUtil.show("最顶层了！");
        }
        String path = curr.substring(0, curr.lastIndexOf(File.separator));
        reShowDirs(path);
    }

    @OnClick(R.id.btn_select_directory_add)
    public void executeAdd() {
        String curr = tvSelectDirectoryCurrent.getText().toString();
        if (TextUtils.isEmpty(curr)) {
            ToastUtil.show("不能为空");
            return;
        }
        File file = new File(curr);
        if (!file.exists()) {
            ToastUtil.show("不存在！");
        } else if (file.isFile()) {
            ToastUtil.show("是文件不是目录！");
        }
        boolean isOk = SaveDirsHelper.appendDir(curr);
        if (isOk) {
            ToastUtil.show("添加成功！\n" + curr);
        }
    }

    //    @OnClick(R.id.btn_select_directory_search)
    public void doSearch() {
        String keyword = etSelectDirectorySearch.getText().toString().trim();

        openDir(tvSelectDirectoryCurrent.getText().toString(), keyword);
    }
}
