package fabricio.jefferson.npd_projeto.ui.setting

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import fabricio.jefferson.npd_projeto.R
import fabricio.jefferson.npd_projeto.common.Constants
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    private val sp: SharedPreferences by lazy {
        getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        updateUi()

        btnSave.setOnClickListener { save() }
    }

    private fun updateUi(){
        val isCelsius = sp.getBoolean(Constants.PREFS_LANG, true)
        val isEnglish = sp.getBoolean(Constants.PREFS_TEMP, true)

        rGroupTemperature.check( if (isCelsius) R.id.rBtnCelsius else R.id.rBtnFahrenheit )
        rGroupLanguage.check( if (isEnglish) R.id.rBtnEnglish else R.id.rBtnPortuguese )
    }

    private fun save(){
        sp.edit {
            putBoolean(Constants.PREFS_LANG, rBtnCelsius.isChecked)
            putBoolean(Constants.PREFS_TEMP, rBtnEnglish.isChecked)
        }
        Toast.makeText(this, R.string.msgSaved, Toast.LENGTH_LONG).show()
        finish()
    }
}
