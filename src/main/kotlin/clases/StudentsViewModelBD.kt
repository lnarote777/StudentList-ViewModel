package clases

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import interfaces.IStudentViewModel

class StudentsViewModelBD(private val repository: StudentsRepository): IStudentViewModel{

    companion object {
        const val MAXLENGTH = 10
        const val MAXVIEW = 5
    }


    private var mensaje = mutableStateOf("")
    override val infoMessage: State<String> = mensaje

    private var mostrar = mutableStateOf(false)
    override val showInfoMessage: State<Boolean> = mostrar

    private val stateStudents = mutableStateListOf<String>()
    override val students : List<String> = stateStudents

    private var name = mutableStateOf("")
    override val newStudent: State<String> = name

    private var indexSelected = mutableStateOf(-1)
    override val selectedIndex: State<Int> = indexSelected

    override fun agregarEstudiante() {
        if (name.value.isNotBlank()){
            stateStudents.add(name.value.trim())
            repository.insertStudent(name.value)
            name.value = ""
        }
    }

    override fun eliminarEstudiante(index: Int) {
        repository.deleteStudent(index)
    }

    override fun guardarCambios() {
        repository.updateStudents(stateStudents)
    }

    override fun eliminarTodo() {
        repository.clearAll()
    }

    override fun mostrarInfo(show: Boolean) {
        mostrar.value = show
    }

    override fun leerEstudiantesArchivo() {
        val result = repository.getAllStudents()

        if (result.isSuccess){
            val studiantesDb = result.getOrNull()

            if (studiantesDb != null) {
                for (estudiante in studiantesDb){
                    stateStudents.add(estudiante)
                }
            }else{
                infoMessage("Base de datos vacía")
            }
        }

    }

    override fun nombreNuevoEstudiante(nombre: String) {
        if (nombre.length <= MAXLENGTH) name.value = nombre
    }

    override fun estudianteSeleccionado(index: Int) {
        indexSelected.value = index
    }

    override fun infoMessage(info: String) {
        mensaje.value = info
        mostrarInfo(true)
    }

    override fun shouldShowScrollStudentListImage() = stateStudents.size > MAXVIEW

}