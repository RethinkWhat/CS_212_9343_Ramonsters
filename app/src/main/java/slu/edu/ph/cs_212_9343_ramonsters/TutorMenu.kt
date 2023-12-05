package slu.edu.ph.cs_212_9343_ramonsters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView

class TutorMenu : AppCompatActivity() {

    var box1 : ArrayList<User> = ArrayList()
    var box2 : ArrayList<User> = ArrayList()
    var box3 : ArrayList<User> = ArrayList()

    lateinit var studentApplicationRecyclerView: RecyclerView
    lateinit var studentApplicationRecyclerView2: RecyclerView
    lateinit var studentApplicationRecyclerView3: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutor_menu)
        var users: ArrayList<User> = ArrayList()

        studentApplicationRecyclerView = findViewById(R.id.studentApplication_recycler_view)
        studentApplicationRecyclerView2 =  findViewById(R.id.studentApplication_recycler_view2)
        studentApplicationRecyclerView3 = findViewById(R.id.studentApplication_recycler_view3)
        /**
         * When needing information from the database this DatabaseHandler obj is what you will use.
         * Pertinent data for this menu is the list of pending tutors
         */
        var databaseHelper = DatabaseHandler(this)

        val approvedSessionsButton : Button = findViewById(R.id.approvedSessionsButton)
        val pendingSessionsButton : Button = findViewById(R.id.pendingSessionsButton)
        approvedSessionsButton.setOnClickListener() {
            approvedSessionsButton.setBackgroundColor(Color.parseColor("#0d0140"))
            approvedSessionsButton.setTextColor(Color.WHITE)
            pendingSessionsButton.setTextColor(Color.parseColor("#0d0140"))
            pendingSessionsButton.setBackgroundColor(Color.parseColor("#FFFFFFFF"))

            users = databaseHelper.getUsers(2) //TODO:Change method to get all the USERS that are confirmed under a tutor
            updateBoxes(users)

            //TODO: Change Username when ADMIN Page is Finished
            studentApplicationRecyclerView.adapter =TutorAdapter(box1, "ramonjasmin@gmail.com"!!,R.layout.approvedsessions)
            studentApplicationRecyclerView2.adapter =TutorAdapter(box2, "ramonjasmin@gmail.com"!!,R.layout.approvedsessions)
            studentApplicationRecyclerView3.adapter =TutorAdapter(box3, "ramonjasmin@gmail.com"!!,R.layout.approvedsessions)
        }

        pendingSessionsButton.setOnClickListener() {
            approvedSessionsButton.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            pendingSessionsButton.setTextColor(Color.WHITE)
            approvedSessionsButton.setTextColor(Color.parseColor("#0d0140"))
            pendingSessionsButton.setBackgroundColor(Color.parseColor("#0d0140"))

            users = databaseHelper.getUsers(2) //TODO:Change method to get PENDING USERS
            updateBoxes(users)
        }
        users = databaseHelper.getUsers(1) //TODO:Change method to get PENDING USERS
        updateBoxes(users)
    }

    fun updateBoxes(users : ArrayList<User>) {

        var size = users.size / 3
        for (x in 0 until size) {
            box1.add(users.get(x))
        }
        for (x in size until(size+size)) {
            box2.add(users.get(x))
        }
        for (x in (size+size)until (size*3)) {
            box3.add(users.get(x))
        }
        if (size % 3 ==2) {
            box1.add(users.get(users.size-1))
            box2.add(users.get(users.size-2))
        }
        if (size % 3 ==1) {
            box1.add(users.get(users.size-1))
        }

        //TODO: Change Username when ADMIN Page is Finished
        studentApplicationRecyclerView.adapter =TutorAdapter(box1, "ramonjasmin@gmail.com"!!,R.layout.betutoredapplication)
        studentApplicationRecyclerView2.adapter =TutorAdapter(box2, "ramonjasmin@gmail.com"!!,R.layout.betutoredapplication)
        studentApplicationRecyclerView3.adapter =TutorAdapter(box3, "ramonjasmin@gmail.com"!!,R.layout.betutoredapplication)
    }


    class TutorAdapter(val tutors: ArrayList<User>, val username: String, val layoutID : Int) :
        RecyclerView.Adapter<TutorAdapter.TutorViewHolder>() {

        class TutorViewHolder(val tutorView: View) : RecyclerView.ViewHolder(tutorView) {
            private val tutorTextView: TextView = tutorView.findViewById(R.id.tutor)
            private val tutorImageView: ImageView = tutorView.findViewById(R.id.tutorImageView)
            private val rejectButton: Button? = tutorView.findViewById(R.id.rejectButton)
            private val acceptButton: Button? = tutorView.findViewById(R.id.acceptButton)
            private val acceptedText : TextView? = tutorView.findViewById(R.id.studentAcceptedTxt)
            private val markButton : Button? = tutorView.findViewById(R.id.markSessionButton)

            fun bind(user: User, context: Context, username: String) {
                tutorTextView.text = user.fullName
                var bitmap = BitmapFactory.decodeByteArray(user.PFP, 0, user.PFP!!.size)
                tutorImageView.setImageBitmap(bitmap)


                if (acceptButton != null) {
                    acceptButton!!.setOnClickListener() {
                        acceptedText!!.visibility = View.VISIBLE
                        acceptButton.visibility = View.GONE
                        rejectButton!!.visibility = View.GONE
                        //TODO: Pass in method to accept user

                    }
                    rejectButton!!.setOnClickListener() {
                        acceptedText!!.visibility = View.VISIBLE
                        acceptedText.setText("STUDENT REJECTED")
                        acceptButton.visibility = View.GONE
                        rejectButton.visibility = View.GONE
                        //TODO: Pass in method to reject user
                    }
                }
                if (markButton != null) {
                    markButton!!.setOnClickListener() {
                        markButton.setBackgroundColor(Color.parseColor("#D4CEFA"))
                        //TODO: Code to remove student from confirmation
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(layoutID, parent, false)
            return TutorViewHolder(view)
        }

        override fun getItemCount(): Int {
            return tutors.size
        }

        override fun onBindViewHolder(holder: TutorViewHolder, position: Int) {
            holder.bind(tutors[position], holder.itemView.context, username)
        }
    }
}