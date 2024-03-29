package slu.edu.ph.cs_212_9343_ramonsters

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import java.io.ByteArrayOutputStream
import java.io.InputStream


/**
 * This class represents the backend functionality of the signUp page.
 * It gathers necessary user information to create a basic account for the application.
 * The class includes methods for handling user input, uploading a profile picture, and navigating to the login page.
 * It also uses a DatabaseHandler to add a new user to the database.
 *
 * @author Tank, Rithik
 * @author Jasmin, Ramon Emmiel P.
 */
class SignUp : AppCompatActivity() {

    lateinit var emailField : EditText
    lateinit var passwordField : EditText
    lateinit var nameField : EditText
    lateinit var signUpButton : Button
    lateinit var uploadPFPButton : Button
    lateinit var loginButton : TextView
    private var databaseHelper = DatabaseHandler(this)
    private var pfp : ByteArray? = null
    lateinit var contactField : EditText

    /**
     * Called when the activity is first created. Initializes UI components, sets up click listeners, and handles user input.
     * @param savedInstanceState The saved instance state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        emailField = findViewById(R.id.emailAddressField)
        passwordField = findViewById(R.id.passwordField)
        signUpButton = findViewById(R.id.signUpButton)
        uploadPFPButton = findViewById(R.id.uploadPFP)
        nameField = findViewById(R.id.nameField)
        loginButton = findViewById(R.id.login)
        contactField = findViewById(R.id.contactField)
        Log.i("signUp", " SignUp Page clicked")


        uploadPFPButton.setOnClickListener() {
            openFileChooser()
        }

        loginButton.setOnClickListener() {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }



        signUpButton.setOnClickListener() {
            if (pfp!= null) {
                Log.i("signUpButton", " SignUpButton clicked")
                val email = emailField.text.toString()
                val password = passwordField.text.toString()
                val name = nameField.text.toString()
                val contact = contactField.text.toString()
                Log.i("signUpButton", " SignUpButton reached")
                val newUser = User(
                    email,
                    password,
                    name,
                    contact,
                    0,
                    "",
                    "",
                    "",
                    "",
                    0.0,
                    0,
                    pfp,
                    null,
                    "",
                    ""
                )
                databaseHelper.addUser(newUser)
                val toast = Toast.makeText(this, "Account Created", Toast.LENGTH_LONG)
                toast.show()
                val intent = Intent(this, StudentMenu::class.java)
                intent.putExtra("user", newUser.userID)
                startActivity(intent)
            } else {
                var toast = Toast.makeText(this, "Upload a Photo.", Toast.LENGTH_LONG)
                toast.show()
            }
        }
    }

    /**
     * Opens the file chooser for selecting a profile picture.
     * Uses the imagePickerLauncher to handle the result of the file chooser activity.
     */
    private fun openFileChooser() {
        try {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
            Log.i("OpenFileChooser", "openFileChooser started")
        } catch (e: Exception) {
            Log.e("OpenFileChooser", "Exception: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Handles the result of the file chooser activity, retrieves the selected image, and sets it as the user's profile picture.
     */
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImageUri = result.data?.data
                selectedImageUri?.let { uri ->
                    val inputStream: InputStream? = contentResolver.openInputStream(uri)
                    val imageData: ByteArray? = readBytes(inputStream)
                    pfp = imageData
                }

                if (pfp != null) {
                    val toast = Toast.makeText(this,"Image Upload Success", Toast.LENGTH_LONG)
                    toast.show()
                } else {
                    val toast = Toast.makeText(this,"Image Upload Failed", Toast.LENGTH_LONG)
                    toast.show()
                    Log.e("imagePickerLauncher", "pfp is null")
                }
            }
            Log.i("imagePickerLauncher", "imagePickerLauncher reached")
        }

    /**
     * Reads bytes from the input stream.
     * @param inputStream The input stream to read from.
     * @return The byte array read from the input stream.
     */
    private fun readBytes(inputStream: InputStream?): ByteArray? {
        if (inputStream == null) return null
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len: Int
        try {
            while (inputStream.read(buffer).also { len = it } != -1) {
                byteBuffer.write(buffer, 0, len)
            }
            Log.i("readBytes", "readBytes success")
            return byteBuffer.toByteArray()
        } catch (e: Exception) {
            Log.i("readBytes", "readBytes fail")
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
                byteBuffer.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }
}