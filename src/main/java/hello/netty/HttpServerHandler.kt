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

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.HttpRequest
import java.util.concurrent.atomic.AtomicLong

class HttpServerHandler : SimpleChannelInboundHandler<Any>() {
    var counter = AtomicLong(0)
    val mapper = ObjectMapper()

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is HttpRequest) {
            //val request = msg
            val greeting = Greeting(counter.incrementAndGet(), "Hello World!")
            val payload = mapper.writeValueAsString(greeting)
            val response = StringBuilder()

            response.append(payload)

            ctx.writeAndFlush(response.toString())
        }
    }

    data class Greeting(val id: Long, val content: String)
}
