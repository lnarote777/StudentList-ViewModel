package interfaces

import java.io.File

interface IFile {
    fun existeDir(ruta: String): String
    fun existeFic(ruta: String): String
    fun escribir(fichero: File, info: String): String
    fun leer(fichero: File): List<String>?
    fun crearDir(ruta: String): String
    fun crearFic(ruta: String, info: String = "", sobreescribir: Boolean = true): File?
}