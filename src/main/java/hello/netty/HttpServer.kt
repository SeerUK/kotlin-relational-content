/**
 * This file is part of the "kotlin-relational-content" project.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the LICENSE is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package hello.netty

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler

final class HttpServer {
    companion object {
        const val PORT = 8080
    }

    val mainGroup = NioEventLoopGroup()
    val wrkrGroup = NioEventLoopGroup()

    @Throws(Exception::class)
    fun run(args: Array<String>) {
        try {
            val bootstrap = ServerBootstrap()

            bootstrap
                .group(mainGroup, wrkrGroup)
                .channel(NioServerSocketChannel::class.java)
                .handler(LoggingHandler(LogLevel.INFO))
                .childHandler(HttpServerInitializer())

            val channel = bootstrap.bind(PORT).sync().channel()

            println("Open your browser, and navigate to http://127.0.0.1:" + PORT)

            channel.closeFuture().sync()
        } catch (e: Exception) {
            println(e.message)
        } finally {
            mainGroup.shutdownGracefully()
            wrkrGroup.shutdownGracefully()
        }
    }
}
