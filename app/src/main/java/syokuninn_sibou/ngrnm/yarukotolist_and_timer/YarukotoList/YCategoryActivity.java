package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import syokuninn_sibou.ngrnm.yarukotolist_and_timer.R;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.AddImage;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.Consts;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.Grids.GridAdapter;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.LibraryChecker;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.Lists.ViewData;



/**
 *  ＜＜＜＜＜〜〜〜〜〜  やることカテゴリ一覧（独立画面ver）  〜〜〜〜〜＞＞＞＞＞
 * やることリストの「カテゴリタイル（カテゴリ選択）画面」
 * タイマーへは、タイマーセット画面へのリンクのみ
 * 
 * 
 * ■ VDatas が用意できれば、画面描画はできる。
 */
public class YCategoryActivity extends YLibraryActivity {
    private static final String kind = "Category";
    @Override protected String getKind() {
        return kind;
    }
    
    private LibraryChecker LibC;
    @Override protected LibraryChecker getLibC() {
        return LibC;
    }
    @Override protected void setLibC(LibraryChecker LibC) {
        this.LibC = LibC;
    }
    
    @Override protected AdapterView getAdptrView() {
        return gV;
    }
    
    
    private ViewData[] VDatas;
    GridView gV;
    
    
    @Override protected int getLayoutResID() {
        return R.layout.activity_ycategory;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // gVのボタンの設定
        gV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*                String message = position + "番目が選択されました。";
                Toast.makeText(YCategoryActivity.this, message, Toast.LENGTH_LONG).show();*/
                
                // 選択された「リストの内容を保存したファイル」の場所（名前）を取得
    
                /** （未）未実装。
                 *  ● 「リストの内容を保存したファイル」の場所を、YListerActivityの intent に引数として渡す。
                 *  ● 渡された先(YListerActivity)で、「読み込み中」のメッセージを表示
                 *  ● 
                 */
                
                // やることリスト（編集）画面に移動
                Intent intent = new Intent(YCategoryActivity.this, YListerActivity.class);
                //intent.putExtra("ListPath", List_Datas[1][position]);
                // Stack に、ディレクトリ名のみ保存
                Consts.libraryName.add( VDatas[position].getTitle() );
                //ntent.putExtra("CategoryName", VDatas[position].title);
                startActivity(intent);
            }
        });
        
    }
    
    @Override
    protected void updateList() {
        // デフォルト画像が準備できてない（No_Image.pngがない）場合は、
        // セッティングし直す。
        String NoImage = LibC.defaultImgDirPath() + "No_Image.png";
        if (!new File(NoImage).exists()) {
            setAllImage("No_Image", LibC.defaultImgDirPath());
        }
    
        // GridView に表示する項目の登録
        int test = LibC.getSize();
        VDatas = new ViewData[test];
        for (int i = 0; i< LibC.getSize(); i++) {
            VDatas[i] = new ViewData(NoImage, LibC.getNames().get(i));
        }
        
        // GridViewのインスタンスを生成
        gV = (GridView) findViewById(R.id.gridview);
        // BaseAdapter を継承したGridAdapterのインスタンスを生成
        // 子要素のレイアウトファイル grid_items.xml を activity_ycategory.xmlry.xml に inflate するためにGridAdapterに引数として渡す
        GridAdapter adapter = new GridAdapter(this.getApplicationContext(), R.layout.grid_items, VDatas);
        // gVにadapterをセット
        gV.setAdapter(adapter);
    }
    
    
    
    public void setAllImage(String ImDir, String outPath) {
        // アセットの defaultImage をエンコードしてコピー
        AssetManager assetManager = getResources().getAssets();
        String[] fileList = null;
        InputStream input = null;
        FileOutputStream output = null;
        try {
            // ファイルリストを作成する
            fileList = assetManager.list(ImDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        boolean isClose = false;
        for(int i=0; i< fileList.length ; i++){
            try {
                input = assetManager.open(ImDir + "/" + fileList[i]);
                // 保存先のパス
                output = new FileOutputStream(new File(outPath + fileList[i]));
                
                if (i == fileList.length-1) { isClose = true; }
                AddImage.add(input, output, isClose);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
