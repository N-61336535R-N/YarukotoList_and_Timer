package syokuninn_sibou.ngrnm.yarukotolist_and_timer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.Consts;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.DirFile;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.YCategoryActivity;

/**
 * Created by ryo on 2017/09/03.
 */

public class MainActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ycategory);
    
        /* ルートフォルダを、Consts に登録 */
        Consts.rootPath = getApplicationContext().getFilesDir().getPath() + "/";
        // DirFile の初期化（ルートディレクトリの設定）
        DirFile.setDirFile(Consts.rootPath);
    
    
    
        // やることリスト（編集）画面に移動
        Intent intent = new Intent(MainActivity.this, YCategoryActivity.class);
        startActivity(intent);
    
    
    
        finish();
    }
    
}
