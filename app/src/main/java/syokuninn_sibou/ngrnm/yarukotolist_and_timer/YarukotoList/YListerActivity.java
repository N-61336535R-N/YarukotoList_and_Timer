package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import syokuninn_sibou.ngrnm.yarukotolist_and_timer.R;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.Timer.TimerActivity;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.*;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.Lists.*;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Utils.MoldDialogs;

/**
 *   ＜＜＜＜＜〜〜〜〜〜  やることリスト  〜〜〜〜〜＞＞＞＞＞
 * Created by M.R on 2017/04/13.
 * 
 * 〜〜〜 リストの一覧を表示する画面 〜〜〜
 * カテゴリ → リスト → 項目 → 項目編集
 * 　　　　　　↑今ココ
 * 
 */

public class YListerActivity extends AppCompatActivity {
    private static final String kind = "Lists";
    
    private LibraryChecker CC;
    
    private List<ListItem> list;
    private ImageArrayAdapter adapter;
    private ListView lv;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ylist);
    
        // Lists.list に登録されているカテゴリーのデータフォルダ
        //  が本当に存在しているかをまず確認。
        // （なければ作る。あれば、CCがList.listの読み込み媒体になる）
        CC = new LibraryChecker(kind);
        CC.check();
    
        updateListView();
    
        registerForContextMenu(lv);
    
        //リスト項目が選択された時のイベントを追加
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = position + "番目のアイテムがクリックされました";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    
                // やることリストの項目一覧画面に移動
                Consts.listName = CC.getNames().get(position);
                Intent intent = new Intent(YListerActivity.this, YItemsActivity.class);
                startActivity(intent);
            }
        });
    
        /*リスト項目が長押しされた時のイベントを追加
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = position + "番目のアイテムが長押しされました";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    
                ListView list = (ListView) parent;
                String selectedItem = (String) list.getItemAtPosition(position);
    
                return false;
            }
        });
        */
    
    
        // FloatingActionButton（↘︎のタイマーアイコン）
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // やることリスト（編集）画面に移動
                Intent intent = new Intent(YListerActivity.this, TimerActivity.class);
                startActivity(intent);
            }
        });
    }
    
    
    // リストの更新処理関係
    private List<ListItem> makeListView() {
        List<ListItem> list = new ArrayList<ListItem>();
        String ImgName;
        for (int i = 0; i < CC.getKind_num(); i++) {
            ListItem item = new ListItem();
            // item にタイトルをセット
            item.setText(CC.getNames().get(i));
            // item に画像をセット
            ImgName = CC.getImgNames().get(i);
            switch (ImgName) {
                case "No_Image":
                    break;
                default:
                    ImgName = CC.ImgDirPath() + ImgName;
            }
            item.setImagePath(ImgName);
            list.add(item);
        }
        return list;
    }
    
    private void updateListView() {
        // adapterの準備
        list = makeListView();
        // adapterのインスタンスを作成
        adapter = new ImageArrayAdapter(this, R.layout.list_view_image_item, list);
        
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter);
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
                if (CC.getNames().size() + 1 < Consts.LIMIT_Lists) {
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
                            CC.makeNewLibrary(newItemTitle, "No_Image");
                            Toast.makeText(YListerActivity.this, "「" + editView.getText().toString() + "」の作成成功", Toast.LENGTH_SHORT).show();
                            updateListView();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(YListerActivity.this, "リスト数の上限：" + Consts.LIMIT_Lists + "個を超えました", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "(未)設定", Toast.LENGTH_SHORT).show();
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
    
    
    // リスト長押しの処理（コンテキストメニュー）
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        
        switch (v.getId()) {
            case R.id.listview:
                
                AdapterView.AdapterContextMenuInfo info =
                        (AdapterView.AdapterContextMenuInfo) menuInfo;
                String s = CC.getNames().get(info.position);
                menu.setHeaderTitle(s);
                
                getMenuInflater().inflate(
                        R.menu.context_menu, menu
                );
                
                break;
        }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.listview_delete:
                AdapterView.AdapterContextMenuInfo info =
                        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                String s = CC.getNames().get(info.position);
                
                showDialogFragment(s, info.position);
                Toast.makeText(getBaseContext(), s
                        + " - Delete", Toast.LENGTH_SHORT).show();
                
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
            builder.setTitle(selectedItem+" を削除します");
            builder.setMessage("よろしいですか？");
    
            // positiveを選択した場合の処理．
            // リスナーはDialogINterface#onClickListener()
            // を使うことに注意．
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    
                        // 外部クラスのインスタンスを直接参照することができないため，
                        // Activity#getActivity()でActivityのインスタンスを取得する
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            YListerActivity activity = (YListerActivity) getActivity();
                            activity.removeList(posi);
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
    
    
    
    public void removeList(int posi) {
        CC.removeLibrary(posi);
    }
    
}
