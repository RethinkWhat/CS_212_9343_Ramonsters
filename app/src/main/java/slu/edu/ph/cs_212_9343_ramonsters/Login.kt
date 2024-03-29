package slu.edu.ph.cs_212_9343_ramonsters

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

/**
 * To stacktrace use Log.i(tag, message) after each method it helps track what executed,
 * you can follow the it on Logcat
 */

/**
 * LoginActivity handles user authentication and navigation to different menus based on user type.
 * This class includes methods for user login, database interactions, and UI initialization.
 * It also contains sample user credentials for testing purposes.
 */
class Login : AppCompatActivity() {

    lateinit var loginButton: Button
    lateinit var signUpButton: TextView
    lateinit var emailField: EditText
    lateinit var passwordField : EditText

    /**
     * It initializes the activity, sets up UI components, and registers click listeners.
     * @param savedInstanceState The saved instance state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        var databaseHelper = DatabaseHandler(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.loginButton)
        signUpButton = findViewById(R.id.signUp)
        emailField = findViewById(R.id.editTextTextEmailAddress)
        passwordField = findViewById(R.id.editTextTextPassword)


        /** To Register a user uncomment this line and execute the program */
        //val newUser = User("rithiktank358@gmail.com", "rithik", "Rithik Tank","09177900153",0,"Baguio City, Benguet","Computer Science","Mathematics","Science",100.20,0, null,null,null,null)
        //databaseHelper.addUser(newUser)

        /** To Reset the database uncomment this line and execute the program */
        /*val databaseFile = this.getDatabasePath(databaseHelper.databaseName)
        if (databaseFile.exists()) { databaseFile.delete() }
         */




        //databaseHelper.changeStatus("rithik@gmail.com", 3)


        signUpButton.setOnClickListener() {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        /**
         * Handles the click event for the login button.
         * Retrieves entered credentials, authenticates the user, and navigates to the appropriate menu.
         */
        loginButton.setOnClickListener() {
            val enteredUsername = emailField.text.toString()
            val enteredPassword = passwordField.text.toString()

            val user = databaseHelper.getUser(enteredUsername)
            Log.i("User Get", "User got from database")


            if (user != null && enteredPassword.equals(user.passHash)) {
                if (user.userType == 1) {
                    val intent = Intent(this, TutorMenu::class.java)
                    intent.putExtra("user", user.userID)
                    startActivity(intent)
                } else if (user.userType == 0) {
                    val intent = Intent(this, StudentMenu::class.java)
                    intent.putExtra("user", user.userID)
                    Log.i("User == 0 ", "StartActivity attempted")
                    startActivity(intent)
                    Log.i("User == 0 ", "StartActivity finished")
                } else if (user.userType == 2) {
                    val toast = Toast.makeText(this,"Your application is still under process.", Toast.LENGTH_LONG)
                    toast.show()
                } else if (user.userType == 3) {
                    val intent = Intent(this, AdminMenu::class.java)
                    intent.putExtra("user", user.userID)
                    startActivity(intent)
                }
                Log.i("User Success", "User find success")
                if (user.userType!=2) {
                    val toast = Toast.makeText(this, "Authentication Success", Toast.LENGTH_LONG)
                    toast.show()
                }
            } else {
                Log.i("User Failed", "User find fail")
                //Authentication failed
                val toast = Toast.makeText(this,"Authentication Error", Toast.LENGTH_LONG)
                toast.show()
            }
        }

    }
}