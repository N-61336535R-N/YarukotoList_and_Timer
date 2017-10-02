package syokuninn_sibou.ngrnm.yarukotolist_and_timer.Settings;

import syokuninn_sibou.ngrnm.yarukotolist_and_timer.R;

/**
 * Created by ryo on 2017/09/30.
 */

public class SettingConsts {
    
    private SettingConsts(){}
    
    // 設定 のカテゴリ一覧
    static final String[] scenes = {
            // Scenes of Isle of Wight
            "表示",
            "構造",
            "全てバックアップ"
    };
    
    // ちょっと冗長的ですが分かり易くするために
    static final int[] icons = {
            R.drawable.ic_build_black_24dp,
            R.drawable.ic_build_black_24dp,
            R.drawable.ic_build_black_24dp,
    };
    
    // preference.xml の一覧
    static final int[] setPref_xmls = {
            R.xml.setting_p1_hyouzi,
            R.xml.setting_p2_kouzou,
            R.xml.setting_p8_backup,
    };
    
    private static int pref_num;
    public static int getPref_num() {
        return pref_num;
    }
    public static void setPref_num(int pref_num) {
        SettingConsts.pref_num = pref_num;
    }
    public static final int KOUMOKU_num = scenes.length;
    
}
