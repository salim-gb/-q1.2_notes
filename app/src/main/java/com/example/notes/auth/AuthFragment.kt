package com.example.notes.auth

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import com.example.notes.R
import com.example.notes.ui.Router
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope


class AuthFragment : Fragment(R.layout.fragment_auth) {

    private lateinit var router: Router

    private val launcher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        val accountTask: Task<GoogleSignInAccount> =
            GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if (accountTask.isSuccessful) {
            router.showNotesList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        router = Router(parentFragmentManager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(requireContext(), signInOptions)

        view.findViewById<SignInButton>(R.id.btn_sign_in).setOnClickListener {
            launcher.launch(client.signInIntent)
        }

        view.findViewById<Button>(R.id.vk_sign_in_btn).setOnClickListener {
            VK.login(requireActivity(), arrayListOf(VKScope.WALL, VKScope.PHOTOS))
        }
    }
}