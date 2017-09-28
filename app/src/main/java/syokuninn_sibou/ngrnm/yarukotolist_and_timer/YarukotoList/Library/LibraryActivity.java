package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import syokuninn_sibou.ngrnm.yarukotolist_and_timer.R;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.SettingActivity;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.Timer.TimerActivity;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.Timer.TimerSetActivity;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Utils.MoldDialogs;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.YItemsActivity;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.YListerActivity;

/**
 * LibraryChecker とのやりとりを中心に実装
 * 
 * IO:Activity と、 DataBase:LibraryChecker
 * を完全に分離することで、「Category, Listの表示形式を自由に変更」
 * を実現する。
 * （Consts の値(Settingで変更)を元に、switch でListの表示形式を決定）
 * 
 * 
 * ○ Checker → ViewData → LibraryActivity ができるように、リファクタリングする。
 */

public abstract class LibraryActivity extends AppCompatActivity {
    /* 以下のように実装して、識別子kindを指定する。
    private static final String kind = "[どのタイプのViewなのかの識別子]";
    @Override
    protected String getKind() {
        return kind;
    }
    */
    protected abstract String getKind();
    protected abstract LibraryChecker getLibC();
    protected abstract void setLibC(LibraryChecker LibC);

    protected abstract AdapterView getAView();
    protected abstract Context getThisActivity();
    
    
    protected abstract @LayoutRes int getLayoutResID();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
    
    
        // Lists.list に登録されているカテゴリーのデータフォルダ
        //  が本当に存在しているかをまず確認。
        // （なければ作る。あれば、getLibC(がList.listの読み込み媒体になる）
        setLibC( new LibraryChecker(getKind()) );
    
        
        updateListView();
    
        registerForContextMenu(getAView());
    
    
    
        //リスト項目が選択された時のイベントを追加
        getAView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = position + "番目のアイテムがクリックされました";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            
                // やることリストの項目一覧画面に移動
                Consts.listName = getLibC().getNames().get(position);
                Intent intent = new Intent(getThisActivity(), YItemsActivity.class);
                startActivity(intent);
            }
        });
    
    
    
    
        //  タイマー設定画面に移動するためのボタン(↓の横長ボタン)
        Button sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryActivity.this, TimerSetActivity.class);
                startActivity(intent);
            }
        });
        // FloatingActionButton（↘︎のタイマーアイコン）
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // やることリスト（編集）画面に移動
                Intent intent = new Intent(LibraryActivity.this, TimerActivity.class);
                startActivity(intent);
            }
        });
    }
    
    protected abstract void updateListView();
    
    protected void removeList(int posi) {
        getLibC().removeLibrary(posi);
    }
    
    
    
    
    
    
    
    // リスト長押しの処理（コンテキストメニュー）
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()) {
            case R.id.listview:
                nagaosiMenu(menu, menuInfo);
                break;
            case R.id.gridview:
                nagaosiMenu(menu, menuInfo);
                break;
        }
    }
    private void nagaosiMenu(ContextMenu menu, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String s = getLibC().getNames().get(info.position);
        menu.setHeaderTitle(s);
        
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.listview_delete:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                String s = getLibC().getNames().get(info.position);
                
                showDialogFragment(s, info.position);
                Toast.makeText(getBaseContext(), s + " - Delete", Toast.LENGTH_SHORT).show();
                
                return true;
            
            case R.id.listview_edit:
                return true;
        }
        return false;
    }
    
    // FragmentManagerでDialogを管理するクラス
    private void showDialogFragment(String selectedItem, int posi) {
        FragmentManager manager = getFragmentManager();
        DeleteDialog dialog = new DeleteDialog();
        dialog.setSelectedItem(selectedItem, posi);
        
        dialog.show(manager, "dialog");
    }
    
    
    /*
     * 削除ダイアログを生成する内部クラス
     * 内部クラスは外部クラスのインスタンスを直接参照できないため，
     * Activity#getActivity()で外部クラスのインスタンスを取得している．
     */
    public static class DeleteDialog extends DialogFragment {
        
        private static final String DEBUG = "DEBUG";
        /* 選択したListViewアイテム */
        private String selectedItem = null;
        private int posi;
        
        // 削除ダイアログの作成．
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(selectedItem+" を削除すると、もとに戻せません！");
            builder.setMessage("本当によろしいですか？");
            
            // positiveを選択した場合の処理．
            // リスナーはDialogINterface#onClickListener()
            // を使うことに注意．
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                
                // 外部クラスのインスタンスを直接参照することができないため，
                // Activity#getActivity()でActivityのインスタンスを取得する
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LibraryActivity activity = (LibraryActivity) getActivity();
                    activity.removeList(posi);
                    activity.updateListView();
                }
            });
            AlertDialog dialog = builder.create();
            return dialog;
        }
        
        // 選択したアイテムをセットする．
        // HACK:削除ダイアログ自身に選択したアイテムを渡せないため，
        // ダイアログをユーザが呼び出した際に，Activityで選択した項目を保持しておく．
        public void setSelectedItem(String selectedItem, int posi) {
            this.selectedItem = selectedItem;
            this.posi = posi;
        }
    }
    
    
    // ↗︎ オプションメニューの中身設定
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ylist_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        switch (id) {
            case R.id.menu_add_list:
                if (getLibC().getNames().size() < getLibC().getLIMIT_kind()) {
                    final EditText editView = new EditText(this);
                    AlertDialog.Builder dialog = MoldDialogs.makeBaseInputDialog(this, "新しい項目の名前を入力", editView);
                    MoldDialogs.setSimpleCancelButton(dialog);
                    // OKボタンの設定
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                    AlertDialog TsuikaDialog = dialog.create();
                    TsuikaDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(editView, 0);
                        }
                    });
                    TsuikaDialog.show();
                } else {
                    Toast.makeText(getThisActivity(), "リスト数の上限：" + Consts.LIMIT_Lists + "個を超えました", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "(未)設定", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LibraryActivity.this, SettingActivity.class);
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
