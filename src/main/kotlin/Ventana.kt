import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.checkScrollableContainerConstraints
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import kotlinx.coroutines.delay
import java.io.File


@Composable
fun Ventana(onClose: () -> Unit){
    val path = "src/main/kotlin/estudiantes.txt"
    val archivo = File(path)
    val students = remember { mutableStateListOf<String>() }

    Window(
        onCloseRequest = onClose,
        title = "My Student"
    ){
        LeerArchivo(archivo, students)
        Content(archivo, students)
    }
}

@Composable
@Preview
fun Content(archivo: File, students: MutableList<String>) {
    var name by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var show by remember { mutableStateOf(false) }
    var eliminar by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterVertically)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AgregarEstudiante(name, { name = it }, { students.add(name); name = "" ; focusRequester.requestFocus() }, focusRequester)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Students: ${students.size}",
                        fontSize = 15.sp
                    )

                    LazyColumn(
                        modifier = Modifier
                            .border(2.dp, Color.Black)
                            .size(150.dp, 250.dp)
                            .background(Color.White)
                    ){
                        items(students) { student ->
                            ListaEstudiantes(student, students, eliminar){ eliminar = it; students.remove(student) }
                        }

                    }

                    ELiminarTodo(students)

                }
            }

            GuardarCambios({ archivo.writeText(students.joinToString ("\n" )); show = true }, show, {show = false})
        }
    }
}

@Composable
fun LeerArchivo(archivo: File, students: MutableList<String>){
    if (archivo.exists()){
        archivo.forEachLine { students.add(it) }
    }else{
        archivo.createNewFile()
    }
}

@Composable
fun AgregarEstudiante(name: String, onValueChange: (String)->Unit, onClick: () -> Unit, focusRequester: FocusRequester){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onValueChange,
            label = { Text("Nuevo estudiante") },
            modifier = Modifier
                .focusRequester(focusRequester)
        )
        Button(
            onClick = onClick
        ){
            Text("Añadir estudiante")
        }
    }
}

@Composable
fun GuardarCambios(click: () -> Unit, show: Boolean, show2: () -> Unit){
    Button(
        onClick = click
    ) {
        Text("Guardar cambios")
    }
    if (show){
        Toast("Cambios Guardados", show2)
    }

}

@Composable
fun ListaEstudiantes(student: String, students: MutableList<String>, eliminar: Boolean, eliminar2: (Boolean) -> Unit){
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = student,
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        )

        IconToggleButton(
            checked = eliminar,
            onCheckedChange = eliminar2
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar"
            )
        }
    }
}

@Composable
fun Toast(message: String, onDismiss: () -> Unit) {
    Dialog(
        icon = painterResource("info_icon.png"),
        title = "Atención",
        resizable = false,
        onCloseRequest = onDismiss
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text(message)
        }
    }
    // Cierra el Toast después de 2 segundos
    LaunchedEffect(Unit) {
        delay(2000)
        onDismiss()
    }

}


@Composable
fun ELiminarTodo(students: MutableList<String>){
    Button(
        onClick = { students.clear() }
    ){
        Text("Eliminar Todo")
    }
}