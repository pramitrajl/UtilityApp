package au.edu.jcu.assessment.utilityapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * State class for Settings preferences.
 */
data class SettingsUiState(
    val selectedTheme: String = "System Default",
    val selectedLanguage: String = "English",
    val statusMessage: String? = null
)

/**
 * ViewModel managing app theme and language preferences.
 */
class SettingsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun setTheme(theme: String) {
        _uiState.update {
            it.copy(
                selectedTheme = theme,
                statusMessage = "Theme updated to $theme"
            )
        }
    }

    fun setLanguage(language: String) {
        _uiState.update {
            it.copy(
                selectedLanguage = language,
                statusMessage = "Language updated to $language"
            )
        }
    }

    fun clearStatusMessage() {
        _uiState.update { it.copy(statusMessage = null) }
    }

    /**
     * Live translation helper for app UI labels.
     */
    fun getString(key: String): String {
        return when (_uiState.value.selectedLanguage) {
            "Spanish" -> when (key) {
                "title" -> "Convertidor de Divisas"
                "amount" -> "Monto"
                "from" -> "De"
                "to" -> "A"
                "result" -> "Resultado Convertido"
                "settings" -> "Ajustes"
                "theme" -> "Tema de la Aplicación"
                "app_language" -> "Idioma de la Aplicación"
                "about" -> "Acerca del Convertidor"
                "fetching" -> "Obteniendo tipos de cambio en vivo..."
                else -> key
            }
            "French" -> when (key) {
                "title" -> "Convertisseur de Devises"
                "amount" -> "Montant"
                "from" -> "De"
                "to" -> "À"
                "result" -> "Résultat Converti"
                "settings" -> "Paramètres"
                "theme" -> "Thème de l'application"
                "app_language" -> "Langue de l'application"
                "about" -> "À propos du Convertisseur"
                "fetching" -> "Obtention des taux en direct..."
                else -> key
            }
            "German" -> when (key) {
                "title" -> "Währungsumrechner"
                "amount" -> "Betrag"
                "from" -> "Von"
                "to" -> "Nach"
                "result" -> "Umgerechnetes Ergebnis"
                "settings" -> "Einstellungen"
                "theme" -> "App-Design"
                "app_language" -> "App-Sprache"
                "about" -> "Über den Umrechner"
                "fetching" -> "Live-Wechselkurse werden abgerufen..."
                else -> key
            }
            "Japanese" -> when (key) {
                "title" -> "通貨コンバーター"
                "amount" -> "金額"
                "from" -> "変換元"
                "to" -> "変換先"
                "result" -> "変換結果"
                "settings" -> "設定"
                "theme" -> "アプリのテーマ"
                "app_language" -> "アプリの言語"
                "about" -> "通貨コンバーターについて"
                "fetching" -> "リアルタイム為替レートを取得中..."
                else -> key
            }
            else -> when (key) { // English default
                "title" -> "Currency Converter"
                "amount" -> "Amount"
                "from" -> "From"
                "to" -> "To"
                "result" -> "Converted Result"
                "settings" -> "Settings"
                "theme" -> "App Theme"
                "app_language" -> "App Language"
                "about" -> "About Currency Converter"
                "fetching" -> "Fetching live exchange rates..."
                else -> key
            }
        }
    }
}