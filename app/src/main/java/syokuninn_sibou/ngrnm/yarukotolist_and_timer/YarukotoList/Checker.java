package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList;

/**
 * Created by M.R on 2017/08/01.
 */

public abstract class Checker {
    
    public Checker(String mode) {
        determPath(mode);
        check();
    }
    protected abstract void determPath(String mode);
    protected abstract void check();
    
    
    protected abstract void ReflectUpdate();
}
