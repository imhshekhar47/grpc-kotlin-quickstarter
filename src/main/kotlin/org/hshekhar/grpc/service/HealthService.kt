package org.hshekhar.grpc.service

import com.google.protobuf.Empty
import com.google.protobuf.Timestamp
import org.hshekhar.grpc.Health
import org.hshekhar.grpc.HealthServiceGrpcKt
import org.hshekhar.grpc.Status
import java.util.*

class HealthService(private val date: Date = Date()) : HealthServiceGrpcKt.HealthServiceCoroutineImplBase() {

    override suspend fun getHealth(request: Empty): Health {
        return Health.newBuilder()
                .setVersion("v1")
                .setStartTime(Timestamp.newBuilder().setSeconds(date.toInstant().epochSecond))
                .setStatus(Status.UP)
                .build()
    }
}