package clases

import Database
import interfaces.IStudentsRepository
import java.sql.Connection
import javax.xml.crypto.Data

class StudentsRepository: IStudentsRepository {

    override fun getAllStudents(): Result<List<String>> {
        return try {
            val connectionDb = Database.getConnection()
            val students = mutableListOf<String>()
            connectionDb.use { conn ->
                conn.createStatement().use { stmt ->
                    stmt.executeQuery("SELECT name FROM students").use { rs ->
                        while (rs.next()) {
                            students.add(rs.getString("name"))
                        }
                    }
                }
            }
            Result.success(students)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun updateStudents(students: List<String>): Result<Unit> {
        var connectionDb : Connection? = null
        return try {
            connectionDb = Database.getConnection()
            connectionDb.autoCommit = false
            connectionDb.createStatement().use { stmt ->
                stmt.execute("DELETE FROM students")
            }
            connectionDb.prepareStatement("INSERT INTO students (name) VALUES (?)").use { ps ->
                for (student in students) {
                    ps.setString(1, student)
                    ps.executeUpdate()
                }
            }
            connectionDb.commit()
            Result.success(Unit)
        } catch (e: Exception) {
            connectionDb?.rollback()
            Result.failure(e)
        } finally {
            connectionDb?.autoCommit = true
            connectionDb?.close()
        }
    }

    override fun clearAll(): Result<Unit> {
        var connectionDb : Connection? = null
        return try {
            connectionDb = Database.getConnection()
            connectionDb.autoCommit = false

            connectionDb.createStatement().use { stmt ->
                stmt.execute("DELETE FROM students")
            }

            connectionDb.commit()
            Result.success(Unit)
        } catch (e: Exception) {
            connectionDb?.rollback()
            Result.failure(e)
        } finally {
            connectionDb?.autoCommit = true
            connectionDb?.close()
        }
    }

    override fun deleteStudent(index: Int): Result<Unit> {
        var connectionDb : Connection? = null
        return try {
            connectionDb = Database.getConnection()
            connectionDb.autoCommit = false

            connectionDb.prepareStatement("DELETE FROM students WHERE id = ?").setInt(1, index)

            connectionDb.commit()
            Result.success(Unit)
        } catch (e: Exception) {
            connectionDb?.rollback()
            Result.failure(e)
        } finally {
            connectionDb?.autoCommit = true
            connectionDb?.close()
        }
    }

    override fun insertStudent(student: String): Result<Unit> {
        var connectionDb : Connection? = null
        return try {
            connectionDb = Database.getConnection()
            connectionDb.autoCommit = false

            connectionDb.prepareStatement(
                "INSERT INTO students (name) VALUES (?)"
            ).setString(1, student)

            connectionDb.commit()
            Result.success(Unit)
        } catch (e: Exception) {
            connectionDb?.rollback()
            Result.failure(e)
        } finally {
            connectionDb?.autoCommit = true
            connectionDb?.close()
        }
    }
}