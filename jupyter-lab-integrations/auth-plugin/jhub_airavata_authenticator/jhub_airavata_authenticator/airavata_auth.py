from tornado import gen
from jupyterhub.auth import Authenticator

class Airav:ataAuthenticator(Authenticator):

    @gen.coroutine
    def authenticate(self, handler, data):
        if data['username'] == 'admin':
            return data['username']