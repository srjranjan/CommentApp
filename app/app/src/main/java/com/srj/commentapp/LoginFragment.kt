package com.srj.commentapp

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.srj.commentapp.Encryption.encodeKey
import com.srj.commentapp.Encryption.encrypt
import com.srj.commentapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment(), ServiceGenerator, Encryption {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentLoginBinding
    var secretKey = "mustbe16byteskey"

    @RequiresApi(Build.VERSION_CODES.O)
    var encodedBase64Key: String = encodeKey(secretKey)

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
        binding = FragmentLoginBinding.inflate(layoutInflater)

        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnsignIn.setOnClickListener {
            validateInput()
        }

        binding.tvSignUp.setOnClickListener {
            navigateToSignup()
        }
        binding.tvForgotPassword.setOnClickListener {
            navigateToForgotPassword()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun validateInput() {
        deActivateInputation()
        val email = binding.loginEmail.text.toString()
        var password = binding.loginPassword.text.toString()
        when {
            email.isBlank() -> {
                binding.tilEmail.boxStrokeErrorColor = ColorStateList.valueOf(R.color.red)
                binding.tilEmail.error = "Please enter email"
                activateInputation()

                Handler().postDelayed(
                    {
                        binding.tilEmail.boxStrokeErrorColor =
                            ColorStateList.valueOf(R.color.teal_200)
                        binding.tilEmail.error = null
                        binding.tilEmail.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    }, 5000
                )
            }
            password.isBlank() -> {
                binding.tilPassword.boxStrokeErrorColor = ColorStateList.valueOf(R.color.red)
                binding.tilPassword.error = "Please enter password"
                activateInputation()
                Handler().postDelayed(
                    {
                        binding.tilPassword.boxStrokeErrorColor =
                            ColorStateList.valueOf(R.color.teal_200)
                        binding.tilPassword.error = null
                        binding.tilPassword.endIconMode =
                            TextInputLayout.END_ICON_PASSWORD_TOGGLE
                    }, 5000
                )
            }

            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    password = encrypt(password, encodedBase64Key)
                }

                sendToServer(email, password)

            }
        }


    }

    private fun sendToServer(email: String, password: String) {
        val service = retrofit.create(APIservice::class.java)
        val json = JSONObject()
        json.put("email", email)
        json.put("password", password)

        val jsonObjectString = json.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        binding.progressBar1.isActivated = true
        binding.progressBar1.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.login(requestBody)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    binding.progressBar1.isEnabled = false
                    binding.progressBar1.visibility = View.GONE
                    handleResponse(response.body())
                }
            }
        }
    }

    private fun deActivateInputation() {
        binding.tilEmail.isEnabled = false
        binding.tilPassword.isEnabled = false
        binding.btnsignIn.isEnabled = false
    }

    private fun activateInputation() {
        binding.tilEmail.isEnabled = true
        binding.tilPassword.isEnabled = true
        binding.btnsignIn.isEnabled = true
    }

    private fun handleResponse(response: responseMessage?) {

        if (response != null) {
            if (response.message == "Successfully logged in!") {
                navigateToHomePage()
            } else {
                activateInputation()
                Toast.makeText(view?.context, response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToHomePage() {
        val directions = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        findNavController().navigate(directions)
    }

    private fun navigateToForgotPassword() {
        val directions = LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment()
        findNavController().navigate(directions)
    }

    private fun navigateToSignup() {
        val directions = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
        findNavController().navigate(directions)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}