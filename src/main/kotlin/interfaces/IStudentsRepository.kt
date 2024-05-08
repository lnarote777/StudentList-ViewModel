package interfaces

interface IStudentsRepository {
    fun getAllStudents(): Result<List<String>>
    fun updateStudents(students: List<String>): Result<Unit>
    fun clearAll(): Result<Unit>
    fun deleteStudent(index: Int): Result<Unit>
    fun insertStudent(student: String): Result<Unit>
}