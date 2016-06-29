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
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.netty.util.CharsetUtil
import java.util.concurrent.atomic.AtomicLong

class HttpServerHandler : SimpleChannelInboundHandler<Any>() {
    val mapper = ObjectMapper()

    companion object {
        var counter = AtomicLong(0)
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is HttpRequest) {
            val request = msg
            val greeting = Greeting(counter.incrementAndGet(), "Hello World!")
            val payload = mapper.writeValueAsString(greeting)
            val buffer = StringBuilder()

            val keepAlive = HttpUtil.isKeepAlive(request);

            buffer.append(payload)

            val response = DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(buffer.toString(), CharsetUtil.UTF_8)
            )

            response.headers().set("Content-Type", "application/json; charset=UTF-8")

            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buffer.length);
                ctx.write(response);
            }
        }
    }

    @Throws(Exception::class)
    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    data class Greeting(val id: Long, val content: String)
}
