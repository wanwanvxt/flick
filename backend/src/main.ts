import Fastify from 'fastify'
import dotenv from 'dotenv'
import routes from './routes'

dotenv.config()

const fastify = Fastify({
  logger: true,
})

fastify.register(routes)

const start = async () => {
  try {
    await fastify.listen({ port: parseInt(process.env.PORT || '8000'), host: '0.0.0.0' })
  } catch (err) {
    fastify.log.error(err)
    process.exit(1)
  }
}
start()
