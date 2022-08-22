# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: calc_topology.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x13\x63\x61lc_topology.proto\x12\ncom.smiles\"\xdc\x01\n\x0c\x43\x61lcTopology\x12\x0f\n\x07symbols\x18\x01 \x03(\t\x12\x10\n\x08geometry\x18\x02 \x03(\t\x12\x12\n\nmol_charge\x18\x03 \x01(\x01\x12\x18\n\x10mol_multiplicity\x18\x04 \x01(\x03\x12\x0c\n\x04name\x18\x05 \x01(\t\x12\x0f\n\x07\x63omment\x18\x06 \x01(\t\x12\x18\n\x0cmass_numbers\x18\x07 \x03(\x03\x42\x02\x10\x01\x12\x12\n\x06masses\x18\x08 \x03(\x01\x42\x02\x10\x01\x12\x19\n\ratomic_number\x18\t \x03(\x01\x42\x02\x10\x01\x12\x13\n\x0b\x61tom_labels\x18\n \x03(\t\"`\n\x13\x43\x61lcTopologyRequest\x12\x19\n\x11\x63\x61lcTopologyQuery\x18\x01 \x01(\t\x12.\n\x0c\x63\x61lcTopology\x18\x02 \x01(\x0b\x32\x18.com.smiles.CalcTopology2\xde\x02\n\x13\x43\x61lcTopologyService\x12N\n\x0fGetCalcTopology\x12\x1f.com.smiles.CalcTopologyRequest\x1a\x18.com.smiles.CalcTopology\"\x00\x12Q\n\x12\x43reateCalcTopology\x12\x1f.com.smiles.CalcTopologyRequest\x1a\x18.com.smiles.CalcTopology\"\x00\x12Q\n\x12UpdateCalcTopology\x12\x1f.com.smiles.CalcTopologyRequest\x1a\x18.com.smiles.CalcTopology\"\x00\x12Q\n\x12\x44\x65leteCalcTopology\x12\x1f.com.smiles.CalcTopologyRequest\x1a\x18.com.smiles.CalcTopology\"\x00\x42\x02P\x01\x62\x06proto3')



_CALCTOPOLOGY = DESCRIPTOR.message_types_by_name['CalcTopology']
_CALCTOPOLOGYREQUEST = DESCRIPTOR.message_types_by_name['CalcTopologyRequest']
CalcTopology = _reflection.GeneratedProtocolMessageType('CalcTopology', (_message.Message,), {
  'DESCRIPTOR' : _CALCTOPOLOGY,
  '__module__' : 'calc_topology_pb2'
  # @@protoc_insertion_point(class_scope:com.smiles.CalcTopology)
  })
_sym_db.RegisterMessage(CalcTopology)

CalcTopologyRequest = _reflection.GeneratedProtocolMessageType('CalcTopologyRequest', (_message.Message,), {
  'DESCRIPTOR' : _CALCTOPOLOGYREQUEST,
  '__module__' : 'calc_topology_pb2'
  # @@protoc_insertion_point(class_scope:com.smiles.CalcTopologyRequest)
  })
_sym_db.RegisterMessage(CalcTopologyRequest)

_CALCTOPOLOGYSERVICE = DESCRIPTOR.services_by_name['CalcTopologyService']
if _descriptor._USE_C_DESCRIPTORS == False:

  DESCRIPTOR._options = None
  DESCRIPTOR._serialized_options = b'P\001'
  _CALCTOPOLOGY.fields_by_name['mass_numbers']._options = None
  _CALCTOPOLOGY.fields_by_name['mass_numbers']._serialized_options = b'\020\001'
  _CALCTOPOLOGY.fields_by_name['masses']._options = None
  _CALCTOPOLOGY.fields_by_name['masses']._serialized_options = b'\020\001'
  _CALCTOPOLOGY.fields_by_name['atomic_number']._options = None
  _CALCTOPOLOGY.fields_by_name['atomic_number']._serialized_options = b'\020\001'
  _CALCTOPOLOGY._serialized_start=36
  _CALCTOPOLOGY._serialized_end=256
  _CALCTOPOLOGYREQUEST._serialized_start=258
  _CALCTOPOLOGYREQUEST._serialized_end=354
  _CALCTOPOLOGYSERVICE._serialized_start=357
  _CALCTOPOLOGYSERVICE._serialized_end=707
# @@protoc_insertion_point(module_scope)