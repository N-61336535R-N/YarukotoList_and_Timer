package syokuninn_sibou.ngrnm.yarukotolist_and_timer.Settings;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import java.io.FileNotFoundException;

import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Utils.Backuper;
import syokuninn_sibou.ngrnm.yarukotolist_and_timer.YarukotoList.Utils.MoldAlertDialogFragment;


/**
 * int SettingConsts.setPref_xmls[SettingConsts.getPref_num()]
 * によって、移動する設定画面を切り替えてる。
 */

public class SettingFragment extends PreferenceFragmentCompat {
    
    private OnFragmentInteractionListener mListener;
    
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(SettingConsts.setPref_xmls[SettingConsts.getPref_num()], rootKey);
    
        if (SettingConsts.getPref_num() == SettingConsts.KOUMOKU_num-1) {
            setBackupButton();
        }

    }
    
    private void setBackupButton() {
        // PreferenceScreenからのIntent
        PreferenceScreen prefScreen = (PreferenceScreen)findPreference("prefKey_goBackup");
        prefScreen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
            
                SharedPreferences sharedPrefs = preference.getSharedPreferences();
                String destiPath = sharedPrefs.getString("prefKey_goBackup", "Error");
                if (destiPath.equals("Error")) {
                    //ダイアログを表示
                    String title = "バックアップ失敗",
                            message = "バックアップ先を指定してください。";
                    makeADialog(title, message);
                    // Toast を表示
                    Toast.makeText(getContext(), "バックアップ失敗", Toast.LENGTH_LONG).show();
                    return false;
                }
            
                Backuper backuper;
                try {
                    backuper = new Backuper(destiPath);
                } catch (FileNotFoundException e) {
                    //ダイアログを表示
                    String title = "バックアップ失敗",
                            message = "バックアップ先が見つかりませんでした。";
                    makeADialog(title, message);
                    // Toast を表示
                    Toast.makeText(getContext(), "バックアップ失敗", Toast.LENGTH_LONG).show();
                    return false;
                }
            
                backuper.backup();
                return true;
            }
        });
    }
    
    private void makeADialog(String title, String message) {
        DialogFragment fragment = new MoldAlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        fragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        fragment.show(ft, "dialog");
    }
    
    
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    
    
    
    
    public interface OnFragmentInteractionListener {}
}
