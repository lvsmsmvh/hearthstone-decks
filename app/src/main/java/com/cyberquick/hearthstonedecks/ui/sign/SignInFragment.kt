package com.cyberquick.hearthstonedecks.ui.sign

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.cyberquick.hearthstonedecks.R
import com.firebase.ui.auth.AuthUI

class SignInFragment: Fragment(R.layout.fragment_signin) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

// Create and launch sign-in intent
        requireActivity().startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

}