from django.shortcuts import render

# parsing data from the client
from rest_framework.parsers import JSONParser
# To bypass having a CSRF token
from django.views.decorators.csrf import csrf_exempt

# for sending response to the client
from django.http import HttpResponse, JsonResponse

import calc_info_pb2
import calc_info_pb2_grpc
import grpc
import json


def getCalcInfo(channel) :
    stub = calc_info_pb2_grpc.CalcInfoServiceStub(channel)
    response = stub.GetCalcInfo(calc_info_pb2.CalcInfoRequest(calcInfoQuery="TEST1", calcInfo=calc_info_pb2.CalcInfo(nbasis=1001, nmo=1001, nalpha=1001, nbeta=1001, natom=1001, energy=1001, SMILES="TEST1001")))
    data = {'SMILES': response.SMILES,
            'nbasis': response.nbasis,
            'nmo': response.nmo,
            'nalpha': response.nalpha,
            'nbeta': response.nbeta,
            'natom': response.natom,
            'energy': response.energy
            }
    return json.dumps(data)



@csrf_exempt
def calcinfo(request):
    if request.method == 'GET':
        print(request)
        with grpc.insecure_channel('localhost:7594') as channel:
            return JsonResponse(getCalcInfo(channel), safe=False)
    elif request.method == 'POST':
        # parse the incoming information
        return JsonResponse("errors", status=400)
