package bitxon.cloud.springazure.database

import com.azure.spring.data.cosmos.repository.CosmosRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StudentRepository : CosmosRepository<Student, String> {
    override fun findById(id: String): Optional<Student>
}