#!/usr/bin/env python
# Reflects the requests from HTTP methods GET, POST, PUT, and DELETE
# Written by Nathan Hamiel (2010)

from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from optparse import OptionParser
from subprocess import Popen, PIPE, STDOUT
import json, ast, sys
import urlparse

class RequestHandler(BaseHTTPRequestHandler):
	#custom response for Access Control Allow Origin (Might be unnecessary on other networks)
    def send_response2(self, *args, **kwargs):
        BaseHTTPRequestHandler.send_response(self, *args, **kwargs)
        self.send_header('Access-Control-Allow-Origin', '*')

   #Handling GET-Requests -> only relevant case for this project
    def do_GET(self):
        request_path = self.path
        data = urlparse.urlparse(request_path)  #extracting formula from request path
        formula = urlparse.parse_qs(data.query)['formula'] #extracting formula from request path
        formula = str(formula) #extracting formula from request path
        formula = formula[1:-1] #extracting formula from request path
        obligatory, optional = request_path.split("heuristik=") #extracting heuristik from path
        if (optional != ''):
            heuristik = urlparse.parse_qs(data.query)['heuristik'] #extracting heuristik from path if heuristik is given
            heuristik = str(heuristik)[1:-1] 
            command="java -jar Main.jar " + formula +" "+ heuristik #start java app with heuristik
        else:
            command="java -jar Main.jar " + formula #start java app without heuristik
        p = Popen(command, stdout=PIPE, stderr=STDOUT, shell=True) #run java app with command
        self.send_response2(200) 
        self.send_header("Content-type", "text/plain") #response is of type text\plain
        self.end_headers()
        try : 
            self.wfile.write('<table><tr><td>Iteration</td><td>Step</td><td>Resulting formula</td></tr>') #building html table in response
            self.wfile.write('<tr><td>0</td><td>Unprocessed formula</td><td>' + formula + '</td></tr>') #building html table in response
            for line in p.stdout: #for every line returned by java program, build one table row
                line.rstrip("\n")
                iteration, step, formula = line.split(":") #splitting line to build table columns
                self.wfile.write('<tr><td> ' + iteration +'</td><td>'+step+'</td><td>'+formula+'</td></tr>') #building rows in response
            self.wfile.write('</table>')
        except Exception as e: #catch invalid formulas by catching returned java error
            self.wfile.write('invalid') #add invalid tag to reponse

def main():
    port = 6253 #listening on port that react uses for request
    print('Listening on localhost:%s' % port)
    server = HTTPServer(('', port), RequestHandler) #using HTTPServer tool
    server.serve_forever() #can run all the time, only needs to be started once

main()
