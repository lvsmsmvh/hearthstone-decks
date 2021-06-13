package com.cyberquick.hearthstonedecks.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth

fun logAuth(string: String) = Log.i("tag_auth", string + "")

class SplashActivity: AppCompatActivity() {

    private var handlerFinishingActivity: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (proceedEmailLink(intent)) return

        if (FirebaseAuth.getInstance().currentUser == null)
            goToSignInActivity()
        else
            goToMainActivityAndFinish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        cancelFinishingActivity()
        proceedEmailLink(intent)
    }

    private fun proceedEmailLink(intent: Intent?): Boolean {
        intent ?: return false

        val link = intent.data?.toString()
        if (link != null && AuthUI.canHandleIntent(intent)) {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setEmailLink(link)
                    .setAvailableProviders(getAvailableProviders())
                    .build(), RC_SIGN_IN)
            return true
        }

        return false
    }

    private fun goToSignInActivity() {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(getAvailableProviders())
                .build(), RC_SIGN_IN)
    }

    private fun goToMainActivityAndFinish() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getAvailableProviders(): ArrayList<AuthUI.IdpConfig> {
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setAndroidPackageName(packageName, true, null)
            .setHandleCodeInApp(true)
            .setUrl("https://decksforhearthstone.page.link")
            .build()

        return arrayListOf(AuthUI.IdpConfig.EmailBuilder().enableEmailLinkSignIn()
                .setActionCodeSettings(actionCodeSettings).build())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                goToMainActivityAndFinish()
            } else {
                finishWithDelayOneSecond()
            }
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }

    private fun finishWithDelayOneSecond() {
        if (handlerFinishingActivity == null)
            handlerFinishingActivity = Handler(Looper.getMainLooper())

        handlerFinishingActivity?.postDelayed({ if (!isFinishing) finish() }, 1000)
    }

    private fun cancelFinishingActivity() {
        handlerFinishingActivity?.removeCallbacksAndMessages(null)
    }

    companion object {
        private const val RC_SIGN_IN = 12424
    }
}