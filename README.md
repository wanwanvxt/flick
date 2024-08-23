# Flick
A movies and TV shows player application

## Backend API
1. Search
```
<base_url>/?query={query}&page={page}
```
>example result:
```js
{
  currentPage: 1, // current page
  hasNextPage: false, // if there is a next page
  results: [
    {
      id: 'tv/watch-vincenzo-67955', // media id
      title: 'Vincenzo',
      url: 'https://flixhq.to/tv/watch-vincenzo-67955', // media url
      image: 'https://img.flixhq.to/xxrz/250x400/379/79/6b/796b32989cf1308b9e0619524af5b022/796b32989cf1308b9e0619524af5b022.jpg',
      type: 'TV Series'
    }
    {...},
    ...
  ]
}
```

2. Media info
```
<base_url>/info?id={id}
```
>example result:
```js
{
  id: 'tv/watch-vincenzo-67955', // media id
  title: 'Vincenzo',
  url: 'https://flixhq.to/tv/watch-vincenzo-67955', // media url
  image: 'https://img.flixhq.to/xxrz/250x400/379/79/6b/796b32989cf1308b9e0619524af5b022/796b32989cf1308b9e0619524af5b022.jpg',
  description: '\n' +
    '        At age of 8, Park Joo-Hyung went to Italy after he was adopted. He is now an adult and has the name of Vincenzo Cassano. ...\n' +
    '    ',
  type: 'TV Series',
  releaseDate: '2021-02-20',
  genres: [ 'Action', 'Adventure', '...' ],
  casts: [
    'Kwak Dong-yeon',
    'Kim Yeo-jin',
      ...
  ],
  tags: [
    'Watch Vincenzo Online Free,',
    'Vincenzo Online Free,',
    ...
  ],
  production: 'Studio Dragon',
  duration: '60 min',
  rating: 8.4,
  episodes: [
    {
      id: '1167571',
      title: 'Eps 1: Episode #1.1',
      number: 1,
      season: 1, // the number of episodes resets to 1 every season
      url: 'https://flixhq.to/ajax/v2/episode/servers/1167571'
    },
    {...},
    ...
  ]
}
```

3. Episode source
```
<base_url>/watch?mediaID={mediaID}&episodeID={episodeID}
```
>example result:
```js
{
  headers: { Referer: 'https://rabbitstream.net/embed-4/gBOHFKQ0sOxE?z=' },
  sources: [
    {
      url: 'https://b-g-ca-5.feetcdn.com:2223/v2-hls-playback/01b3e0bf48e643923f849702a32bd97a5c4360797759b0838c8f34597271ed8bf541e616b85a255a1320417863fe198040e65edb91d55f65c2f187d38c159aac95365664aa55f6121e784c83e8719033f811224effd0aefb9b88c77caf71b2d8943454dee7f505d5e1aae5f70dea1472a541a7c283a37782ea8253b156aad0f83701ef208196d2a5b75a864b6d6e3a2d454e55ea1885f3d5df798053a843cc223d6e41ecb1af3f6d6a07fc72a41bce18/playlist.m3u8',
      isM3U8: true
    }
  ],
  subtitles: [
    {
      url: 'https://cc.1clickcdn.ru/26/7f/267fbca84e18437aa7c7df80179b0751/ara-3.vtt',
      lang: 'Arabic - Arabic'
    },
    {
      url: 'https://cc.1clickcdn.ru/26/7f/267fbca84e18437aa7c7df80179b0751/chi-4.vtt',
      lang: 'Chinese - Chinese Simplified'
    },
    {...}
    ...
  ]
}
```

