package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library;



public class Consts {
    private Consts(){}
    
    public static final int LIMIT_Category = 4;
    public static final int LIMIT_Lists = 16;
    public static final int LIMIT_Items = 5;
    
    public static final int Item_num = 3;
    public static final int LIMIT_histry = 10;
    
    // 画像などのファイルの保管場所のパス
    public static String rootPath;    // context.getFilesDir().getPath() + "/"
    public static String libraryRootPath;    // context.getFilesDir().getPath() + "/YList/"
    public static String categoryName, listName;   // 末尾に "/" を加えることを忘れないように。
    public static int ItemNumber;   // アイテムのファイル名は通し番号で管理するので、識別子は整数。
    
}
