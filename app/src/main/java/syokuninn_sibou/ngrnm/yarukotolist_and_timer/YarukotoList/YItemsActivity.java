package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;

import de.timroes.android.listview.EnhancedListView;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.R;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.Timer.TimerActivity;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Items.ItemsChecker;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.Consts;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Utils.MoldDialogs;

/**
 *  ＜＜＜＜＜〜〜〜〜〜  やることリストの内容(Item)一覧  〜〜〜〜〜＞＞＞＞＞
 * 
 * Items ⇆ Item は、タイトルが変わる可能性が高い。
 * よって、タイトルとファイル名は分離すべき
 * （タイトルをファイル名にしてしまうと、
 * 　　タイトルを変更したとき、Items のリストの中身が変わってしまう）
 * 管理番号を各々用意して、
 * それに対して
 * [0] タイトル
 * [1] 概要
 * [2] 詳細
 * のファイルを各々作る。
 */

public class YItemsActivity extends AppCompatActivity {
    private static final String kind = "Item";
    
    // 最初のリスト：カテゴリクリック時に、そのカテゴリの固有IDを取得
    //                              → その固有IDを元に、リストファイルから項目を取得
    /** 起動を早くするために、一度項目を取得したリストは一定期間保持し続けるスタイルに
     → 取得するときに、保持リストの総数が規定値を超えたら、一番古いリストを破棄する。
     */
    // リストビュー
    private EnhancedListView mListView;
    // リストビューに設定するリストとアダプター
    private LinkedList<String> mItemList;
    private ArrayAdapter<String> mAdapter;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yitems);
    
        // 初期値読み込み
        ItemsChecker ItCheck = new ItemsChecker("list");  // リスト内容
        ItemsChecker FItCheck = new ItemsChecker("finish");  // 終了リストの内容
        // リストビュー
        mListView = (EnhancedListView) findViewById(R.id.listview1);
    
        // 再描画のために一つにまとめた
        makeListView();
        
        // アイテムクリック時のイベントを追加
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                // 選択アイテムを取得
                ListView listView = (ListView)parent;
                String item = (String)listView.getItemAtPosition(pos);
                Toast.makeText(YItemsActivity.this, item, Toast.LENGTH_LONG).show();
                /**(未)
                 * 次のintentに移動
                 * [実験] Preference.java に飛ぶ
                 * [実装] 項目の編集画面に飛ぶ
                 *      項目内に保存されている
                 *      ○ タイトル
                 *      ○ 概要
                 *      ○ 詳細
                 *      ○ メモ
                 *      ○ 達成度
                 *      ○ 各種時間
                 *      を読み込んで、編集できるようにする。
                 */
                Consts.ItemNumber = pos;
                // やることリスト（編集）画面に移動
                Intent intent = new Intent(YItemsActivity.this, YItemEditActivity.class);
                startActivity(intent);
            }
        });
        
        /* スワイプで消す（終了リストに追加する）設定
          *  追加削除をその都度やっていたら、負担が大きいので、
          *  追加削除をするのはリストだけ。
          *  画面遷移するタイミングでファイルを更新。
        */
        mListView.setDismissCallback(new de.timroes.android.listview.EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {
                
                final String item = (String) mAdapter.getItem(position);
                /// 消す処理
                mItemList.remove(position);
                mAdapter.notifyDataSetChanged();
                return new EnhancedListView.Undoable() {
                    @Override
                    public void undo() {
                        // 元に戻す処理
                        mItemList.add(position, item);
                        mAdapter.notifyDataSetChanged();
                    }
                };
            }
        });
        mListView.enableSwipeToDismiss();
        
        
        
        // FloatingActionButton（↘︎のタイマーアイコン）
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // やることリスト（編集）画面に移動
                Intent intent = new Intent(YItemsActivity.this, TimerActivity.class);
                startActivity(intent);
            }
        });
    
    
    }
    
    
    
    
    private ItemsChecker ItC = new ItemsChecker("aaa");
    
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
                if (ItC.getTitles().size() + 1 < Consts.LIMIT_Items) {
                    final EditText editView = new EditText(this);
                    AlertDialog.Builder dialog = MoldDialogs.makeBaseInputDialog(this, "新しい項目の名前を入力", editView);
                    MoldDialogs.setSimpleCancelButton(dialog);
                    // OKボタンの設定
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String newItemTitle = editView.getText().toString();
                            ItC.makeNewItem(newItemTitle);
                            Toast.makeText(YItemsActivity.this, "「"+editView.getText().toString()+"」の作成成功", Toast.LENGTH_SHORT).show();
                            makeListView();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(YItemsActivity.this, "項目数の上限："+Consts.LIMIT_Items+"個を超えました", Toast.LENGTH_SHORT).show(); 
                }
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "(未)設定", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.edit_mode:
                Toast.makeText(this, "(未)編集モード", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.system_exit:
                Toast.makeText(this, "(未)終了", Toast.LENGTH_SHORT).show();
                return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    
    public void makeListView() {
        // リストビューにアイテム追加
        mItemList = new LinkedList<String>();
        mItemList.addAll( ItC.getTitles() );
        mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.text, mItemList);
        mListView.setAdapter(mAdapter);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        makeListView();
    }
    
    // 画面遷移前に、mItemList の変更をディレクトリ構造に反映させる。
    @Override
    protected void onPause() {
        super.onPause();
        // 全て書き換えor変更箇所だけ書き換え
        // → 全て比較して、変更箇所のみ変更
        ItC.ReflectUpdate();
    }
    
    
}

