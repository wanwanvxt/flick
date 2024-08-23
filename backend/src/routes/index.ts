import { FastifyInstance, RegisterOptions } from "fastify"
import { MOVIES } from '@consumet/extensions'

const movieProvider = new MOVIES.FlixHQ()

const routes = async (fastify: FastifyInstance, options: RegisterOptions) => {
  /** /?query={query}&page={page} */
  fastify.get('/', async (request, reply) => {
    const query = decodeURIComponent((request.query as { query: string }).query)
    const page = (request.query as { page: number }).page || 1

    movieProvider
      .search(query, page)
      .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
      .catch((err) => reply.code(500).send(err))

    return reply
  })

  /** /info?id={id} */
  fastify.get('/info', async (request, reply) => {
    const mediaID = (request.query as { id: string }).id

    movieProvider
      .fetchMediaInfo(mediaID)
      .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
      .catch((err) => reply.code(500).send(err))

    return reply
  })

  /** /watch?mediaID={mediaID}&episodeID={episodeID} */
  fastify.get('/watch', async (request, reply) => {
    const mediaID = (request.query as { mediaID: string }).mediaID
    const episodeID = (request.query as { episodeID: string }).episodeID

    movieProvider
      .fetchEpisodeSources(episodeID, mediaID)
      .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
      .catch((err) => reply.code(500).send(err))

    return reply
  })

  /** /recent */
  fastify.get('/recent', async (request, reply) => {
    const type = (request.query as { type: string }).type
    if (type != 'movie' && type != 'tvshow') {
      reply.code(400).send()
    }

    if (type == 'movie') {
      movieProvider
        .fetchRecentMovies()
        .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
        .catch((err) => reply.code(500).send(err))
    } else if (type == 'tvshow') {
      movieProvider
        .fetchRecentTvShows()
        .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
        .catch((err) => reply.code(500).send(err))
    }

    return reply
  })

  /** /trending */
  fastify.get('/trending', async (request, reply) => {
    const type = (request.query as { type: string }).type
    if (type != 'movie' && type != 'tvshow') {
      reply.code(400).send()
    }

    if (type == 'movie') {
      movieProvider
        .fetchTrendingMovies()
        .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
        .catch((err) => reply.code(500).send(err))
    } else if (type == 'tvshow') {
      movieProvider
        .fetchTrendingTvShows()
        .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
        .catch((err) => reply.code(500).send(err))
    }

    return reply
  })

  /** /genre?genre={genre}&page={page} */
  fastify.get('/genre', async (request, reply) => {
    const genre = decodeURIComponent((request.query as { genre: string }).genre)
    const page = (request.query as { page: number }).page || 1

    movieProvider
      .fetchByGenre(genre, page)
      .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
      .catch((err) => reply.code(500).send(err))

    return reply
  })
}

export default routes
