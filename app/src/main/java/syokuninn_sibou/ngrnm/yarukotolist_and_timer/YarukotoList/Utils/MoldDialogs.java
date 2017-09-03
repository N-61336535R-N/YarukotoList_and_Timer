package syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

/**
 * Created by M.R on 2017/06/27.
 */

public class MoldDialogs {
    public static AlertDialog.Builder makeBaseDialog(Context context, final String message) {
        // テキスト入力用Viewの作成
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(message);
        return dialog;
    }
    public static AlertDialog.Builder makeBaseInputDialog(Context context, final String message, final EditText editView) {
        // テキスト入力用Viewの作成
        AlertDialog.Builder dialog = makeBaseDialog(context, message);
        editView.setInputType(InputType.TYPE_CLASS_TEXT);
        dialog.setView(editView);
        return dialog;
    }
    public static void setSimpleOKButton(AlertDialog.Builder dialog) {
        // OKボタンの設定
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Okボタンを押した時の処理。何もしないver
            }
        });
    }
    public static void setSimpleCancelButton(AlertDialog.Builder dialog) {
        // キャンセルボタンの設定
        dialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Okボタンを押した時の処理。何もしないver
            }
        });
    }
    
}
