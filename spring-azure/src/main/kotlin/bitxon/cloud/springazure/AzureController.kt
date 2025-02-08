package bitxon.cloud.springazure

import bitxon.cloud.springazure.database.Student
import bitxon.cloud.springazure.storage.Diploma
import bitxon.cloud.springazure.storage.DiplomaStorage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AzureController(
    private val storage: DiplomaStorage
){

    @GetMapping("/students/{id}")
    fun getStudent(@PathVariable id: String): Student {
        throw UnsupportedOperationException("Not implemented")
        //return repository.findById(id) ?: throw ResponseStatusException(NOT_FOUND)
    }

    @PostMapping("/students")
    fun postStudent(@RequestBody student: Student): Student {
        throw UnsupportedOperationException("Not implemented")
        //return repository.save(student)
    }

    @GetMapping("/diplomas")
    fun getDiplomas(): List<Diploma> {
        return storage.readDiplomasArchive()
    }

    @PostMapping("/diplomas")
    fun postDiplomas(@RequestBody diplomas: List<Diploma>): List<Diploma> {
        return storage.writeDiplomasArchive(diplomas)
    }
}