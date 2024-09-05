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
    await fastify.listen({ port: parseInt(process.env.PORT || '8000') })
  } catch (err) {
    fastify.log.error(err)
    process.exit(1)
  }
}
start()

export default async (req: any, res: any) => {
  await fastify.ready();
  fastify.server.emit('request', req, res);
};
