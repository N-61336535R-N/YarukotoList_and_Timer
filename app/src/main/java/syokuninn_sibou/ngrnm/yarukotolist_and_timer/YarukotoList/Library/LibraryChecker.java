package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by M.R on 2017/06/04.
 */

public class LibraryChecker {
    private List<String> Names, ImgNames;
    private String kind;  // "Category" or "Lists" or "Item"
    private int LIMIT_kind;
    private int kind_num;
    
    private String kindDirPath;
    private String imgDirPath;
    
    public LibraryChecker(String kind) {
        // rootDir には、context.getFilesDir().getPath() + "/カテゴリ名" + "/リスト名"
        // (今いる位置（階層）のタイトルまでを、rootDirの末尾に追加して渡す。)
        this.kind = kind;
        switch (kind) {
            case "Category":
                LIMIT_kind = Consts.LIMIT_Category;
                kindDirPath = Consts.libraryRootPath;
                break;
            case "Lists":
                LIMIT_kind = Consts.LIMIT_Lists;
                kindDirPath = Consts.libraryRootPath + Consts.categoryName + "/";
                break;
        }
        this.imgDirPath = Consts.rootPath + kind + "_imgs/";
    }
    
    
    public boolean check() {
        File kindDir = new File(kindDirPath);
        File checkF;
        // サムネイル保存ディレクトリを作成する。
        if (!(checkF = new File(imgDirPath)).exists()) {
            checkF.mkdir();
        }
        
        /*
         * [kind].list が
         * 存在する
         *      → 読み込みを開始
         * 存在しない
         *      → 作る
         */  
        boolean problem=false;
        checkF =  new File(kindDirPath + kind + ".list");
        if( ! checkF.exists() ) {
            kindDir.mkdirs();
            try {
                checkF.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /**
             * Category.list が存在しない ＝  なんらかの事情でデータが消失した or 初回起動
             * Category.list がないとどうしようもないので、
             * ok●まず、Category.list を作る。
             * ●初回起動の説明ページを表示。
             */
            /**
             *  ok●すでにディレクトリが存在しているのなら、
             *      ディレクトリの名前をリストに組み込む
             *      ※ 「データベースが破損しました。修復を試みますか？」
             *          というメッセージのあと、同意を得てからする。
             */
            List<String> dirList;
            int dirNum;
            StringBuilder sb = new StringBuilder();
            if ( ( dirNum= (dirList=getDirList(kindDir) ).size() ) > 0 ) {
                if (dirNum > LIMIT_kind) {
                    kind_num = LIMIT_kind;
                } else {  kind_num=dirNum;  }
                for (int i=0; i<kind_num; i++) {
                    sb.append(dirList.get(i));
                    sb.append(",No_Image");
                    if (i != kind_num-1) sb.append("\n");
                }
            } else {
                sb.append(kind+"名"+",No_Image");
            }
            syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.DirFile.writeAll(checkF, sb.toString());
    
            // カテゴリーの階層でtrueになった場合は、●初回起動の説明ページを表示。
            problem = true;
        }
        
        
        /*
         * ○kind+".list "に書いてある [kindの要素の名前のついたディレクトリ] が全て存在しているかを確認。
         * ○なければ作る
         */
        // kind+".list " を読み込み
        String[] List_checkDir = syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.DirFile.readAll(checkF).split("\n", -1);
        kind_num = List_checkDir.length;
        Names = new ArrayList<>();
        ImgNames = new ArrayList<>();
        for(String categ : List_checkDir) {
            Names.add( categ.split(",", -1)[0] );
            ImgNames.add( categ.split(",", -1)[1] );
        }
        // ディレクトリがなければ作る
        for(int i=0; i<kind_num; i++) {
            if ( !( checkF = new File(kindDirPath+ Names.get(i)) ).exists() ) {
                checkF.mkdir();
            }
        }
    
        return problem;
    }
    
    
    private static List<String> getDirList(File Dir) {
        File[] dirs = Dir.listFiles();
        List<String> dirList = new ArrayList<>();
        
        for (File dir : dirs) {
            if (dir.isDirectory()) {
                dirList.add(dir.getName());
            }
        }
        return dirList;
    }
    
    

    private void update_listF() {
        File checkF =  new File(kindDirPath + kind + ".list");
        StringBuilder sb = new StringBuilder();
    
        for (int i=0; i<kind_num; i++) {
            sb.append(Names.get(i)+","+ImgNames.get(i));
            if (i != kind_num-1) sb.append("\n");
        }
        syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.DirFile.writeAll(checkF, sb.toString());
    }
    
    // 新しいライブラリ（カテゴリ、リスト）を作成
    public boolean makeNewLibrary(String ItemName, String ItemImageName) {
        if (Names.size() + 1 < LIMIT_kind) {
            Names.add(ItemName);
            ImgNames.add(ItemImageName);
            kind_num++;
            // Items.list を更新
            update_listF();
            // ディレクトリ作成
            new File(kindDirPath + Names.get(kind_num-1)).mkdir();
            return true;
        } else {  return false;  }
    }
    
    // 選択したライブラリ（カテゴリ、リスト）を削除
    public void removeLibrary(int posi) {
        // ディレクトリ削除
        delete( new File(kindDirPath + Names.get(posi)) );
        Names.remove(posi);
        ImgNames.remove(posi);
        kind_num--;
        // Items.list を更新
        update_listF();
    }
    
    private static void delete(File f) {
         // ファイルまたはディレクトリが存在しない場合は何もしない
        if(f.exists() == false) {  return;  }
        
        if(f.isFile()) {
             // ファイルの場合は削除する
            f.delete();
        } else if(f.isDirectory()){
             // ディレクトリの場合は、すべてのファイルを削除する
 
             // 対象ディレクトリ内のファイルおよびディレクトリの一覧を取得
            File[] files = f.listFiles();
 
             // ファイルおよびディレクトリをすべて削除
            for(int i=0; i<files.length; i++) {
                 // 自身をコールし、再帰的に削除する
                delete( files[i] );
            }
             // 自ディレクトリを削除する
            f.delete();
        }
    }

    
    
    public  List<String>getNames() {
        return Names;
    }
    public List<String> getImgNames() {
        return ImgNames;
    }
    public int getKind_num() {
        return kind_num;
    }
    public String ImgDirPath() {
        return imgDirPath;
    }
}
