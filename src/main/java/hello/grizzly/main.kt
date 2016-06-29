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

package hello.grizzly

import com.fasterxml.jackson.databind.ObjectMapper
import org.glassfish.grizzly.http.server.HttpHandler
import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.grizzly.http.server.Request
import org.glassfish.grizzly.http.server.Response
import java.util.concurrent.atomic.AtomicLong

var counter = AtomicLong(0)

fun main2(args: Array<String>) {
    val mapper = ObjectMapper()
    val server = HttpServer.createSimpleServer()

    server.serverConfiguration.addHttpHandler(object : HttpHandler() {
        @Throws(Exception::class)
        override fun service(req: Request, res: Response) {
            val greeting = Greeting(counter.incrementAndGet(), "Hello World!")
            val payload = mapper.writeValueAsString(greeting)

            res.contentType = "application/json"
            res.contentLength = payload.length
            res.writer.write(payload)
        }
    }, "/greeting")

    try {
        server.start()

        println("Press any key to stop the server...")
        readLine()
    } catch (e: Exception) {
        println(e.message)
    }
}

data class Greeting(val id: Long, val content: String)
