import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import clases.GestorFichero
import clases.StudentViewModelFile
import clases.StudentsRepository
import clases.StudentsViewModelBD
import java.io.File

@Composable
fun VentanaEstudiantes( onClose: () -> Unit){
    val archivo = File("estudiantes.txt")
    val gestor = GestorFichero()
    val repository = StudentsRepository()
    val studentViewModelFile = StudentViewModelFile(gestor, archivo)
    val studentsViewModelBD = StudentsViewModelBD(repository)
    var selected1 by remember { mutableStateOf(false) }
    var selected2 by remember { mutableStateOf(false) }

    Window(
        onCloseRequest = onClose,
        title = "My Student"
    ){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.LightGray
        ) {

            LaunchedEffect(key1 = true){
                studentViewModelFile.leerEstudiantesArchivo()
            }

            LaunchedEffect(key1 = true){
                studentsViewModelBD.leerEstudiantesArchivo()
            }

            Eleccion(
                selected1,
                selected2,
                {selected1 = true ; selected2 = false },
                {selected1 = false; selected2 = true}
            )

            if (selected1){

                Content(studentViewModelFile)
            }

            if (selected2){

                Content(studentsViewModelBD)
            }

        }
    }



}