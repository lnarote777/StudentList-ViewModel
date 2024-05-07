import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import clases.GestorFichero
import clases.StudentViewModel
import java.awt.Checkbox
import java.io.File

@Composable
fun VentanaEstudiantes(onClose: () -> Unit){
    val archivo = File("estudiantes.txt")
    val gestor = GestorFichero()
    val studentViewModel = StudentViewModel(gestor, archivo)

    Window(
        onCloseRequest = onClose,
        title = "My Student"
    ){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.LightGray
        ) {
            Content(studentViewModel)
        }
    }
}
