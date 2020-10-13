package org.hshekhar.grpc.service

import com.google.protobuf.Empty
import com.google.protobuf.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import org.hshekhar.grpc.Health
import org.hshekhar.grpc.HealthServiceGrpcKt
import org.hshekhar.grpc.Heartbeat
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

    override fun getHeartbeats(request: Empty): Flow<Heartbeat> = flow {
        while(true) {
            delay(1000)
            val now = Date()
            val beat = Heartbeat.newBuilder()
                    .setTimestamp(Timestamp.newBuilder().setSeconds(now.toInstant().epochSecond))
                    .setStatus(if ( now.seconds % 10 == 0) Status.DOWN else Status.UP)
                    .build()
            emit(beat)
        }
    }
}

