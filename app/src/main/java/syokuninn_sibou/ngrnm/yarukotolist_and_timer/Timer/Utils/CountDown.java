package syokuninn_sibou.ngrnm.yarukotolist_and_timer.Timer.Utils;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import syokuninn_sibou.ngrnm.yarukotolist_and_timer.R;

import static android.support.v7.appcompat.R.styleable.CompoundButton;

/**
 * Created by M.R on 2017/04/08.
 */

public class CountDown extends CountDownTimer {
    boolean show_ms=false;
    String time_txt;
    private TextView timerText;
    
    private long millisLeft;
    
    public CountDown(long millisInFuture, long countDownInterval, TextView timerText) {
        super(millisInFuture, countDownInterval);
        this.timerText = timerText;
        if (millisInFuture <= 30000) {
            show_ms = true;
        }
    }
    public CountDown(long millisInFuture, long countDownInterval, TextView timerText, boolean show_ms) {
        super(millisInFuture, countDownInterval);
        this.timerText = timerText;
        this.show_ms = show_ms;
    }

    @Override
    public void onFinish() {
        // 完了
        timerText.setText("00:00.00 ms");
        show_ms=false;
    }

    // インターバルで呼ばれる
    @Override
    public void onTick(long millisUntilFinished) {
        // 残り時間を分、秒、ミリ秒に分割
        long mm = millisUntilFinished / 1000 / 60;
        long ss = millisUntilFinished / 1000 % 60;
        if (show_ms) {
            long ms = (millisUntilFinished - ss * 1000 - mm * 1000 * 60) / 10;
            time_txt = String.format("%1$02d:%2$02d.%3$02d ms", mm, ss, ms);
        } else {
            if (mm == 0 && ss <= 30) {
                show_ms = true;
            }
            time_txt = String.format("%1$02d:%2$02d s", mm, ss);
        }
        timerText.setText(time_txt);
    
        millisLeft = millisUntilFinished;
    }
    
    
    public long getMillisLeft() {
        return millisLeft;
    }
}
