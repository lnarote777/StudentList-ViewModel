package interfaces

import androidx.compose.runtime.State

interface IStudentViewModel {
    val newStudent: State<String>
    val students: List<String>
    val infoMessage: State<String>
    val showInfoMessage: State<Boolean>
    val selectedIndex: State<Int>

    fun agregarEstudiante()
    fun eliminarEstudiante(index: Int)
    fun guardarCambios()
    fun eliminarTodo()
    fun mostrarInfo(show: Boolean)
    fun leerEstudiantesArchivo()
    fun nombreNuevoEstudiante(name: String)
    fun estudianteSeleccionado(index: Int)
    fun infoMessage(info: String)
    fun shouldShowScrollStudentListImage(): Boolean
}