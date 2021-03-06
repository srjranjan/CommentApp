package com.srj.commentapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.srj.commentapp.R
import com.srj.commentapp.databinding.CommentLayoutBinding
import com.srj.commentapp.modals.TestJsonClass

class CommentsRecyclerAdapter(
    private var commentModal: TestJsonClass
) : RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CommentLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                val email = commentModal[itemCount - (position + 1)].email
                val comment = commentModal[itemCount - (position + 1)].comment
                Toast.makeText(itemView.context, "$email \n $comment ", Toast.LENGTH_SHORT).show()
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CommentLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.email.text =
            holder.itemView.context.getString(R.string.user) + " " + commentModal[itemCount - (position + 1)].email
        holder.binding.comments.text = commentModal[itemCount - (position + 1)].comment
    }

    override fun getItemCount(): Int {
        return commentModal.size
    }
}