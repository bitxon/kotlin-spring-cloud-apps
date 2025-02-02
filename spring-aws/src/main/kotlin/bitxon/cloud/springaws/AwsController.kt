package bitxon.cloud.springaws

import bitxon.cloud.springaws.database.StudentRepository
import bitxon.cloud.springaws.database.Student
import bitxon.cloud.springaws.storage.Diploma
import bitxon.cloud.springaws.storage.DiplomaStorage
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class AwsController(
    private val repository: StudentRepository,
    private val storage: DiplomaStorage
) {

    @GetMapping("/students/{id}")
    fun getStudent(@PathVariable id: String): Student {
        return repository.findById(id) ?: throw ResponseStatusException(NOT_FOUND)
    }

    @PostMapping("/students")
    fun postStudent(@RequestBody student: Student): Student {
        return repository.save(student)
    }

    @GetMapping("/diplomas")
    fun getDiplomas(): List<Diploma> {
        return storage.readStudentsArchive()
    }

    @PostMapping("/diplomas")
    fun postDiplomas(@RequestBody diplomas: List<Diploma>): List<Diploma> {
        return storage.writeStudentsArchive(diplomas)
    }
}