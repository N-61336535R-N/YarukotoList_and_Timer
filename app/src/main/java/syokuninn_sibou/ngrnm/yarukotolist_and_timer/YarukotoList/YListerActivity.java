package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import syokuninn_sibou.ngrnm.yarukotolist_and_timer.R;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.*;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.Lists.*;

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

public class YListerActivity extends YLibraryActivity {
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
    
    
    private List<ViewData> list;
    private ImageArrayAdapter adapter;
    private ListView lV;
    
    
    @Override protected int getLayoutResID() {
        return R.layout.activity_ylist;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
                    ImgName = LibC.defaultImgDirPath() + ImgName;
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
        adapter = new ImageArrayAdapter(this, R.layout.list_image_item, list);
        
        lV = (ListView) findViewById(R.id.listview);
        lV.setAdapter(adapter);
    }
    
}
