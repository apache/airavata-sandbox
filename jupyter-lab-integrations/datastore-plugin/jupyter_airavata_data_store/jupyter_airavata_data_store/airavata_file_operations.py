import os
from pathlib import Path
from airavata.api import Airavata
from thrift import Thrift
from thrift.transport import TSocket, TSSLSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from airavata.model.security.ttypes import AuthzToken


class AiravataFileOperations:

    basePath = "/tmp/a"
    gateway = "default"
    storageResourceId = "192.168.99.105_3bf6b8d9-67a6-4faf-b201-68550eeeb978"
    user = "default-admin"

    def __init__(self):
        try:
            self.socket = TSSLSocket.TSSLSocket('192.168.99.105', 9930, validate=False)

        except Thrift.TException as tx:
            print('%s' % (tx.message))

    def is_file(self, path):
        absPath = self.appendPaths(self.basePath, path)
        transport = TTransport.TBufferedTransport(self.socket)
        transport.open()
        protocol = TBinaryProtocol.TBinaryProtocol(transport)
        client = Airavata.Client(protocol)

        fileStructure = client.getFileDetailsFromStorage(AuthzToken(""), self.gateway, self.storageResourceId, self.user, absPath)

        transport.close()
        return fileStructure.isFile and fileStructure.isExist

    def file_exists(self, path):
        absPath = self.appendPaths(self.basePath, path)
        print("airavata_file_operations.file_exists " + absPath)

        transport = TTransport.TBufferedTransport(self.socket)
        transport.open()
        protocol = TBinaryProtocol.TBinaryProtocol(transport)
        client = Airavata.Client(protocol)

        fileStructure = client.getFileDetailsFromStorage(AuthzToken(""), self.gateway, self.storageResourceId, self.user, absPath)

        transport.close()

        return fileStructure.isFile and fileStructure.isExist

    def dir_exists(self, path):
        absPath = self.appendPaths(self.basePath, path)
        print("airavata_file_operations.dir_exists " + absPath)

        transport = TTransport.TBufferedTransport(self.socket)
        transport.open()
        protocol = TBinaryProtocol.TBinaryProtocol(transport)
        client = Airavata.Client(protocol)

        fileStructure = client.getFileDetailsFromStorage(AuthzToken(""), self.gateway, self.storageResourceId, self.user, absPath)

        transport.close()
        return (not fileStructure.isFile) and (fileStructure.isExist)

    def ls_dir(self, path):
        # returns a list of paths of the all the files inside the directory
        absPath = self.appendPaths(self.basePath, path)

        transport = TTransport.TBufferedTransport(self.socket)
        transport.open()
        protocol = TBinaryProtocol.TBinaryProtocol(transport)
        client = Airavata.Client(protocol)

        fileStructures = client.listDirectoryFromStorage(AuthzToken(""), self.gateway, self.storageResourceId, self.user, absPath)
        fileList = []
        for fileStructure in fileStructures:
            fileList.append(fileStructure.path.replace(self.basePath, "", 1))

        transport.close()
        return fileList

    def read_file(self, path):
        absPath = self.appendPaths(self.basePath, path)
        print("airavata_file_operations.read_file " + absPath)
        transport = TTransport.TBufferedTransport(self.socket)
        transport.open()
        protocol = TBinaryProtocol.TBinaryProtocol(transport)
        client = Airavata.Client(protocol)

        fileStructure = client.downloadFileFromStorage(AuthzToken(""), self.gateway, self.storageResourceId, self.user, absPath)
        print(fileStructure)
        transport.close()
        return fileStructure.content.decode("utf-8")

    def write_file(self, path, content):
        absPath = self.appendPaths(self.basePath, path)
        print("airavata_file_operations.write_file " + absPath)

        transport = TTransport.TBufferedTransport(self.socket)
        transport.open()
        protocol = TBinaryProtocol.TBinaryProtocol(transport)
        client = Airavata.Client(protocol)

        client.uploadFileToStorage(AuthzToken(""), self.gateway, self.storageResourceId, self.user, bytearray(content, "utf-8"), absPath, "text")
        transport.close()

    def create_dir(self, path):
        Path(self.basePath + path).mkdir()

    def rename_file(self, oldPath, newPath):
        os.renames(self.basePath + oldPath, self.basePath + newPath)

    def delete_file(self, path):
        os.remove(self.basePath + path)

    def appendPaths(self, first, second):
        if second.startswith("/"):
            second = second[1:]
        return first + "/" + second
