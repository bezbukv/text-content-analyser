import webapp2

class MainPage(webapp2.RequestHandler):
  def get(self):
      self.response.headers['Content-Type'] = 'text/plain'
      self.response.write('<h1>Hello, webapp2 World!</h1>')

app = webapp2.WSGIApplication([('/', MainPage)],
                              debug=True)