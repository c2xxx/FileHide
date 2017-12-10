package com.chen.filehide.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chen.filehide.R;
import com.chen.filehide.adapter.ShowDirsAdapter;
import com.chen.filehide.bean.ContentBean;
import com.chen.filehide.help.SaveDirsHelper;
import com.chen.filehide.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hui on 2017/4/23.
 */

public class DeleteDirectoryActivity extends BaseActivity {
    @BindView(R.id.lv_delete_directory)
    ListView lvDeleteDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_directory);
        ButterKnife.bind(this);
        reShowDirs();
    }

    private void reShowDirs() {
        List<ContentBean> list = new ArrayList<>();
        List<String> dirs = SaveDirsHelper.readDirs();
        if (dirs == null || dirs.isEmpty()) {
            ToastUtil.show("没有管理中的文件夹");
            showDirs(list);
            return;
        }
        for (String f : dirs) {
            ContentBean item = new ContentBean();
            item.setTitle(f);
            list.add(item);
        }
        showDirs(list);
    }

    private void showDirs(final List<ContentBean> list) {
        ShowDirsAdapter adapter = new ShowDirsAdapter(this, list);
        lvDeleteDirectory.setAdapter(adapter);
        lvDeleteDirectory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtil.show("position=" + list.get(position).getTitle());
                String name = list.get(position).getTitle();
                ToastUtil.show("删除：" + name);
                SaveDirsHelper.remove(name);
                reShowDirs();
            }
        });
    }
}
