package org.hshekhar.grpc.service

import com.google.protobuf.Empty
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import kotlin.test.BeforeTest
import kotlin.test.Test
import io.grpc.testing.GrpcCleanupRule
import org.hshekhar.grpc.HealthServiceGrpc
import org.hshekhar.grpc.Status
import kotlin.test.assertEquals


internal class HealthServiceTest {

    val grpcCleanUpRule = GrpcCleanupRule()

    @BeforeTest
    fun setUp() {
        println("Running setUp")
        val serverName = InProcessServerBuilder.generateName()
        grpcCleanUpRule.register(
                InProcessServerBuilder.forName(serverName)
                        .directExecutor()
                        .addService(HealthService())
                        .build()
                        .start())

        val blockingStub = HealthServiceGrpc.newBlockingStub(
                InProcessChannelBuilder.forName(serverName)
                        .directExecutor()
                        .build())

        val response = blockingStub.getHealth(Empty.newBuilder().build())

        assertEquals( expected = "v1", actual = response.version, message = "Version should match",)
        assertEquals( expected = Status.UP, actual = response.status, message = "Status should match",)
    }

    @Test
    fun `test HealthService_GetHealth`() {
        println("Running HealthService_GetHealth")
    }
}