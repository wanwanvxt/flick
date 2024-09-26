import { FastifyInstance, RegisterOptions } from "fastify"
import { MOVIES, StreamingServers } from '@consumet/extensions'

const movieProvider = new MOVIES.MovieHdWatch()
// const movieProvider2 = new MOVIES.FlixHQ()

const routes = async (fastify: FastifyInstance, options: RegisterOptions) => {
  /** /?query={query}&page={page} */
  fastify.get('/', async (request, reply) => {
    const query = decodeURIComponent((request.query as { query: string }).query)
    const page = (request.query as { page: number }).page || 1

    if (!query || !Number.isFinite(+page)) {
      reply.code(400).send()
      return reply
    }

    movieProvider
      .search(query, page)
      .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
      .catch((err) => reply.code(500).send(err))

    return reply
  })

  /** /info?id={id} */
  fastify.get('/info', async (request, reply) => {
    const mediaID = (request.query as { id: string }).id

    if (!mediaID) {
      reply.code(400).send()
      return reply
    }

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

    if (!mediaID || !episodeID) {
      reply.code(400).send()
      return reply
    }

    movieProvider
      .fetchEpisodeSources(episodeID, mediaID, StreamingServers.VidCloud)
      .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
      .catch((err) => reply.code(500).send(err))

    return reply
  })

  /** /recent */
  fastify.get('/recent', async (request, reply) => {
    const type = (request.query as { type: string }).type

    switch (type) {
      case 'movie':
        movieProvider
          .fetchRecentMovies()
          .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
          .catch((err) => reply.code(500).send(err))
        break

      case 'tvshow':
        movieProvider
          .fetchRecentTvShows()
          .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
          .catch((err) => reply.code(500).send(err))
        break

      default:
        reply.code(400).send()
        break
    }

    return reply
  })

  /** /trending */
  fastify.get('/trending', async (request, reply) => {
    const type = (request.query as { type: string }).type

    switch (type) {
      case 'movie':
        movieProvider
          .fetchTrendingMovies()
          .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
          .catch((err) => reply.code(500).send(err))
        break

      case 'tvshow':
        movieProvider
          .fetchTrendingTvShows()
          .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
          .catch((err) => reply.code(500).send(err))
        break

      default:
        reply.code(400).send()
        break
    }

    return reply
  })

  /** /genre?genre={genre}&page={page} */
  /*
  fastify.get('/genre', async (request, reply) => {
    const genre = decodeURIComponent((request.query as { genre: string }).genre)
    const page = (request.query as { page: number }).page || 1

    if (!genre || !Number.isFinite(+page)) {
      reply.code(400).send()
      return reply
    }

    movieProvider2
      .fetchByGenre(genre, page)
      .then((data) => data ? reply.code(200).send(data) : reply.code(404).send())
      .catch((err) => reply.code(500).send(err))

    return reply
  })*/
}

export default routes
