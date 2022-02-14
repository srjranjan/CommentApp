package com.srj.commentapp.fragments

import android.annotation.SuppressLint
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
import com.google.android.material.textfield.TextInputLayout
import com.srj.commentapp.R.color.red
import com.srj.commentapp.R.color.teal_200
import com.srj.commentapp.databinding.FragmentResetPasswordBinding
import com.srj.commentapp.utils.APIservice
import com.srj.commentapp.utils.Encryption
import com.srj.commentapp.utils.ServiceGenerator
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
 * Use the [ResetPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPasswordFragment : Fragment(), ServiceGenerator,
    Encryption {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val TAG: String = "ResetPassword"
    private lateinit var binding: FragmentResetPasswordBinding
    var secretKey = "R#Q9DtjzHMFen&C*"

    @RequiresApi(Build.VERSION_CODES.O)
    var encodedBase64Key: String = Encryption.encodeKey(secretKey)

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
        binding = FragmentResetPasswordBinding.inflate(layoutInflater)
        return (binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etEmail.addTextChangedListener {

            binding.tilEmail.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
        }
        binding.btnShowPassword.setOnClickListener {
            disableInput()
            val email = binding.etEmail.text.toString()
            val secretCode = binding.etSecretCode.text.toString()
            if (validateInput(email, secretCode)) {
                submitToServer(email, secretCode)
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun validateInput(email: String, secretCode: String): Boolean {
        var result = false
        when {
            email.isBlank() -> {
                enableInput()
                binding.tilEmail.error = "Email is Empty"
                binding.tilEmail.boxStrokeErrorColor = ColorStateList.valueOf(red)
                binding.etEmail.addTextChangedListener {
                    binding.tilEmail.error = null
                    binding.tilEmail.boxStrokeErrorColor = ColorStateList.valueOf(teal_200)

                }

            }
            secretCode.isBlank() -> {
                enableInput()
                binding.tilSecretcode.error = "Secret Code is Empty"
                binding.tilSecretcode.boxStrokeErrorColor = ColorStateList.valueOf(red)
                binding.etSecretCode.addTextChangedListener {
                    binding.tilSecretcode.error = null
                    binding.tilSecretcode.boxStrokeErrorColor = ColorStateList.valueOf(teal_200)
                }

            }
            else -> result = true

        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitToServer(email: String, secretCode: String) {
        val secretcode = Encryption.encrypt(secretCode, encodedBase64Key)
        val json = JSONObject()
        json.put("email", email)
        json.put("secretCode", secretcode)

        val jsonObjectString = json.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        val serviceGenerator = retrofit.create(APIservice::class.java)

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val response = serviceGenerator.forgotPassword(requestBody)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Log.d(TAG, response.toString())
                        handleResponse(response.body())
                    } else {
                        Toast.makeText(view?.context, response.message(), Toast.LENGTH_SHORT).show()
                        Log.d(TAG, response.message())

                    }
                }
            } catch (e: Exception) {


            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleResponse(body: PasswordModal?) {
        if (body != null) {
            val password = body.password
            Log.d(TAG, password)
            val decryptedPassword = Encryption.decrypt(password, encodedBase64Key)
            Log.d(TAG, decryptedPassword)
            binding.showPassword.text = decryptedPassword
            binding.showPassword.visibility = View.VISIBLE
        }
    }

    private fun disableInput() {
        binding.btnShowPassword.isEnabled = false
        binding.tilEmail.isEnabled = false
        binding.tilSecretcode.isEnabled = false

    }

    private fun enableInput() {
        binding.btnShowPassword.isEnabled = true
        binding.tilEmail.isEnabled = true
        binding.tilSecretcode.isEnabled = true

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResetPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResetPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}