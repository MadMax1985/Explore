package android.ufrpe.com.explore

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.squareup.picasso.Picasso

/**
 * Created by Marcelo on 05/08/2018.
 */

class ImageAdapter(private val mContext: Context, private val mUsers: List<User>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    var user: User? = null
        private set
    private val textView: TextView? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false)
        return ImageViewHolder(v)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        this.user = mUsers[position]
        holder.textViewName.text = user!!.email

        Picasso.with(mContext)
                .load(user!!.imgUrl)
                .fit()
                .centerCrop()
                .into(holder.imageView)

        holder.imageView.setOnClickListener {
            val chat = Chat()
            chat.setContactName(user!!.email!!)

            val intent = Intent(mContext, Chat::class.java)
            mContext.startActivity(intent)
            Log.v("Usuário", user!!.email)
        }


    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewName: TextView
        var imageView: ImageView

        init {
            textViewName = itemView.findViewById(R.id.text_view_name) as TextView
            imageView = itemView.findViewById(R.id.image_view_upload) as ImageView


        }


    }


}



