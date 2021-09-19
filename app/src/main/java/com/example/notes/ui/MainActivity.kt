package com.example.notes.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.noteList.NotesListFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKAuthException


class MainActivity : AppCompatActivity(), OnNoteClickedListener {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        router = Router(supportFragmentManager)

        if (savedInstanceState == null) {
            if (isAuthorized() || VK.isLoggedIn()) {
                router.showNotesList()
            } else {
                router.showAuthFragment()
            }
        }
    }

    override fun onNoteOnClicked(note: Note, position: Int) {
        if (!resources.getBoolean(R.bool.isLandScape)) {
            router.showNoteDetail(note, position)
        }
    }

    private fun isAuthorized(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(this) != null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, NotesListFragment())
                    .commitAllowingStateLoss()
            }

            override fun onLoginFailed(authException: VKAuthException) {
                if (!authException.isCanceled) {
                    val descriptionResource =
                        if (authException.webViewError == WebViewClient.ERROR_HOST_LOOKUP) R.string.message_connection_error
                        else R.string.message_unknown_error
                    AlertDialog.Builder(this@MainActivity)
                        .setMessage(descriptionResource)
                        .setPositiveButton(R.string.vk_retry) { _, _ ->
                            VK.login(
                                this@MainActivity,
                                arrayListOf(VKScope.WALL, VKScope.PHOTOS)
                            )
                        }
                        .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}