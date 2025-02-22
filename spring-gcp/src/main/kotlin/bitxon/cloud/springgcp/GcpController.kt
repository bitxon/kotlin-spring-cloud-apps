package bitxon.cloud.springgcp

import bitxon.cloud.springgcp.database.Student
import bitxon.cloud.springgcp.database.StudentRepository
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class GcpController(
    private val repository: StudentRepository
){

    @GetMapping("/students/{id}")
    fun getStudent(@PathVariable id: String): Student {
        return repository.findById(id).block() ?: throw ResponseStatusException(NOT_FOUND)
    }

    @PostMapping("/students")
    fun postStudent(@RequestBody student: Student): Student {
        return repository.save(student).block() ?: throw ResponseStatusException(INTERNAL_SERVER_ERROR)
    }
}