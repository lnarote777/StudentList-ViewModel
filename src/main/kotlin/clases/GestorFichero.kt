package clases

import interfaces.IFile
import java.io.File

class GestorFichero: IFile {
    override fun existeDir(ruta: String): String {
        try {
            if (File(ruta).isDirectory){
                return ""
            }
        }catch (e: Exception){
            return "ERROR - Ha ocurrido un fallo al comprobar el directorio de la ruta"
        }
        return "ERROR - No existe el fichero"
    }

    override fun existeFic(ruta: String): String {
        try {
            if (File(ruta).isFile){
                return ""
            }
        }catch (e: Exception){
            return "ERROR - Ha ocurrido un fallo al comprobar el fichero"
        }
        return "ERROR - No existe el fichero"
    }

    override fun escribir(fichero: File, info: String): String {
        try {
            fichero.appendText(info)
            return ""
        }catch (e: Exception){
            return "ERROR - Ha ocurrido un fallo al escribir en el archivo"
        }
        return ""
    }

    override fun leer(fichero: File): List<String>? {
        var lineas : List<String>
        try {
            lineas = fichero.readLines()
        }catch (e: Exception){
            return null
        }
        return lineas
    }

    override fun crearDir(ruta: String): String {
        val dir = File(ruta)
        try {
            if (!dir.isDirectory){
                if (!dir.mkdirs()){
                    return "ERROR - No es posible crear la ruta de directorios"
                }
            }
        }catch (e: Exception){
            return "ERROR - Fallo al crear el directorio"
        }
        return ""
    }

    override fun crearFic(ruta: String, info: String, sobreescribir: Boolean): File? {
        val fichero = File(ruta)
        crearDir(fichero.parent)

        try {
            if (sobreescribir) {
                fichero.writeText(info)
            } else {
                fichero.createNewFile()
                if (info.isNotEmpty()) {
                    escribir( fichero, info)
                }
            }
        } catch (e: Exception) {
            return null
        }
        return fichero
    }

}