package org.hshekhar.grpc

import io.grpc.ServerBuilder
import io.grpc.protobuf.services.ProtoReflectionService
import org.hshekhar.grpc.service.HealthService


class AppServer(private val port: Int) {
    private val server = ServerBuilder.forPort(port)
            .addService(HealthService())
            .addService(ProtoReflectionService.newInstance())
            .build()

    private fun stop() {
        server.shutdown()
    }

    fun start() {
        server.start()
        println("Started ${AppServer::javaClass} server, listening on ${port}")
        Runtime.getRuntime().addShutdownHook(Thread{
            println("JVM Termination intercepted. Shutting down server")
            this@AppServer.stop()
            println("${AppServer::javaClass} Stopped")
        })
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }
}

fun main(args: Array<String>) {
    val port = if (args.isNotEmpty())  args[0].toIntOrNull()?:8081 else 8081
    val server = AppServer(port)
    server.start()
    server.blockUntilShutdown()
}
