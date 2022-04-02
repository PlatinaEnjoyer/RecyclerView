package com.example.recyclerview

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclerview.databinding.ItemUserBinding
import com.example.recyclerview.model.User

interface UserActionListener{
    fun onUserMove(user: User, moveBy: Int)

    fun onUserDelete(user: User)

    fun onUserDetails(user: User)
}


class UsersAdapter(
    private val actionListener: UserActionListener // это че такое
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(), View.OnClickListener {



    var users: List<User> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class UsersViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(p0: View) {
        val user = p0.tag as User
        when(p0.id){
            R.id.optionImageButton ->{
                showPopUpMenu(p0)
            }
            else -> {
                actionListener.onUserDetails(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.optionImageButton.setOnClickListener(this)

        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = users[position]
        with(holder.binding){
            holder.itemView.tag = user // что за тэги
            optionImageButton.tag = user


            userNameTextView.text = user.name
            userCompanyTextView.text = user.company
            if (user.photo.isNotBlank()) {
                Glide.with(photoImage.context)
                    .load(user.photo)
                    .circleCrop()
                    .placeholder(R.drawable.ic_user_avatar)
                    .error(R.drawable.ic_user_avatar)
                    .into(photoImage)
            } else {
                photoImage.setImageResource(R.drawable.ic_user_avatar)
            }
        }
    }

    private fun showPopUpMenu(view: View){
        val popupMenu = PopupMenu(view.context, view)
        val user = view.tag as User

        popupMenu.menu.add(0, 1, Menu.NONE, "MOVE UP")
        popupMenu.menu.add(0, 3, Menu.NONE, "DELETE")
        popupMenu.menu.add(0, 2, Menu.NONE, "MOVE DOWN")

        popupMenu.setOnMenuItemClickListener{
            when(it.itemId){
                1 -> {
                    actionListener.onUserMove(user, -1)
                }
                2 -> {
                    actionListener.onUserMove(user, 1)
                }
                3 -> {
                    actionListener.onUserDelete(user)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }


    override fun getItemCount(): Int = users.size
}