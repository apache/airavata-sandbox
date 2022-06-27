import logging

import grpc
import calc_info_pb2
import calc_info_pb2_grpc


def ceateCalcInfo(channel):
    stub = calc_info_pb2_grpc.CalcInfoServiceStub(channel)
    response = stub.CreateCalcInfo(calc_info_pb2.CalcInfoRequest(calcInfoQuery="SAVE",
                                                                 calcInfo=calc_info_pb2.CalcInfo(nbasis=1002, nmo=1002,
                                                                                                 nalpha=1001,
                                                                                                 nbeta=1001, natom=1001,
                                                                                                 energy=1001,
                                                                                                 SMILES="TEST1001")))
    print(response)
    return


def updateCalcInfo(channel):
    stub = calc_info_pb2_grpc.CalcInfoServiceStub(channel)
    response = stub.UpdateCalcInfo(calc_info_pb2.CalcInfoRequest(calcInfoQuery="UPD",
                                                                 calcInfo=calc_info_pb2.CalcInfo(nbasis=1001, nmo=1002,
                                                                                                 nalpha=1001,
                                                                                                 nbeta=1001, natom=1001,
                                                                                                 energy=1001,
                                                                                                 SMILES="TEST1001")))
    print(response)
    return


def deleteCalcInfo(channel):
    stub = calc_info_pb2_grpc.CalcInfoServiceStub(channel)
    response = stub.DeleteCalcInfo(calc_info_pb2.CalcInfoRequest(calcInfoQuery="DEL",
                                                                 calcInfo=calc_info_pb2.CalcInfo(nbasis=1001, nmo=1003,
                                                                                                 nalpha=1001,
                                                                                                 nbeta=1001, natom=1001,
                                                                                                 energy=1001,
                                                                                                 SMILES="TEST1001")))
    print(response)
    return


def run():
    # NOTE(gRPC Python Team): .close() is possible on a channel and should be
    # used in circumstances in which the with statement does not fit the needs
    # of the code.
    with grpc.insecure_channel('localhost:7594') as channel:
        # ceateCalcInfo(channel)
        # updateCalcInfo(channel)
        # uncomment below line to run delete
        deleteCalcInfo(channel)


if __name__ == '__main__':
    run()
