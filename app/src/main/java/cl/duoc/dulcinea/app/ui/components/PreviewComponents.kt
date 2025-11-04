package cl.duoc.dulcinea.app.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cl.duoc.dulcinea.app.ui.theme.DulcineaAppTheme

@Composable
fun MyButton(text: String, onClick: () -> Unit = {}) {
    Button(onClick = onClick) {
        Text(text = text)
    }
}


@Preview(showBackground = true)
@Composable
fun MyButtonPreview() {
    DulcineaAppTheme {
        MyButton("Mi Botón")
    }
}