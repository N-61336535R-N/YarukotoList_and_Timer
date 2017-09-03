package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Items;

/**
 * Created by M.R on 2017/08/01.
 */

public abstract class Checker {
    public Checker() {
        determPath();
    }
    
    protected abstract void determPath();
    
    protected abstract void check();
    
    protected abstract void ReflectUpdate();
}
