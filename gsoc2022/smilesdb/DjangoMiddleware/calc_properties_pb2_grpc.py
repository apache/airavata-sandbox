# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
"""Client and server classes corresponding to protobuf-defined services."""
import grpc

import calc_properties_pb2 as calc__properties__pb2


class CalcPropsServiceStub(object):
    """Missing associated documentation comment in .proto file."""

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.GetCalcProps = channel.unary_unary(
                '/com.smiles.CalcPropsService/GetCalcProps',
                request_serializer=calc__properties__pb2.CalcPropsRequest.SerializeToString,
                response_deserializer=calc__properties__pb2.CalcProps.FromString,
                )
        self.CreateCalcProps = channel.unary_unary(
                '/com.smiles.CalcPropsService/CreateCalcProps',
                request_serializer=calc__properties__pb2.CalcPropsRequest.SerializeToString,
                response_deserializer=calc__properties__pb2.CalcProps.FromString,
                )
        self.UpdateCalcProps = channel.unary_unary(
                '/com.smiles.CalcPropsService/UpdateCalcProps',
                request_serializer=calc__properties__pb2.CalcPropsRequest.SerializeToString,
                response_deserializer=calc__properties__pb2.CalcProps.FromString,
                )
        self.DeleteCalcProps = channel.unary_unary(
                '/com.smiles.CalcPropsService/DeleteCalcProps',
                request_serializer=calc__properties__pb2.CalcPropsRequest.SerializeToString,
                response_deserializer=calc__properties__pb2.CalcProps.FromString,
                )


class CalcPropsServiceServicer(object):
    """Missing associated documentation comment in .proto file."""

    def GetCalcProps(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def CreateCalcProps(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def UpdateCalcProps(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def DeleteCalcProps(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_CalcPropsServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
            'GetCalcProps': grpc.unary_unary_rpc_method_handler(
                    servicer.GetCalcProps,
                    request_deserializer=calc__properties__pb2.CalcPropsRequest.FromString,
                    response_serializer=calc__properties__pb2.CalcProps.SerializeToString,
            ),
            'CreateCalcProps': grpc.unary_unary_rpc_method_handler(
                    servicer.CreateCalcProps,
                    request_deserializer=calc__properties__pb2.CalcPropsRequest.FromString,
                    response_serializer=calc__properties__pb2.CalcProps.SerializeToString,
            ),
            'UpdateCalcProps': grpc.unary_unary_rpc_method_handler(
                    servicer.UpdateCalcProps,
                    request_deserializer=calc__properties__pb2.CalcPropsRequest.FromString,
                    response_serializer=calc__properties__pb2.CalcProps.SerializeToString,
            ),
            'DeleteCalcProps': grpc.unary_unary_rpc_method_handler(
                    servicer.DeleteCalcProps,
                    request_deserializer=calc__properties__pb2.CalcPropsRequest.FromString,
                    response_serializer=calc__properties__pb2.CalcProps.SerializeToString,
            ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
            'com.smiles.CalcPropsService', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))


 # This class is part of an EXPERIMENTAL API.
class CalcPropsService(object):
    """Missing associated documentation comment in .proto file."""

    @staticmethod
    def GetCalcProps(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/com.smiles.CalcPropsService/GetCalcProps',
            calc__properties__pb2.CalcPropsRequest.SerializeToString,
            calc__properties__pb2.CalcProps.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def CreateCalcProps(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/com.smiles.CalcPropsService/CreateCalcProps',
            calc__properties__pb2.CalcPropsRequest.SerializeToString,
            calc__properties__pb2.CalcProps.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def UpdateCalcProps(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/com.smiles.CalcPropsService/UpdateCalcProps',
            calc__properties__pb2.CalcPropsRequest.SerializeToString,
            calc__properties__pb2.CalcProps.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def DeleteCalcProps(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/com.smiles.CalcPropsService/DeleteCalcProps',
            calc__properties__pb2.CalcPropsRequest.SerializeToString,
            calc__properties__pb2.CalcProps.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)
