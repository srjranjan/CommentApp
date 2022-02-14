package com.srj.commentapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.srj.commentapp.R.color.red
import com.srj.commentapp.R.color.teal_200
import com.srj.commentapp.R.string.empty
import com.srj.commentapp.databinding.FragmentHomeBinding
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
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), ServiceGenerator {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val TAG = "HomeFragment"
    lateinit var binding: FragmentHomeBinding
    val SHARED_PREFS = "shared_prefs"

    val EMAIL_KEY = "email_key"


    var sharedpreferences: SharedPreferences? = null

    var email: String = ""
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
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return (binding.root)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getComments()
        sharedpreferences =
            view.context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        binding.commentEditText.addTextChangedListener {
            binding.textInputLayout4.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
        }

        binding.logout.setOnClickListener {
            val editor: SharedPreferences.Editor? = sharedpreferences?.edit()
            editor?.clear()
            editor?.apply()
            val directions = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
            findNavController().navigate(directions)
        }
        binding.submit.setOnClickListener {


            email = sharedpreferences?.getString(EMAIL_KEY, null).toString()
            val comment = binding.commentEditText.text.toString()
            deActivateInput()
            if (validateInput(comment))
                submitComment(
                    email,
                    comment
                )
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun validateInput(comment: String): Boolean {
        var result = false
        when {

            comment.isBlank() -> {
                binding.textInputLayout4.boxStrokeErrorColor = ColorStateList.valueOf(red)

                binding.textInputLayout4.error = getString(empty)
                activateInput()
                binding.commentEditText.addTextChangedListener {
                    binding.textInputLayout4.boxStrokeErrorColor =
                        ColorStateList.valueOf(teal_200)
                    binding.textInputLayout4.error = null
                }
            }
            else -> result = true
        }
        return result
    }

    private fun deActivateInput() {
        binding.submit.isEnabled = false
        binding.textInputLayout4.isEnabled = false
        binding.commentEditText.isEnabled = false
        binding.commentEditText.text?.clear()
    }

    private fun activateInput() {
        binding.submit.isEnabled = true
        binding.textInputLayout4.isEnabled = true
        binding.commentEditText.isEnabled = true

    }

    private fun submitComment(email: String?, comment: String) {
        binding.progressBar2.isActivated = true
        binding.progressBar2.visibility = View.VISIBLE

        val serviceGenerator = retrofit.create(APIservice::class.java)
        val json = JSONObject()
        json.put("email", email)
        json.put("comment", comment)
        val jsonObjectString = json.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = serviceGenerator.submitComment(requestBody)
                withContext(Dispatchers.Main)
                {
                    if (response.isSuccessful) {
                        binding.progressBar2.visibility = View.GONE
                        Log.d(TAG, response.message())
                        handleResponse(response.body())
                    } else {
                        Log.d(TAG, response.message())
                        Toast.makeText(view?.context, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {

            }

        }

    }

    private fun handleResponse(body: postComment?) {
        if (body != null) {
            if (body.message == "Successfully created a new comment") {
                activateInput()
                getComments()
            }
            val email = body.email
            val comment = body.comment
            val message = body.message
            Log.d(TAG, "$email  , $comment , $message")
        }
    }

    private fun getComments() {
        binding.progressBar2.isEnabled = true
        binding.progressBar2.visibility = View.VISIBLE
        deActivateInput()
        val serviceGenerator = retrofit.create(APIservice::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = serviceGenerator.viewComment()
                withContext(Dispatchers.Main)
                {
                    binding.progressBar2.isEnabled = false
                    binding.progressBar2.visibility = View.GONE
                    activateInput()
                    if (response.isSuccessful) {
                        Log.d(TAG, response.message())
                        initRecylerview(response.body())
                    } else {
                        Log.d(TAG, response.message())
                        Toast.makeText(view?.context, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())

            }

        }
    }

    private fun initRecylerview(body: List<CommentModal>?) {
        Log.d(TAG, body.toString())
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(view?.context)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = CommentsRecyclerAdapter(body)
        binding.recyclerView.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}