package android.ufrpe.com.explore

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList

class UserList : AppCompatActivity() {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: ImageAdapter? = null

    private var mDataBaseRef: DatabaseReference? = null
    private var mUser: MutableList<User>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        mRecyclerView = findViewById(R.id.recycler_view) as RecyclerView
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)

        mUser = ArrayList<User>()

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("users")
        mDataBaseRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapShot in dataSnapshot.children) {
                    val user = postSnapShot.getValue<User>(User::class.java)
                    mUser!!.add(user!!)
                }

                mAdapter = ImageAdapter(this@UserList, mUser!!)
                mRecyclerView!!.adapter = mAdapter

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@UserList, databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })


    }


}
