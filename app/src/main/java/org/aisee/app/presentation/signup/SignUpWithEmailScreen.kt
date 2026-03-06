package org.aisee.app.presentation.signup

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import org.aisee.app.R

private val Purple = Color(0xFF9B87E8)
private val FieldBackground = Color(0xFF2A2A2A)
private val FieldText = Color(0xFF8A8A8A)
private val ErrorColor = Color(0xFFFF6B6B)

private fun validateName(name: String): String? {
    if (name.isEmpty()) return null
    if (name.length < 3) return "Must be at least 3 characters"
    if (!name.all { it.isLetter() }) return "Only letters allowed"
    return null
}

private fun validateEmail(email: String): String? {
    if (email.isEmpty()) return null
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Invalid email address"
    return null
}

private fun validatePhoneNumber(phone: String): String? {
    if (phone.isEmpty()) return null
    if (!phone.all { it.isDigit() }) return "Only numbers allowed"
    if (phone.length < 10) return "Must be at least 10 digits"
    return null
}

private fun validatePassword(password: String): String? {
    if (password.isEmpty()) return null
    if (password.length < 8) return "Must be at least 8 characters"
    if (!password.any { it.isUpperCase() }) return "Must include an uppercase letter"
    if (!password.any { it.isLowerCase() }) return "Must include a lowercase letter"
    if (!password.any { it.isDigit() }) return "Must include a digit"
    if (!password.any { !it.isLetterOrDigit() }) return "Must include a symbol"
    return null
}

@Composable
fun SignUpWithEmailScreen(
    onCreateAccount: (firstName: String, lastName: String, email: String, password: String, phoneNumber: String) -> Unit,
    onSignUpWithGoogle: () -> Unit,
    isLoading: Boolean = false
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val firstNameError by remember { derivedStateOf { validateName(firstName) } }
    val lastNameError by remember { derivedStateOf { validateName(lastName) } }
    val emailError by remember { derivedStateOf { validateEmail(email) } }
    val phoneNumberError by remember { derivedStateOf { validatePhoneNumber(phoneNumber) } }
    val passwordError by remember { derivedStateOf { validatePassword(password) } }
    val confirmPasswordError by remember {
        derivedStateOf {
            if (confirmPassword.isEmpty()) null
            else if (password != confirmPassword) "Passwords do not match"
            else null
        }
    }

    val isFormValid by remember {
        derivedStateOf {
            firstName.length >= 3 && firstName.all { it.isLetter() } &&
            lastName.length >= 3 && lastName.all { it.isLetter() } &&
            Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            phoneNumber.all { it.isDigit() } && phoneNumber.length >= 10 &&
            validatePassword(password) == null &&
            password == confirmPassword
        }
    }

    val fieldShape = RoundedCornerShape(24.dp)
    val fieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = FieldBackground,
        focusedContainerColor = FieldBackground,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        errorContainerColor = FieldBackground,
        cursorColor = Color.White,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        errorTextColor = Color.White,
        unfocusedPlaceholderColor = FieldText,
        focusedPlaceholderColor = FieldText,
        errorPlaceholderColor = FieldText
    )

    fun Modifier.errorBorder(hasError: Boolean): Modifier =
        if (hasError) this.border(1.5.dp, ErrorColor, fieldShape) else this

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 32.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        Image(
            painter = painterResource(id = R.drawable.aisee_icon),
            contentDescription = "AiSee Logo",
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Create an Account",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(32.dp))

        // First Name
        Text(
            text = "First Name",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            placeholder = { Text("First Name") },
            modifier = Modifier.fillMaxWidth().errorBorder(firstNameError != null),
            shape = fieldShape,
            colors = fieldColors,
            singleLine = true
        )
        if (firstNameError != null) {
            Text(
                text = firstNameError!!,
                style = MaterialTheme.typography.bodySmall,
                color = ErrorColor,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Last Name
        Text(
            text = "Last Name",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            placeholder = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth().errorBorder(lastNameError != null),
            shape = fieldShape,
            colors = fieldColors,
            singleLine = true
        )
        if (lastNameError != null) {
            Text(
                text = lastNameError!!,
                style = MaterialTheme.typography.bodySmall,
                color = ErrorColor,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Email
        Text(
            text = "Email",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") },
            modifier = Modifier.fillMaxWidth().errorBorder(emailError != null),
            shape = fieldShape,
            colors = fieldColors,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        if (emailError != null) {
            Text(
                text = emailError!!,
                style = MaterialTheme.typography.bodySmall,
                color = ErrorColor,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Phone Number
        Text(
            text = "Phone Number",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = phoneNumber,
            onValueChange = { if (it.all { c -> c.isDigit() }) phoneNumber = it },
            placeholder = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth().errorBorder(phoneNumberError != null),
            shape = fieldShape,
            colors = fieldColors,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (phoneNumberError != null) {
            Text(
                text = phoneNumberError!!,
                style = MaterialTheme.typography.bodySmall,
                color = ErrorColor,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Password
        Text(
            text = "Password",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            modifier = Modifier.fillMaxWidth().errorBorder(passwordError != null),
            shape = fieldShape,
            colors = fieldColors,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        if (passwordError != null) {
            Text(
                text = passwordError!!,
                style = MaterialTheme.typography.bodySmall,
                color = ErrorColor,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Confirm Password
        Text(
            text = "Confirm Password",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth().errorBorder(confirmPasswordError != null),
            shape = fieldShape,
            colors = fieldColors,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        if (confirmPasswordError != null) {
            Text(
                text = confirmPasswordError!!,
                style = MaterialTheme.typography.bodySmall,
                color = ErrorColor,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onCreateAccount(firstName, lastName, email, password, phoneNumber) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple,
                disabledContainerColor = Purple.copy(alpha = 0.4f),
                disabledContentColor = Color.White.copy(alpha = 0.5f)
            ),
            enabled = !isLoading && isFormValid
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color(0xFF3A3A3A)
            )
            Text(
                text = "or sign up with",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF8A8A8A),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color(0xFF3A3A3A)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
private fun SignUpWithEmailScreenPreview() {
    SignUpWithEmailScreen(
        onCreateAccount = { _, _, _, _, _ -> },
        onSignUpWithGoogle = {}
    )
}
