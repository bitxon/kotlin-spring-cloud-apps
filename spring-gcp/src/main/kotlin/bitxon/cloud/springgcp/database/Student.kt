package bitxon.cloud.springgcp.database

import com.google.cloud.firestore.annotation.DocumentId
import com.google.cloud.spring.data.firestore.Document

@Document(collectionName = "students")
data class Student(
    @DocumentId
    val id: String? = null,
    var name: String = "",
    var status: String = ""
)