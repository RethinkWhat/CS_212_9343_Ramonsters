package slu.edu.ph.cs_212_9343_ramonsters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * This class represents the main menu for students where they can view information about approved tutors,
 * pending tutor applications, and available tutors. Students can also navigate to their profile.
 */
class StudentMenu : AppCompatActivity() {

    lateinit var helloMessage: TextView
    lateinit var viewDetails: Button

    /**
     * Overrides the onCreate method to initialize the activity.
     *
     * @param savedInstanceState The saved instance state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_menu)


        /**
         * When needing information from the database this DatabaseHandler obj is what you will use.
         * Pertinent data for this menu is the list of pending tutors
         */
        var databaseHelper = DatabaseHandler(this)

        // Retrieving username from the intent
        var thisIntent = getIntent()
        val username = thisIntent.getStringExtra("user")

        // UI element initialization
        val approvedTutorsMsg : TextView = findViewById(R.id.approvedTutorsMsg)
        val pendingApplicationsMsg : TextView = findViewById(R.id.pendingApplicationsMsg)
        val pendingApplications : TextView = findViewById(R.id.pendingApplications)

        val approvedTutor : TextView = findViewById(R.id.approvedTutor)
        val approvedTutorsRecyclerView: RecyclerView = findViewById(R.id.approvedTutors_recycler_view)
        val pendingApplicationRecyclerView: RecyclerView = findViewById(R.id.pendingApplications_recycler_view)



        approvedTutorsMsg.text = ""
        pendingApplicationsMsg.text = ""
        approvedTutorsRecyclerView.setBackgroundColor(255)
        pendingApplicationRecyclerView.setBackgroundColor(255)

        helloMessage = findViewById(R.id.helloMsg)
        var user: User? = databaseHelper.getUser(username.toString())
        helloMessage.setText("Hello ${user!!.fullName}!")


        // This variable already holds all of the pending tutors in the database
        Log.i("onCreate", "possibleTutors Create")
        var possibleTutors: ArrayList<User>? = databaseHelper.getUsers(1)

        // Display confirmed tutors if any
        if (!databaseHelper.getUser(username!!)!!.confirmations.equals("")) {
            var confirmedTutors: ArrayList<User>? = databaseHelper.getConfirmed(username)
            approvedTutorsRecyclerView.adapter = TutorAdapter(confirmedTutors, username!!)
            approvedTutorsMsg.text = "Approved Tutors"

        }

        // Display pending tutor applications if any
        if (databaseHelper.getUser(username!!)!!.pendings != null) {
            var pendingTutors: ArrayList<User> = databaseHelper.getPendings(username)
            pendingApplicationRecyclerView.adapter = TutorAdapter(pendingTutors, username!!)
            pendingApplicationsMsg.text = "Pending Applications"

        }


        // Display available tutors
        Log.i("onCreate", "recyclerView Create")
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        Log.i("onCreate", "recyclerView Adapater Create")

        if (!possibleTutors!!.equals("")) {
            recyclerView.adapter = TutorAdapter(possibleTutors, username!!)
        }
        Log.i("onCreate", "recyclerView Adapater End")


        // Display the user's profile picture and set up a redirect to the profile menu
        var bitmap = BitmapFactory.decodeByteArray(user.PFP,0,user.PFP!!.size)
        var scaledBitMap = Bitmap.createScaledBitmap(bitmap,
            100,
            100, true)
        var profileRedirectButton: Button = findViewById(R.id.profileRedirectButton)
        var imageOfRedirectButton : ImageView = findViewById(R.id.profileRedirectImage)
        imageOfRedirectButton.setImageBitmap(scaledBitMap)
        profileRedirectButton.setOnClickListener() {
            val intent = Intent(this, ProfileMenu::class.java)
            intent.putExtra("user", username)
            startActivity(intent)
        }
    }

    /**
     * This inner class represents the adapter for the RecyclerView displaying tutors in the main menu.
     *
     * @property tutors List of tutors to be displayed in the RecyclerView.
     * @property username The username of the student using the app.
     */
    class TutorAdapter(val tutors: ArrayList<User>?, val username: String) :
        RecyclerView.Adapter<TutorAdapter.TutorViewHolder>() {

        /**
         * This inner class represents the ViewHolder for the TutorAdapter.
         *
         * @property tutorView The view representing a single tutor item in the RecyclerView.
         */
        class TutorViewHolder(val tutorView: View) : RecyclerView.ViewHolder(tutorView) {
            private val tutorTextView: TextView = tutorView.findViewById(R.id.tutor)
            private val tutorImageView: ImageView = tutorView.findViewById(R.id.tutorImageView)
            private val viewDetails: Button = tutorView.findViewById(R.id.viewDetailsButton)
            private val location : TextView = tutorView.findViewById(R.id.tutorLocation)
            private val specialization1 : TextView = tutorView.findViewById(R.id.specialization1)
            private val specialization2 : TextView = tutorView.findViewById(R.id.specialization2)
            private val specialization3 : TextView = tutorView.findViewById(R.id.specialization3)

            /**
             * Binds tutor information to the ViewHolder.
             *
             * @param user The User object representing a tutor.
             * @param context The context of the application.
             * @param username The username of the student using the app.
             */
            fun bind(user: User, context: Context, username: String) {
                tutorTextView.text = user.fullName
                var bitmap = BitmapFactory.decodeByteArray(user.PFP, 0, user.PFP!!.size)
                tutorImageView.setImageBitmap(bitmap)
                location.setText(user.location)
                specialization1.setText(user.specialization1)
                specialization2.setText(user.specialization2)
                specialization3.setText(user.specialization3)

                // Set up a click listener to view details of the tutor
                viewDetails.setOnClickListener() {
                    val intent = Intent(context, ViewDetails::class.java)
                    intent.putExtra("user", username)
                    intent.putExtra("tutor", user.userID)
                    context.startActivity(intent)
                }
            }
        }


        /**
         * Creates a new ViewHolder.
         *
         * @param parent The parent view group.
         * @param viewType The view type.
         * @return The created TutorViewHolder.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.availabletutors, parent, false)
            return TutorViewHolder(view)
        }


        /**
         * Gets the total number of tutors.
         *
         * @return The total number of tutors.
         */
        override fun getItemCount(): Int {
            return tutors!!.size
        }

        /**
         * Binds tutor information to the ViewHolder at the specified position.
         *
         * @param holder The TutorViewHolder to bind the information to.
         * @param position The position of the tutor in the list.
         */
        override fun onBindViewHolder(holder: TutorViewHolder, position: Int) {
            holder.bind(tutors!![position], holder.itemView.context, username)
        }
    }
}