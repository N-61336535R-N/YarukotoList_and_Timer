package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Library.Consts;

/**
 * Created by ryo on 2017/10/01.
 */

public class Backuper {
    private File originF = new File(Consts.libraryRootPath);
    private File destiF;
    private ZipCompressUtils zipCU = new ZipCompressUtils();
    
    public Backuper(String destiPath) throws FileNotFoundException {
        this.destiF = new File(destiPath);
        if (!destiF.exists()) {
            throw new FileNotFoundException();
        }
    }
    
    public void backup() throws Exception {
        //Calendarクラスのオブジェクトを生成する
        Calendar cl = Calendar.getInstance();
    
        //フォーマットを指定する
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        System.out.println(sdf.format(cl.getTime()));
    
        // originF 以下の全フォルダを zip圧縮
        // destiF に保存する 
        zipCU.compressFiles(Arrays.asList(originF.listFiles()), destiF.getPath()+"/BackupYLibrarys["+sdf.format(cl.getTime())+"].zip");
        
    }
    
    
}
