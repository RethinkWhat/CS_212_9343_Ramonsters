package slu.edu.ph.cs_212_9343_ramonsters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import slu.edu.ph.cs_212_9343_ramonsters.DatabaseHandler


/** DECLARED USERS SO FAR:
 *      Tutor:
 *          1. Email: patrick, password: patpat
 *      User:
 *          1. Email: ramon, password: monem
 * */

/**
 * To stacktrace use Log.i(tag, message) after each method it helps track what executed,
 * you can follow the it on Logcat
 */

class LoginActivity : AppCompatActivity() {

    var databaseHelper = DatabaseHandler(this)
    lateinit var loginButton: Button
    lateinit var emailField: EditText
    lateinit var passwordField : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.loginButton)
        emailField = findViewById(R.id.editTextTextEmailAddress)
        passwordField = findViewById(R.id.editTextTextPassword)


        /** To Reset the database uncomment this line and execute the program
        val databaseFile = this.getDatabasePath(databaseHelper.databaseName)
        if (databaseFile.exists()) {
            databaseFile.delete()
        }
        */

        // Register
        val newUser = User("Basti", "pass", false,"",0.0,0)
        databaseHelper.addUser(newUser)

        loginButton.setOnClickListener() {
            val enteredUsername = emailField.text.toString()
            val enteredPassword = passwordField.text.toString()

            val user = databaseHelper.getUser(enteredUsername)
            Log.i("User Get", "User got from database")


            if (user != null && enteredPassword.equals(user.passHash)) {
                if (user.tutor)
                    setContentView(R.layout.tutormenu)
                else
                    setContentView(R.layout.tutormenu)

                Log.i("User Success", "User find success")
            } else {
                Log.i("User Failed", "User find fail")
                //Authentication failed
            }
        }

    }
}