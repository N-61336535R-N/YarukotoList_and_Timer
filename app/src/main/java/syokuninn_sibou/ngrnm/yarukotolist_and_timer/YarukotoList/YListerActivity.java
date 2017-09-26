package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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
 * 
 * 
 *  ■ mDatas が用意できれば、画面描画はできる。
 */

public class YListerActivity extends LibraryActivity {
    private static final String kind = "Lists";
    @Override protected final String getKind() {
        return kind;
    }
    
    private LibraryChecker LibC;
    @Override protected final LibraryChecker getLibC() {
        return LibC;
    }
    @Override protected final void setLibC(LibraryChecker LibC) {
        this.LibC = LibC;
    }
    
    
    @Override protected AdapterView getAView() {
        return lV;
    }
    
    @Override protected Context getThisActivity() {
        return YListerActivity.this;
    }
    
    
    private List<ViewData> list;
    private ImageArrayAdapter adapter;
    private ListView lV;
    
    
    @Override protected int getLayoutResID() {
        return R.layout.activity_ylist;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        //リスト項目が選択された時のイベントを追加
        lV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = position + "番目のアイテムがクリックされました";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    
                // やることリストの項目一覧画面に移動
                Consts.listName = LibC.getNames().get(position);
                Intent intent = new Intent(YListerActivity.this, YItemsActivity.class);
                startActivity(intent);
            }
        });
    }
    
    
    // リストの更新処理関係
    private List<ViewData> makeListView() {
        List<ViewData> list = new ArrayList<ViewData>();
        String ImgName;
        for (int i = 0; i < LibC.getKind_num(); i++) {
            // item に画像をセット
            ImgName = LibC.getImgNames().get(i);
            switch (ImgName) {
                case "No_Image":
                    break;
                default:
                    ImgName = LibC.ImgDirPath() + ImgName;
            }
            ViewData item = new ViewData(ImgName, LibC.getNames().get(i));
            list.add(item);
        }
        return list;
    }
    
    protected final void updateListView() {
        // adapterの準備
        list = makeListView();
        // adapterのインスタンスを作成
        adapter = new ImageArrayAdapter(this, R.layout.list_view_image_item, list);
        
        lV = (ListView) findViewById(R.id.listview);
        lV.setAdapter(adapter);
    }
    
}
