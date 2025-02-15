package bitxon.cloud.springazure.database

import com.azure.spring.data.cosmos.core.mapping.Container
import com.azure.spring.data.cosmos.core.mapping.PartitionKey
import org.springframework.data.annotation.Id

@Container(containerName = "students", autoCreateContainer = true)
data class Student(
    @Id
    var id: String? = "",
    @PartitionKey
    var name: String = "",
    var status: String = ""
)