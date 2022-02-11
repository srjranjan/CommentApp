package com.srj.commentapp

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment(), ServiceGenerator {

    lateinit var progressBar: ProgressBar

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tv_signIn = view.findViewById<TextView>(R.id.tv_signIn)
        val et_email = view.findViewById<TextInputEditText>(R.id.Email)
        val et_password = view.findViewById<TextInputEditText>(R.id.password)
        val et_secretCode = view.findViewById<TextInputEditText>(R.id.secretCode)
        val emailBox = view.findViewById<TextInputLayout>(R.id.textInputLayout)
        val passBox = view.findViewById<TextInputLayout>(R.id.textInputLayout2)
        val secretBox = view.findViewById<TextInputLayout>(R.id.textInputLayout3)
        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        tv_signIn.setOnClickListener {
            navigateToSignIn()
        }

        val btn_signIn = view.findViewById<Button>(R.id.btn_signup)

        btn_signIn.setOnClickListener {
            submitSignUp(et_email, et_password, et_secretCode, emailBox, passBox, secretBox)
        }
    }

    private fun submitSignUp(
        et_email: TextInputEditText,
        et_password: TextInputEditText,
        et_secretCode: TextInputEditText,
        emailBox: TextInputLayout,
        passBox: TextInputLayout,
        secretBox: TextInputLayout
    ) {
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        val secretCode = et_secretCode.text.toString()
        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {

                if (secretCode.isNotEmpty()) {
                    sendtoserver(email, password, secretCode)


                } else {
                    secretBox.boxStrokeErrorColor = ColorStateList.valueOf(0xFF0000)
                }
            } else {
                passBox.boxStrokeErrorColor = ColorStateList.valueOf(0xFF0000)
            }


        } else {
            emailBox.boxStrokeErrorColor = ColorStateList.valueOf(0xFF0000)

        }

    }

    private fun sendtoserver(email: String, password: String, secretCode: String) {
        val service = retrofit.create(APIservice::class.java)
        val json = JSONObject()
        json.put("email", email)
        json.put("password", password)
        json.put("secretCode", secretCode)

        val jsonOBjectString = json.toString()
        val requestBody = jsonOBjectString.toRequestBody("application/json".toMediaTypeOrNull())
        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            try {
                val response = service.signup(requestBody)
                // progressBar.visibility = View.VISIBLE

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(view?.context, "Submitted", Toast.LENGTH_SHORT).show()
                        val directions =
                            SignupFragmentDirections.actionSignupFragmentToHomeFragment()
                        findNavController().navigate(directions)
                    } else {
                        Toast.makeText(view?.context, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                Log.e("mirror", e.message!!)
            }
        }


        //val response = service.signup(requestBody)


    }

    private fun navigateToSignIn() {
        val directions = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
        findNavController().navigate(directions)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}