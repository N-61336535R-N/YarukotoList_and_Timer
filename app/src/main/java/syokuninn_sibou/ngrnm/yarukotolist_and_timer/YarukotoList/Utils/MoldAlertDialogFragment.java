package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

/**
 * Created by M.R on 2017/06/27.
 */

public final class MoldAlertDialogFragment extends DialogFragment {
    
    /**
     private void makeADialog(String title, String message) {
         DialogFragment fragment = new MoldAlertDialogFragment();
         Bundle bundle = new Bundle();
         bundle.putString("title", title);
         bundle.putString("message", message);
         fragment.setArguments(bundle);
         FragmentTransaction ft = getFragmentManager().beginTransaction();
         fragment.show(ft, "dialog");
     }
     ↑こんな感じで使う。
 
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //fragment.show(ft, "dialog"); のタイミングで、Fragment本体が拾いに来るイメージ
        return new AlertDialog
                .Builder(getContext())
                .setTitle(getArguments().getString("title"))
                .setMessage(getArguments().getString("message"))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Okボタンを押した時の処理。何もしないver
                    }
                })
                .create();
    }
    
}