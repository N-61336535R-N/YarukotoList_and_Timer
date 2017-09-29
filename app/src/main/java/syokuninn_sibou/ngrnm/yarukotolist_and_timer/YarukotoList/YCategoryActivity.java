package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.*;

import syokuninn_sibou.ngrnm.yarukotolist_and_timer.R;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.Lists.ViewData;



/**
 *  ＜＜＜＜＜〜〜〜〜〜  やることカテゴリ一覧（独立画面ver）  〜〜〜〜〜＞＞＞＞＞
 * やることリストの「カテゴリタイル（カテゴリ選択）画面」
 * タイマーへは、タイマーセット画面へのリンクのみ
 * 
 * 
 * ■ VDatas が用意できれば、画面描画はできる。
 */
public class YCategoryActivity extends LibraryActivity {
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
    
    @Override protected AdapterView getAView() {
        return gV;
    }
    
    @Override protected Context getThisActivity() {
        return YCategoryActivity.this;
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
    protected void updateListView() {
        // デフォルト画像が準備できてない（No_Image.pngがない）場合は、
        // セッティングし直す。
        String NoImage = LibC.defaultImgDirPath() + "No_Image.png";
        if (!new File(NoImage).exists()) {
            setAllImage("No_Image", LibC.defaultImgDirPath());
        }
    
        // GridView に表示する項目の登録
        int test = LibC.getKind_num();
        VDatas = new ViewData[test];
        for (int i = 0; i< LibC.getKind_num(); i++) {
            VDatas[i] = new ViewData(NoImage,  LibC.getNames().get(i));
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
    
    
    /*  GridView の生成の諸々  */
    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
    
    // ArrayAdapter<ViewData> を継承した GridAdapter クラスのインスタンス生成
    private class GridAdapter extends ArrayAdapter<ViewData> {
        private LayoutInflater inflater;
        private int layoutId;
    
        public GridAdapter(Context context, int layoutId, ViewData[] objects) {
            super(context, 0, objects);
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layoutId = layoutId;
        }
    
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            
            if (convertView == null) {
                // main.xml の <GridView .../> に grid_items.xml を inflate して convertView とする
                convertView = inflater.inflate(layoutId, parent, false);
                // ViewHolder を生成
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.textview);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            ViewData data = getItem(position);
            holder.textView.setText(data.getTitle());
            Bitmap bmp = BitmapFactory.decodeFile(data.getImagePath());
            holder.imageView.setImageBitmap(bmp);
        
            return convertView;
        }
    }
}
