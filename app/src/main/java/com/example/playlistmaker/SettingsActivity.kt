package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SharedPreferencesKeeper.initSharedPreferencesFromContext(this)

        binding.darkTheme.isChecked = SharedPreferencesKeeper.getDarkThemeSwitchState()
        binding.darkTheme.setOnClickListener {
            SharedPreferencesKeeper.setDarkThemeSwitchState(binding.darkTheme.isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (binding.darkTheme.isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        binding.settingsToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back)
        binding.settingsToolbar.setNavigationOnClickListener { finish() }

        binding.share.setOnClickListener {onShareButtonClick()}

        binding.support.setOnClickListener {onSupportButtonClick()}

        binding.userAgreement.setOnClickListener {onUserAgreementClick()}

    }

    private fun onShareButtonClick() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_screen_item_share_link))
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun onSupportButtonClick() {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.settings_screen_item_support_mailto)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_screen_item_support_subj))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_screen_item_support_message))
        }
        startActivity(Intent.createChooser(supportIntent, "Send email"))
    }

    private fun onUserAgreementClick() {
        val url = getString(R.string.settings_screen_item_user_agreement_url)
        val userAgreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(Intent.createChooser(userAgreementIntent, "Open URL"))
    }
}