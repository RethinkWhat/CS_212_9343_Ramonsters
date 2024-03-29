package slu.edu.ph.cs_212_9343_ramonsters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

/**
 * Class that provides the functionality for tutors to view and manage pending and approved
 * tutoring sessions
 */
class TutorMenu : AppCompatActivity(){

    /**
     * Displays the pending or approved students.
     */
    private lateinit var recycler1: RecyclerView

    /**
     * Instantiates and populates the components of the UI elements.
     */
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutor_menu)

        /**
         * Instantiates DatabaseHandler to perform database operations.
         */
        val databaseHelper = DatabaseHandler(this)
        var approvedSessionButton: Button = findViewById(R.id.approvedSessionsButton)
        var pendingSessionButton: Button = findViewById(R.id.pendingSessionsButton)
        var helloMsg: TextView = findViewById(R.id.helloMsg)

        val intent = getIntent();
        val username = intent.getStringExtra("user")
        Log.i("Tutor Menu", "getIntent")
        val user = databaseHelper.getUser(username!!)
        helloMsg.setText("Hello ${user!!.fullName}!")
        Log.i("Tutor Menu", "getUser")

        /**
         * Bitmap array that holds the image of the profile picture.
         */
        var bitmap = BitmapFactory.decodeByteArray(user.PFP,0,user.PFP!!.size)
        var scaledBitMap = Bitmap.createScaledBitmap(bitmap,
            100,
            100, true)
        var profileRedirectButton: Button = findViewById(R.id.profileRedirectButton)
        var imageOfRedirectButton : ImageView = findViewById(R.id.profileRedirectImage)
        imageOfRedirectButton.setImageBitmap(scaledBitMap)
        profileRedirectButton.setOnClickListener() {
            val newIntent = Intent(this, TutorProfileMenu::class.java)
            newIntent.putExtra("user", username)
            startActivity(newIntent)
        }

        /**
         * ArrayList of the students that are pending
         */
        var pendingTutors: ArrayList<User>? = databaseHelper.getPendings(user!!.userID)
        Log.i("Tutor Menu", "getPendings")

        recycler1 = findViewById(R.id.studentApplication_recycler_view)

        approvedSessionButton.setOnClickListener() {
            approvedSessionButton.setBackgroundColor(Color.parseColor("#0d0140"))
            approvedSessionButton.setTextColor(Color.parseColor("#FFFFFFFF"))
            pendingSessionButton.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            pendingSessionButton.setTextColor(Color.parseColor("#0d0140"))

            var confirmedTutors: ArrayList<User>? = databaseHelper.getConfirmed(user!!.userID)
            recycler1.adapter?.notifyDataSetChanged()
            recycler1.adapter =
                TutorAdapter(confirmedTutors!!, user!!.userID, R.layout.approvedsessions)

        }
        pendingSessionButton.setOnClickListener() {
            pendingSessionButton.setBackgroundColor(Color.parseColor("#0d0140"))
            pendingSessionButton.setTextColor(Color.parseColor("#FFFFFFFF"))
            approvedSessionButton.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            approvedSessionButton.setTextColor(Color.parseColor("#0d0140"))
            var pendingTutors: ArrayList<User>? = databaseHelper.getPendings(user!!.userID)
            recycler1.adapter?.notifyDataSetChanged()
            recycler1.adapter =
                TutorAdapter(pendingTutors!!, user!!.userID, R.layout.betutoredapplication)
        }
        Log.i("adapter", "Adapter reached")
        recycler1.adapter =
            TutorAdapter(pendingTutors!!, user!!.userID, R.layout.betutoredapplication)

    }

    /**
     * Nested class that store the users' attributes for three initial separate display boxes.
     *
     */
    class TutorAdapter(val tutors: ArrayList<User>, val username: String, val layoutID: Int) :

        RecyclerView.Adapter<TutorAdapter.TutorViewHolder>() {

        var box1: ArrayList<User> = ArrayList()
        var box2: ArrayList<User> = ArrayList()
        var box3: ArrayList<User> = ArrayList()

        /**
         * Handles button clicks in the RecyclerView
         */
        interface AdapterCallback {
            fun buttonClick(user: User)
        }

        /**
         * Nested class to view individual items of the RecyclerView.
         */
        class TutorViewHolder(val tutorView: View) : RecyclerView.ViewHolder(tutorView) {

            private val tutorName1: TextView = tutorView.findViewById(R.id.tutor)
            private val tutorName2: TextView = tutorView.findViewById(R.id.tutor2)
            private val tutorName3: TextView = tutorView.findViewById(R.id.tutor3)

            private val tutorLocation1: TextView = tutorView.findViewById(R.id.tutorLocation)
            private val tutorLocation2: TextView = tutorView.findViewById(R.id.tutorLocation2)
            private val tutorLocation3: TextView = tutorView.findViewById(R.id.tutorLocation3)

            private val tutorPhone1: TextView = tutorView.findViewById(R.id.phoneNumber)
            private val tutorPhone2: TextView = tutorView.findViewById(R.id.phoneNumber2)
            private val tutorPhone3: TextView = tutorView.findViewById(R.id.phoneNumber3)

            private val tutorImage1: ImageView = tutorView.findViewById(R.id.tutorImageView)
            private val tutorImage2: ImageView = tutorView.findViewById(R.id.tutorImageView2)
            private val tutorImage3: ImageView = tutorView.findViewById(R.id.tutorImageView3)

            private val acceptButton1 : Button? = tutorView.findViewById(R.id.acceptButton) ?: null
            private val rejectButton1 : Button? = tutorView.findViewById(R.id.rejectButton)
            private val acceptButton2 : Button? = tutorView.findViewById(R.id.acceptButton2)
            private val rejectButton2 : Button? = tutorView.findViewById(R.id.rejectButton2)
            private val acceptButton3 : Button? = tutorView.findViewById(R.id.acceptButton3)
            private val rejectButton3 : Button? = tutorView.findViewById(R.id.rejectButton3)

            private val studentAcceptedTxt : TextView = tutorView.findViewById(R.id.studentAcceptedTxt)
            private val studentAcceptedTxt2 : TextView = tutorView.findViewById(R.id.studentAcceptedTxt2)
            private val studentAcceptedTxt3 : TextView = tutorView.findViewById(R.id.studentAcceptedTxt3)

            fun bind( user1: User, user2: User, user3: User, context: Context, username: String) {
                Log.i("Tutor Menu", "TutorViewHolder")
                Log.i("BINDING BEGIN", "BINDING")

                val databaseHandler = DatabaseHandler(context)

                // First box
                    if (user1.userID != "") {
                        tutorName1.setText(user1.fullName)
                        tutorPhone1.setText(user1.contactNumber)
                        tutorLocation1.setText(user1.userID)
                        var bitmap1 = BitmapFactory.decodeByteArray(user1.PFP, 0, user1.PFP!!.size)
                        tutorImage1.setImageBitmap(bitmap1)
                    }

                    //Second box
                    if (user2.userID != "") {
                        tutorName2.visibility = View.VISIBLE
                        tutorPhone2.visibility = View.VISIBLE
                        tutorLocation2.visibility = View.VISIBLE
                        tutorImage2.visibility = View.VISIBLE
                        acceptButton2!!.visibility = View.VISIBLE

                        if (user2.pendings.toString().contains(username)) {
                            rejectButton2!!.visibility = View.VISIBLE
                        }

                        tutorName2.setText(user2.fullName)
                        tutorPhone2.setText(user2.contactNumber)
                        tutorLocation2.setText(user2.userID)
                        var bitmap2 = BitmapFactory.decodeByteArray(user2.PFP, 0, user2.PFP!!.size)
                        tutorImage2.setImageBitmap(bitmap2)
                    }

                    if (user3.userID != "") {
                        // Third box
                        tutorName3.visibility = View.VISIBLE
                        tutorPhone3.visibility = View.VISIBLE
                        tutorLocation3.visibility = View.VISIBLE
                        tutorImage3.visibility = View.VISIBLE
                        acceptButton2!!.visibility = View.VISIBLE

                        if (user3.pendings.toString().contains(username)) {
                            rejectButton3!!.visibility = View.VISIBLE
                        }
                        tutorName3.setText(user3.fullName)
                        tutorPhone3.setText(user3.contactNumber)
                        tutorLocation3.setText(user3.userID)
                        var bitmap3 = BitmapFactory.decodeByteArray(user3.PFP, 0, user3.PFP!!.size)
                        tutorImage3.setImageBitmap(bitmap3)
                    }
                if (user1.pendings.toString().contains(username)) {
                    acceptButton1!!.setOnClickListener() {
                        studentAcceptedTxt.visibility = View.VISIBLE
                        studentAcceptedTxt.setText("STUDENT ACCEPTED")
                        acceptButton1.visibility = View.GONE
                        rejectButton1!!.visibility = View.GONE
                        databaseHandler.tutorAcceptOrRejectStudent(user1.userID, username, 1)
                    }
                    rejectButton1!!.setOnClickListener() {
                        studentAcceptedTxt.visibility = View.VISIBLE
                        studentAcceptedTxt.setText("STUDENT REJECTED")
                        acceptButton1.visibility = View.GONE
                        rejectButton1!!.visibility = View.GONE
                        databaseHandler.tutorAcceptOrRejectStudent(user1.userID, username, 0)
                    }

                    acceptButton2!!.setOnClickListener() {
                        studentAcceptedTxt2.visibility = View.VISIBLE
                        studentAcceptedTxt2.setText("STUDENT ACCEPTED")
                        acceptButton2!!.visibility = View.GONE
                        rejectButton2!!.visibility = View.GONE
                        databaseHandler.tutorAcceptOrRejectStudent(user2.userID, username, 1)
                    }
                    rejectButton2!!.setOnClickListener() {
                        studentAcceptedTxt2.visibility = View.VISIBLE
                        studentAcceptedTxt2.setText("STUDENT REJECTED")
                        acceptButton2.visibility = View.GONE
                        rejectButton2.visibility = View.GONE
                        databaseHandler.tutorAcceptOrRejectStudent(user2.userID, username, 0)
                    }

                    acceptButton3!!.setOnClickListener() {
                        studentAcceptedTxt3.visibility = View.VISIBLE
                        studentAcceptedTxt3.setText("STUDENT ACCEPTED")
                        acceptButton3.visibility = View.GONE
                        rejectButton3!!.visibility = View.GONE
                        databaseHandler.tutorAcceptOrRejectStudent(user3.userID, username, 1)
                    }
                    rejectButton3!!.setOnClickListener() {
                        studentAcceptedTxt3.visibility = View.VISIBLE
                        studentAcceptedTxt3.setText("STUDENT REJECTED")
                        acceptButton3.visibility = View.GONE
                        rejectButton3.visibility = View.GONE
                        databaseHandler.tutorAcceptOrRejectStudent(user3.userID, username, 0)
                    }
                } else {
                    acceptButton1!!.setOnClickListener() {
                        databaseHandler.deleteFromConfirms(user1.userID,username)
                        studentAcceptedTxt.visibility = View.VISIBLE
                    }
                    acceptButton2!!.setOnClickListener() {
                        databaseHandler.deleteFromConfirms(user2.userID,username)
                        studentAcceptedTxt2.visibility = View.VISIBLE
                    }
                    acceptButton3!!.setOnClickListener() {
                        databaseHandler.deleteFromConfirms(user3.userID,username)
                        studentAcceptedTxt3.visibility = View.VISIBLE
                    }
                }
            }
        }

        /**
         * Creates a new view holder when needed.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorViewHolder {
            updateBoxes(tutors)
            val view = LayoutInflater.from(parent.context)
                .inflate(layoutID, parent, false)
            return TutorViewHolder(view)
        }

        /**
         * Gets the size of the tutors
         */
        override fun getItemCount(): Int {
            return tutors.size
        }

        override fun onBindViewHolder(holder: TutorViewHolder, position: Int) {
            Log.i("Bind", "Bind started")
            val box1Position = if (box1.isNotEmpty()) position % box1.size else 0
            val box2Position = if (box2.isNotEmpty()) position % box2.size else 0
            val box3Position = if (box3.isNotEmpty()) position % box3.size else 0

            var blankUser = User("","","","",0,"","","","",0.0,0,null,null,"","")
            Log.i("holder.bind", "Binding attempt")
            holder.bind(
                box1.getOrNull(box1Position) ?: blankUser,
                box2.getOrNull(box2Position) ?: blankUser,
                box3.getOrNull(box3Position) ?: blankUser,
                holder.itemView.context,
                username
            )
            Log.i("Bind", "Bind completed")
        }

        fun updateBoxes(users: ArrayList<User>) {
            Log.i("updateBoxes", "updateBoxes started")

            val boxCount = 3
            val boxSizes = IntArray(boxCount) { users.size / boxCount }  // Initialize with even distribution
            val remainder = users.size % boxCount

            // Distribute the remainder among the first few boxes
            for (i in 0 until remainder) {
                boxSizes[i]++
            }

            val boxes = listOf(box1, box2, box3)

            for (i in users.indices) {
                val user = users[i]
                val boxIndex = i % boxCount
                boxes[boxIndex].add(user)
            }

            Log.i("updateBoxes", "updateBoxes finished")
        }
    }
}
