package com.example.notes.ui

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.notes.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var router: Router
    private lateinit var firsName: TextView
    private lateinit var lastName: TextView
    private lateinit var image: ImageView
    private var personName: String? = null
    private var personGivenName: String? = null
    private var personFamilyName: String? = null
    private var personPhoto: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isAuthorized()) {
            val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
            if (acct != null) {
                personName = acct.displayName
                personGivenName = acct.givenName
                personFamilyName = acct.familyName
                val personEmail: String? = acct.email
                val personId: String? = acct.id
                personPhoto = acct.photoUrl
            }
        }

        if (VK.isLoggedIn()) {
            requestUsers()
        }

        router = Router(parentFragmentManager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.topAppBar)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        image = view.findViewById(R.id.image)
        firsName = view.findViewById(R.id.first_name)
        lastName = view.findViewById(R.id.last_name)

        firsName.text = personGivenName
        lastName.text = personFamilyName

        if (personPhoto != null) {
            Glide.with(requireContext())
                .load(personPhoto)
                .circleCrop()
                .error(R.drawable.user_placeholder)
                .into(image)
        } else {
            Glide.with(requireContext())
                .load(R.drawable.user_placeholder)
                .circleCrop()
                .into(image)
        }


        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(requireContext(), signInOptions)

        val singOut = view.findViewById<Button>(R.id.sign_out_btn)

        singOut.setOnClickListener {
            client.signOut().addOnCompleteListener {
                router.showAuthFragment()
            }

            VK.logout()
        }
    }

    private fun isAuthorized(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(requireContext()) != null
    }

    private fun requestUsers() {
        VK.execute(VKUsersCommand(), object : VKApiCallback<List<VKUser>> {
            override fun success(result: List<VKUser>) {
                if (result.isNotEmpty()) {
                    val user = result[0]

                    firsName.text = user.firstName
                    lastName.text = user.lastName

                    if (!TextUtils.isEmpty(user.photo)) {
                        Glide.with(requireContext())
                            .load(user.photo)
                            .circleCrop()
                            .error(R.drawable.user_placeholder)
                            .into(image)

                    } else {
                        image.setImageResource(R.drawable.user_placeholder)
                    }
                }
            }

            override fun fail(error: Exception) {

            }
        })
    }
}