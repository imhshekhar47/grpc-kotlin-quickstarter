package org.hshekhar.grpc.client

import com.google.protobuf.Empty
import com.google.protobuf.util.JsonFormat
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.hshekhar.grpc.Health
import org.hshekhar.grpc.HealthServiceGrpcKt
import org.hshekhar.grpc.Heartbeat
import java.io.Closeable
import java.util.concurrent.TimeUnit


class HealthServiceClient(private val channel: ManagedChannel): Closeable {

    private val stub = HealthServiceGrpcKt.HealthServiceCoroutineStub(channel)

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    suspend fun getHealth(): Health {
        return stub.getHealth(Empty.getDefaultInstance())
    }

    fun getHeartBeats(): Flow<Heartbeat> {
        return stub.getHeartbeats(Empty.getDefaultInstance())
    }
}

/*
fun main() {
    val channel = ManagedChannelBuilder.forAddress("localhost", 8081).usePlaintext().build()
    val client = HealthServiceClient(channel)

    runBlocking {
        val health = client.getHealth()
        println(JsonFormat.printer().print(health))
        client.getHeartBeats().collect { beat ->
            println(JsonFormat.printer().print(beat))
        }
    }
}
*/