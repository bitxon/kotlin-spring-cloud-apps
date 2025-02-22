package bitxon.cloud.springgcp.database

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : FirestoreReactiveRepository<Student>