4. Recent movies/TV shows
```
<base_url>/recent?type={type}
```
>example result:
```js
[
 {
    id: 'tv/watch-yellowstone-38684',
    title: 'Yellowstone',
    url: 'https://flixhq.to/tv/watch-yellowstone-38684',
    image: 'https://img.flixhq.to/xxrz/250x400/379/86/ba/86bacd45c63959587ef16c92927fe8eb/86bacd45c63959587ef16c92927fe8eb.jpg',
    season: 'SS 5',
    latestEpisode: 'EPS 7',
    type: 'TV Series'
  },
  {
    id: 'tv/watch-his-dark-materials-34639',
    title: 'His Dark Materials',
    url: 'https://flixhq.to/tv/watch-his-dark-materials-34639',
    image: 'https://img.flixhq.to/xxrz/250x400/379/0e/41/0e41301e8f1152499dcf51253b64a29f/0e41301e8f1152499dcf51253b64a29f.jpg',
    season: 'SS 3',
    latestEpisode: 'EPS 8',
    type: 'TV Series'
  },
  {
    id: 'tv/watch-dangerous-liaisons-89965',
    title: 'Dangerous Liaisons',
    url: 'https://flixhq.to/tv/watch-dangerous-liaisons-89965',
    image: 'https://img.flixhq.to/xxrz/250x400/379/fc/5b/fc5ba1c5d4445eb29d0b002f2c8425db/fc5ba1c5d4445eb29d0b002f2c8425db.jpg',
    season: 'SS 1',
    latestEpisode: 'EPS 7',
    type: 'TV Series'
  },
  {...},
]
```

5. Trending movies/TV shows
```
<base_url>/trending?type={type}
```
>example result:
```js
[
{
    id: 'tv/watch-1923-91522',
    title: '1923',
    url: 'https://flixhq.to/tv/watch-1923-91522',
    image: 'https://img.flixhq.to/xxrz/250x400/379/96/f3/96f3c8dfd9583a855473e2e9039c8bda/96f3c8dfd9583a855473e2e9039c8bda.jpg',
    season: 'SS 1',
    latestEpisode: 'EPS 1',
    type: 'TV Series'
  },
  {
    id: 'tv/watch-the-recruit-91507',
    title: 'The Recruit',
    url: 'https://flixhq.to/tv/watch-the-recruit-91507',
    image: 'https://img.flixhq.to/xxrz/250x400/379/0e/bd/0ebd5fe83f5a5f7055089d3390727e1c/0ebd5fe83f5a5f7055089d3390727e1c.jpg',
    season: 'SS 1',
    latestEpisode: 'EPS 8',
    type: 'TV Series'
  },
  {
    id: 'tv/watch-wednesday-90553',
    title: 'Wednesday',
    url: 'https://flixhq.to/tv/watch-wednesday-90553',
    image: 'https://img.flixhq.to/xxrz/250x400/379/9b/70/9b70e344f895fd9ed9cbac46d95b21a2/9b70e344f895fd9ed9cbac46d95b21a2.jpg',
    season: 'SS 1',
    latestEpisode: 'EPS 8',
    type: 'TV Series'
  },
  {...},
]
```

6. Search by genre
```
<base_url>/genre?query={query}
```
>example result:
```js
{
  currentPage: 1,
  hasNextPage: true,
  results: [
    {
      id: 'tv/watch-are-you-sure-112516',
      title: 'Are You Sure?!',
      url: 'https://flixhq.to/tv/watch-are-you-sure-112516',
      image: 'https://img.flixhq.to/xxrz/250x400/379/56/5f/565fad67a9c9343acf4260994f565e62/565fad67a9c9343acf4260994f565e62.jpg',
      type: 'TV Series',
      season: 'SS 1',
      latestEpisode: 'EPS 2'
    },
    {
      id: 'movie/watch-mission-cross-112537',
      title: 'Mission: Cross',
      url: 'https://flixhq.to/movie/watch-mission-cross-112537',
      image: 'https://img.flixhq.to/xxrz/250x400/379/b6/24/b62476092851e487dbf78d75162e2be9/b62476092851e487dbf78d75162e2be9.jpg',
      type: 'Movie',
      releaseDate: '2024',
      duration: '100m'
    },
    {...}
  ]
}
```
