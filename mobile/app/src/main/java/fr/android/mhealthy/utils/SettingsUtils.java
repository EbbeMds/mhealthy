package fr.android.mhealthy.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.preference.PreferenceManager;

import java.util.Arrays;
import java.util.Locale;

import fr.android.mhealthy.AuthenticationActivity;
import fr.android.mhealthy.R;

public class SettingsUtils {
    public static void setup_defaults(Activity act) {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(act);
        // Getting defined language
        // Otherwise we check system default language
        // And in worst case we default to en
        String default_lang_sys = Locale.getDefault()
                .getLanguage();
        String lang = p.getString(
                "language_preference",
                Arrays.asList(act.getResources().getStringArray(R.array.language_values)).contains(default_lang_sys)
                        ? default_lang_sys : "en"
        );
        if (!act.getResources().getConfiguration()
                .getLocales().get(0).getLanguage().equals(lang)) {
            change_lang(act, lang);
        }
    }

    private static void change_lang(Activity act, String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = act.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(act, act.getClass());
        act.finish();
        act.startActivity(refresh);
    }

    public static void restart_on_change(Activity act) {
        AlertDialog dialog = new AlertDialog.Builder(act)
                .setTitle(act.getString(R.string.confirm))
                .setMessage(act.getString(R.string.conf_change_restart))
                .setPositiveButton(act.getString(R.string.yes), (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(act, AuthenticationActivity.class);
                    act.startActivity(intent);
                    act.finishAffinity();
                })
                .setNegativeButton(act.getString(R.string.no), (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .create();
        dialog.show();
    }
}
