class CollectedData:
    def __init__(self, twitter_data: str, bluesky_data: str, google_data: str, reddit_data: str, newsapi_data: str, references: list):
        self.twitter_data = twitter_data
        self.bluesky_data = bluesky_data
        self.google_data = google_data
        self.reddit_data = reddit_data
        self.newsapi_data = newsapi_data
        self.references = references
