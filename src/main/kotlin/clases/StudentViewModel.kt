package clases

import androidx.compose.runtime.*
import interfaces.IFile
import interfaces.IStudentViewModel
import java.io.File

class StudentViewModel (
    private val gestorFichero: IFile,
    private val nombreArchivo: File,

): IStudentViewModel {
    companion object {
        const val MAXLENGTH = 10
    }


    private var mensaje = mutableStateOf("")
    override val infoMessage: State<String> = mensaje

    private var mostrar = mutableStateOf(false)
    override val showInfoMessage: State<Boolean> = mostrar

    private val stateStudents = mutableStateListOf<String>()
    override val students : List<String> = stateStudents

    private var name = mutableStateOf("")
    override val newStudent: State<String> = name

    private var index = mutableStateOf(-1)
    override val selectedIndex: State<Int> = index



    override fun agregarEstudiante() {
        if (name.value.isNotBlank()){
            stateStudents.add(name.value.trim())
            name.value = ""
        }

    }

    override fun eliminarEstudiante(index: Int) {
        if(index in stateStudents.indices) stateStudents.removeAt(index)
    }

    override fun guardarCambios() {
        stateStudents.forEach{
            gestorFichero.crearFic(nombreArchivo.absolutePath, "$it\n", true)
        }

    }

    override fun eliminarTodo() {
        stateStudents.clear()
    }

    override fun mostrarInfo(show: Boolean) {
        mostrar.value = show
    }

    override fun leerEstudiantesArchivo() {
        val listaEstudiantes = gestorFichero.leer(nombreArchivo)

        if (listaEstudiantes != null){
            listaEstudiantes.forEach { stateStudents.add(it) }
        }else{
            infoMessage("ERROR - No se pudo leer el fichero")
        }
    }

    override fun nombreNuevoEstudiante(name: String) {
        TODO("Not yet implemented")
    }

    override fun estudianteSeleccionado(index: Int) {
        TODO("Not yet implemented")
    }

    override fun infoMessage(info: String) {
        mensaje.value = info
        mostrarInfo(true)
    }


}