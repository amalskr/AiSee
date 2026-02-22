package org.aisee.app.presentation.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import org.aisee.app.R

private val Purple = Color(0xFF9B87E8)
private val FieldBackground = Color(0xFF2A2A2A)
private val FieldText = Color(0xFF8A8A8A)

@Composable
fun SignUpWithEmailScreen(
    onCreateAccount: (username: String, email: String, password: String) -> Unit,
    onSignUpWithGoogle: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val fieldShape = RoundedCornerShape(24.dp)
    val fieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = FieldBackground,
        focusedContainerColor = FieldBackground,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        cursorColor = Color.White,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        unfocusedPlaceholderColor = FieldText,
        focusedPlaceholderColor = FieldText
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 32.dp)
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
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Username
        Text(
            text = "Username",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            shape = fieldShape,
            colors = fieldColors,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Email
        Text(
            text = "Email",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = fieldShape,
            colors = fieldColors,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Password
        Text(
            text = "Password",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = fieldShape,
            colors = fieldColors,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Create Account button
        Button(
            onClick = { onCreateAccount(username, email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text(
                text = "Create Account",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // "or sign up with" divider
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
                fontSize = 13.sp,
                color = Color(0xFF8A8A8A),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color(0xFF3A3A3A)
            )
        }
    }
}
