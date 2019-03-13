#!/usr/bin/env python
# Reflects the requests from HTTP methods GET, POST, PUT, and DELETE
# Written by Nathan Hamiel (2010)

from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from optparse import OptionParser
from subprocess import Popen, PIPE, STDOUT
import json, ast, sys
import urlparse

class RequestHandler(BaseHTTPRequestHandler):

    def send_response2(self, *args, **kwargs):
        BaseHTTPRequestHandler.send_response(self, *args, **kwargs)
        self.send_header('Access-Control-Allow-Origin', '*')

    def do_GET(self):
        request_path = self.path
        print(request_path)
        data = urlparse.urlparse(request_path)
        formula = urlparse.parse_qs(data.query)['formula']
        formula = str(formula)
        formula = formula[1:-1]
        obligatory, optional = request_path.split("heuristik=");
        print(obligatory)
        print(optional)
        if (optional != ''):
            heuristik = urlparse.parse_qs(data.query)['heuristik']
            heuristik = str(heuristik)[1:-1]
            command="java -jar Main.jar " + formula +" "+ heuristik
        else:
            command="java -jar Main.jar " + formula
        p = Popen(command, stdout=PIPE, stderr=STDOUT, shell=True)
        self.send_response2(200)
        self.send_header("Content-type", "text/plain")
        self.end_headers()
        try :
            self.wfile.write('<table><tr><td>Iteration</td><td>Step</td><td>Resulting formula</td></tr>')
            self.wfile.write('<tr><td>0</td><td>Unprocessed formula</td><td>' + formula + '</td></tr>')
            for line in p.stdout:
                line.rstrip("\n")
                iteration, step, formula = line.split(":");
                self.wfile.write('<tr><td> ' + iteration +'</td><td>'+step+'</td><td>'+formula+'</td></tr>')
            self.wfile.write('</table>')
        except Exception as e:
            self.wfile.write('invalid')
    def do_POST(self):

        request_path = self.path

        print("\n----- Request Start ----->\n")
        print(request_path)

        request_headers = self.headers
        content_length = request_headers.getheaders('content-length')
        length = int(content_length[0]) if content_length else 0

        print(request_headers)
        print(self.rfile.read(length))
        print("<----- Request End -----\n")

        self.send_response(200)

    do_PUT = do_POST
    do_DELETE = do_GET

def main():
    port = 6253
    print('Listening on localhost:%s' % port)
    server = HTTPServer(('', port), RequestHandler)
    server.serve_forever()


if __name__ == "__main__":
    parser = OptionParser()
    parser.usage = ("Creates an http-server that will echo out any GET or POST parameters\n"
                    "Run:\n\n"
                    "   reflect")
    (options, args) = parser.parse_args()

main()
