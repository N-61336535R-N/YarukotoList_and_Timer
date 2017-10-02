package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import syokuninn_sibou.ngrnm.yarukotolist_and_timer.R;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.Settings.SettingActivity;

/**
 * Created by ryo on 2017/10/02.
 */

public abstract class YActivity extends AppCompatActivity {
    
    abstract void updateListView();
    
    abstract void removeList(int posi);
    
    // ↗︎ メニューの中身設定
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.y_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        switch (id) {
            case R.id.menu_add_list:
                if (getLibC().getNames().size() < getLibC().getLIMIT_kind()) {
                    final EditText editView = new EditText(this);
                    editView.setInputType(InputType.TYPE_CLASS_TEXT);
                    AlertDialog.Builder ADBuilder
                            = new AlertDialog.Builder(this)
                            .setTitle("新しい項目の名前を入力")
                            .setView(editView)
                            .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Okボタンを押した時の処理。何もしないver
                                }
                            });
                    // OKボタンの設定
                    ADBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String newItemTitle = editView.getText().toString();
                            // サムネイル画像は、指定次第、その場で imgDirPath まで移動させる。
                            // 第２引数は、移動先内での、その画像ファイルの名前のみ
                            // 今は、"No_Image"で統一
                            getLibC().makeNewLibrary(newItemTitle, "No_Image");
                            Toast.makeText(getThisActivity(), "「" + editView.getText().toString() + "」の作成に成功しました!", Toast.LENGTH_SHORT).show();
                            updateListView();
                        }
                    });
                    AlertDialog TsuikaDialog = ADBuilder.create();
                    TsuikaDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(editView, 0);
                        }
                    });
                    TsuikaDialog.show();
                } else {
                    Toast.makeText(getThisActivity(), "上限：" + getLibC().getLIMIT_kind() + " 個を超えました", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "設定", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(YActivity.this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_save_timer_set:
                Toast.makeText(this, "(未)タイマーリスト保存", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.system_exit:
                Toast.makeText(this, "(未)終了", Toast.LENGTH_SHORT).show();
                return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
}