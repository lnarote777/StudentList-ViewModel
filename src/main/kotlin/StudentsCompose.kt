
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import clases.GestorFichero
import clases.StudentViewModel
import interfaces.IStudentViewModel
import kotlinx.coroutines.delay
import java.io.File




@Composable
fun Content(viewModel: IStudentViewModel) {
    val student by viewModel.newStudent
    val students = viewModel.students
    val focusRequester = remember { FocusRequester() }
    val show by viewModel.showInfoMessage
    var eliminar by remember { mutableStateOf(false) }
    val selectedIndex by viewModel.selectedIndex
    val info by viewModel.infoMessage

    val showScrollStudentListImage = remember {
        derivedStateOf { viewModel.shouldShowScrollStudentListImage() }
    }

    LaunchedEffect(key1 = true){
        viewModel.leerEstudiantesArchivo()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterVertically)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AgregarEstudiante(
                name = student,
                onValueChange = { name -> viewModel.nombreNuevoEstudiante(name) },
                onClick = { viewModel.agregarEstudiante() ; focusRequester.requestFocus() },
                focusRequester = focusRequester
            )

            ColumnaEstudiantes(
                students = students,
                focusRequester = focusRequester,
                selectedIndex = selectedIndex,
                estudianteSeleccionado = {index -> viewModel.estudianteSeleccionado(index)},
                eliminarTodo = {viewModel.eliminarTodo()},
                eliminarEstudianteSeleccionado = {index -> viewModel.eliminarEstudiante(index)}
            )

            ImageUpDownScroll(showScrollStudentListImage.value)
        }

        GuardarCambios(show, {viewModel.guardarCambios(); focusRequester.requestFocus(); viewModel.mostrarInfo(true)}, {viewModel.mostrarInfo(false) } )
    }
}


@Composable
fun AgregarEstudiante(name: String, onValueChange: (String) -> Unit, onClick: () -> Unit, focusRequester: FocusRequester){
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
                .onKeyEvent { keyEvent ->
                    controlKeyEnter(
                        keyEvent,
                        onClick
                    ) },
            maxLines = 1
        )
        Button(
            onClick = onClick
        ){
            Text("Añadir estudiante")
        }
    }
}

@Composable
fun GuardarCambios( show: Boolean, click: () -> Unit, show2: () -> Unit){
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
fun ListaEstudiantes(index: Int, students: List<String>, onClick: (Int) -> Unit){
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = students[index],
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        )

        IconButton(
            onClick = { onClick(index) }
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
fun ELiminarTodo(onClick: () -> Unit){
    Button(
        onClick = onClick
    ){
        Text("Eliminar Todo")
    }
}

@Composable
fun ColumnaEstudiantes(
    students: List<String>,
    focusRequester: FocusRequester,
    selectedIndex: Int,
    estudianteSeleccionado: (Int) -> Unit,
    eliminarTodo:() -> Unit,
    eliminarEstudianteSeleccionado: (Int) -> Unit
){
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
                .focusRequester(focusRequester)
                .onKeyEvent { event ->
                    selectRowScrolling(
                        event = event,
                        selectedIndex = selectedIndex,
                        maxRow = students.size - 1,
                        onStudentSelected = { estudianteSeleccionado(it) }
                    )
                }
        ){
            items(students.size) { index ->
                ListaEstudiantes(
                    index = index,
                    students = students
                ){ eliminarEstudianteSeleccionado(index)}
            }

        }

        ELiminarTodo { eliminarTodo() }

    }
}


@OptIn(ExperimentalComposeUiApi::class)
fun selectRowScrolling(
    event: KeyEvent,
    selectedIndex: Int,
    maxRow: Int,
    onStudentSelected: (Int) -> Unit
) : Boolean {
    return if (event.type == KeyEventType.KeyDown) {
        when (event.key) {
            Key.DirectionUp -> {
                if (selectedIndex > 0) {
                    onStudentSelected(selectedIndex - 1)
                    true
                } else false//No consumimos el evento
            }

            Key.DirectionDown -> {
                if (selectedIndex < maxRow) {
                    onStudentSelected(selectedIndex + 1)
                    true
                } else false//No consumimos el evento
            }

            else -> false//No consumimos el evento
        }
    } else {
        false//Solo manejar cuando la tecla se haya levantado de la presión
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun controlKeyEnter(
    event: KeyEvent,
    onButtonAddNewStudentClick: () -> Unit
) : Boolean {
    return if (event.type == KeyEventType.KeyDown && event.key == Key.Enter) {
        onButtonAddNewStudentClick()
        true // Consumimos el evento
    } else {
        false // No consumimos el evento
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageWithTooltip(tooltipText: String, imagePath: String, contentDesc: String, modifierImg: Modifier) {
    TooltipArea(
        tooltip = {
            Box(
                modifier = Modifier
                    .background(Color.LightGray)
                    .border(1.dp, Color.DarkGray)
            ) {
                Text(
                    text = tooltipText,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    ) {
        Image(
            painter = painterResource(imagePath),
            contentDescription = contentDesc,
            modifier = modifierImg
        )
    }
}

@Composable
fun ImageUpDownScroll(
    showImgScrollStudentList: Boolean,
) {
    if (showImgScrollStudentList) {
        ImageWithTooltip(
            tooltipText = "Use scroll down-up",
            imagePath = "up_down_arrows.png",
            contentDesc = "Use scroll down-up",
            modifierImg = Modifier
                .padding(start = 5.dp, bottom = 50.dp)
                .width(50.dp)
        )
    } else {
        Box(
            modifier = Modifier
                .padding(start = 5.dp, bottom = 50.dp)
                .size(20.dp)
        )
    }
}