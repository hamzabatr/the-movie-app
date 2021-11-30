package com.gmail.eamosse.imdb.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.preference.*
import com.gmail.eamosse.imdb.R

class SettingsFragment :
    PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // gérer le choix des langues
        // gérer le mode sombre grace a un switch
        // gérer les paramètres de notifications :
        //      - lorsque les notifications sont désaciver les paramètre de mode et de volume le sont aussi
        //      - lorsque le mode vibreur ou le mode silencieux est activé, le volume est a 0 est le slider ne peut pas etre modifier

        settings()
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        settings()
        return super.onPreferenceTreeClick(preference)
    }

    private fun settings() {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)

        val language = sp.getString("languages", "")
        val night_mode = sp.getBoolean("night_mode", true)
        val notifications = sp.getBoolean("notifications", true)
        val notifications_mode = sp.getString("notifications_mode", "")
        val volume = sp.getInt("volume_notifications", 0)

        if (night_mode == true) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        }

        findPreference<ListPreference>("notifications_mode")?.setEnabled(!notifications)
        findPreference<SeekBarPreference>("notifications_volume")?.setEnabled(
            !notifications && !(
                notifications_mode.equals(
                    getString(R.string.vibrate)
                ) || notifications_mode.equals(getString(R.string.silent))
                )
        )
    }
}
