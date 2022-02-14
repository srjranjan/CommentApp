package com.srj.commentapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout.END_ICON_CLEAR_TEXT
import com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
import com.srj.commentapp.databinding.FragmentSignupBinding
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
class SignupFragment : Fragment(), ServiceGenerator, Encryption {
    var secretKey = "R#Q9DtjzHMFen&C*"

    @RequiresApi(Build.VERSION_CODES.O)
    var encodedBase64Key: String = Encryption.encodeKey(secretKey)
    val SHARED_PREFS = "shared_prefs"

    val EMAIL_KEY = "email_key"

    val PASSWORD_KEY = "password_key"

    var sharedpreferences: SharedPreferences? = null


    private lateinit var binding: FragmentSignupBinding

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
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(layoutInflater)
        return (binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.Email.addTextChangedListener {
            binding.textInputLayout.endIconMode = END_ICON_CLEAR_TEXT

        }

        binding.tvSignIn.setOnClickListener {
            navigateToSignIn()
        }


        binding.btnSignup.setOnClickListener {

            submitSignUp()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    private fun submitSignUp() {
        deActivateIputation()

        val email = binding.Email.text.toString()
        var password = binding.password.text.toString()
        var secretCode = binding.secretCode.text.toString()

        when {
            email.isBlank() -> {
                binding.textInputLayout.boxStrokeErrorColor = ColorStateList.valueOf(R.color.red)
                binding.textInputLayout.error = "Please enter email"
                activateIputation()

                binding.Email.addTextChangedListener {

                    binding.textInputLayout.boxStrokeErrorColor =
                        ColorStateList.valueOf(R.color.teal_200)
                    binding.textInputLayout.error = null
                    binding.textInputLayout.endIconMode = END_ICON_CLEAR_TEXT

                }
            }
            password.isBlank() -> {
                binding.textInputLayout2.boxStrokeErrorColor = ColorStateList.valueOf(R.color.red)
                binding.textInputLayout2.error = "Please enter password"
                activateIputation()
                binding.password.addTextChangedListener {
                    binding.textInputLayout2.boxStrokeErrorColor =
                        ColorStateList.valueOf(R.color.teal_200)
                    binding.textInputLayout2.error = null
                    binding.textInputLayout2.endIconMode = END_ICON_PASSWORD_TOGGLE

                }
            }
            secretCode.isBlank() -> {
                binding.textInputLayout3.boxStrokeErrorColor = ColorStateList.valueOf(R.color.red)
                binding.textInputLayout3.error = "PLease enter secret code"
                activateIputation()
                binding.secretCode.addTextChangedListener {

                    binding.textInputLayout3.boxStrokeErrorColor =
                        ColorStateList.valueOf(R.color.teal_200)
                    binding.textInputLayout3.error = null
                    binding.textInputLayout3.endIconMode = END_ICON_PASSWORD_TOGGLE

                }
            }
            else -> {
                password = Encryption.encrypt(password, encodedBase64Key)
                secretCode = Encryption.encrypt(secretCode, encodedBase64Key)
                sendToServer(email, password, secretCode)
            }
        }


    }

    private fun deActivateIputation() {
        binding.textInputLayout.isEnabled = false
        binding.textInputLayout2.isEnabled = false
        binding.textInputLayout3.isEnabled = false
        binding.btnSignup.isEnabled = false
    }

    private fun activateIputation() {
        binding.textInputLayout.isEnabled = true
        binding.textInputLayout2.isEnabled = true
        binding.textInputLayout3.isEnabled = true
        binding.btnSignup.isEnabled = true
    }

    private fun sendToServer(email: String, password: String, secretCode: String) {
        val service = retrofit.create(APIservice::class.java)
        val json = JSONObject()
        json.put("email", email)
        json.put("password", password)
        json.put("secretCode", secretCode)

        val jsonOBjectString = json.toString()
        val requestBody = jsonOBjectString.toRequestBody("application/json".toMediaTypeOrNull())
        binding.progressBar.isActivated = true
        binding.progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            try {
                val response = service.signup(requestBody)



                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        handleResponse(response.body(), email)

                    } else {

                        Toast.makeText(view?.context, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                Log.e("mirror", e.message!!)
            }
        }

    }

    private fun handleResponse(responseBody: responseMessage?, email: String) {
        if (responseBody != null) {
            if (responseBody.message == "User already exists") {
                binding.userExist.text = getString(R.string.userExist)
                binding.userExist.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE

                activateIputation()

            } else {
                sharedpreferences =
                    view?.context?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                val editor = sharedpreferences?.edit()
                editor?.putString(EMAIL_KEY, email)
                editor?.apply()

                binding.progressBar.visibility = View.GONE
                Toast.makeText(view?.context, responseBody.message, Toast.LENGTH_SHORT)
                    .show()
                val directions =
                    SignupFragmentDirections.actionSignupFragmentToHomeFragment()
                findNavController().navigate(directions)

            }

        }
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