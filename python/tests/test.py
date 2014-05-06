import reddit.reddit as reddit
import unittest

class TestRedditServiceOnline(unittest.TestCase):
    def setUp(self):
        reddit.connect()
    
    def test_askreddit(self):
        posts = reddit.get_posts("askreddit")
        self.assertTrue(posts)
        post = posts[0]
        self.assertIsInstance(post, reddit.Post)
        self.assertIsInstance(post.id, str)
        self.assertIsInstance(post.author, str)
        self.assertEqual(post.subreddit.lower(), "askreddit")
        self.assertIsInstance(post.downs, int)
        self.assertIsInstance(post.ups, int)
        self.assertIsInstance(post.created, int)
        self.assertIsInstance(post.title, str)
        self.assertIsInstance(post.content, str)
        self.assertIsInstance(post.is_nsfw, bool)
        self.assertEqual(post.is_url, False)
    def test_str(self):
        post = reddit.get_posts("askreddit")[0]
        print(post, "printed successfully!")
    def test_nsfw(self):
        posts = reddit.get_posts("nsfw", allow_nsfw=False)
        self.assertFalse(posts)
        posts = reddit.get_posts("nsfw", allow_nsfw=True)
        self.assertTrue(posts)
    def test_corgis(self):
        posts = reddit.get_posts("corgis")
        self.assertTrue(posts)
        post = posts[0]
        self.assertIsInstance(post, reddit.Post)
        self.assertIsInstance(post.id, str)
        self.assertIsInstance(post.author, str)
        self.assertEqual(post.subreddit.lower(), "corgis")
        self.assertIsInstance(post.downs, int)
        self.assertIsInstance(post.ups, int)
        self.assertIsInstance(post.created, int)
        self.assertIsInstance(post.title, str)
        self.assertIsInstance(post.content, str)
        self.assertIsInstance(post.is_nsfw, bool)
        self.assertEqual(post.is_url, True)
    def test_empty_subreddit(self):
        with self.assertRaises(reddit.RedditException):
            reddit.get_posts("thisisanemptysubreddit")
    def test_empty(self):
        posts = reddit.get_posts("empty")
        self.assertFalse(posts)
    def test_comments(self):
        post = reddit.get_posts("askreddit")[1]
        comments = reddit.get_comments(post,max_depth=5, max_breadth=1)
        self.assertTrue(comments)
        self.assertTrue(comments[0].replies)
        self.assertTrue(comments[0].replies[0].replies)
        self.assertTrue(comments[0].replies[0].replies[0].replies[0])
        self.assertTrue(comments[0].replies[0].replies[0].replies[0].replies)
        self.assertFalse(comments[0].replies[0].replies[0].replies[0].replies[0].replies)
    def test_ucsantabarbara(self):
        post = reddit.get_posts("ucsantabarbara", sort_mode='random')[0]
        comments = reddit.get_comments(post)
        #self.assertTrue(comments)
            
class TestRedditEditing(unittest.TestCase):
    def setUp(self):
        reddit.connect()

    def test_cache(self):
        reddit._start_editing()
        p1 = reddit.get_posts("askreddit")[0]
        p2 = reddit.get_posts("empty")
        p3 = reddit.get_posts("corgis")[0]
        p4 = reddit.get_posts("ucsantabarbara")[0]
        self.assertEqual(len(reddit._CACHE), 4)
        reddit.get_comments(p1, max_depth=4, max_breadth=4)
        reddit.get_comments(p3, max_depth=4, max_breadth=4)
        self.assertGreater(sum([len(element) for key, element in reddit._CACHE.items()]), 4)
        
    def tearDown(self):
        reddit._save_cache("cache2.json")
        

class TestredditOffline(unittest.TestCase):
    def setUp(self):
        reddit.disconnect("cache2.json")
    
    def test_virginiatech(self):
        with self.assertRaises(reddit.RedditException):
            reddit.get_posts("virginiatech")
    def test_askreddit(self):
        p1 = reddit.get_posts("askreddit")[0]
        self.assertIsInstance(p1, reddit.Post)
   
if __name__ == '__main__':
    unittest.main()