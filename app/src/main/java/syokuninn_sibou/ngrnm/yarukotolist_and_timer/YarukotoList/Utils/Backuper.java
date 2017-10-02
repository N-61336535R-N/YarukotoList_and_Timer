package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Utils;

import java.io.FileNotFoundException;
import java.io.FileReader;

import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.Consts;

/**
 * Created by ryo on 2017/10/01.
 */

public class Backuper {
    private String originPath = Consts.rootPath;
    private String destiPath;
    
    public Backuper(String destiPath) throws FileNotFoundException {
        FileReader r = new FileReader(destiPath);
        this.destiPath = destiPath;
    }
    
    public void backup() {
        // originPath 以下の全フォルダを zip圧縮
        
        // destiPath に保存する 
        
    }
}
