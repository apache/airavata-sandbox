# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: calc_properties.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x15\x63\x61lc_properties.proto\x12\ncom.smiles\"\x95\x08\n\tCalcProps\x12\r\n\x05InChI\x18\x01 \x01(\t\x12\x10\n\x08InChIKey\x18\x02 \x01(\t\x12\x0e\n\x06SMILES\x18\x03 \x01(\t\x12\x17\n\x0f\x43\x61nonicalSMILES\x18\x04 \x01(\t\x12\x0b\n\x03PDB\x18\x05 \x01(\t\x12\x0b\n\x03SDF\x18\x06 \x01(\t\x12\x10\n\x08ParsedBy\x18\x07 \x01(\t\x12\x0f\n\x07\x46ormula\x18\x08 \x01(\t\x12\x0e\n\x06\x43harge\x18\t \x01(\x03\x12\x14\n\x0cMultiplicity\x18\n \x01(\x03\x12\x10\n\x08Keywords\x18\x0b \x01(\t\x12\x10\n\x08\x43\x61lcType\x18\x0c \x01(\t\x12\x0f\n\x07Methods\x18\r \x01(\t\x12\r\n\x05\x42\x61sis\x18\x0e \x01(\t\x12\x10\n\x08NumBasis\x18\x0f \x01(\x03\x12\r\n\x05NumFC\x18\x10 \x01(\x03\x12\x0f\n\x07NumVirt\x18\x11 \x01(\x03\x12\x11\n\tJobStatus\x18\x12 \x01(\t\x12\x0f\n\x07\x46inTime\x18\x13 \x01(\t\x12\x10\n\x08InitGeom\x18\x14 \x01(\t\x12\x11\n\tFinalGeom\x18\x15 \x01(\t\x12\n\n\x02PG\x18\x16 \x01(\t\x12\x0f\n\x07\x45lecSym\x18\x17 \x01(\t\x12\r\n\x05NImag\x18\x18 \x01(\x03\x12\x0e\n\x06\x45nergy\x18\x19 \x01(\x01\x12\x12\n\nEnergyKcal\x18\x1a \x01(\x01\x12\x0b\n\x03ZPE\x18\x1b \x01(\x01\x12\x0f\n\x07ZPEKcal\x18\x1c \x01(\x01\x12\n\n\x02HF\x18\x1d \x01(\x01\x12\x0e\n\x06HFKcal\x18\x1e \x01(\x01\x12\x0f\n\x07Thermal\x18\x1f \x01(\x01\x12\x13\n\x0bThermalKcal\x18  \x01(\x01\x12\x10\n\x08\x45nthalpy\x18! \x01(\x01\x12\x14\n\x0c\x45nthalpyKcal\x18\" \x01(\x01\x12\x0f\n\x07\x45ntropy\x18# \x01(\x01\x12\x13\n\x0b\x45ntropyKcal\x18$ \x01(\x01\x12\r\n\x05Gibbs\x18% \x01(\x01\x12\x11\n\tGibbsKcal\x18& \x01(\x01\x12\x0e\n\x06OrbSym\x18\' \x01(\t\x12\x0e\n\x06\x44ipole\x18( \x01(\x01\x12\x0c\n\x04\x46req\x18) \x01(\x01\x12\x11\n\tAtomWeigh\x18* \x01(\x01\x12\n\n\x02S2\x18+ \x01(\x01\x12\x13\n\x0b\x43odeVersion\x18, \x01(\t\x12\x13\n\x0b\x43\x61lcMachine\x18- \x01(\t\x12\x0e\n\x06\x43\x61lcBy\x18. \x01(\t\x12\x0f\n\x07MemCost\x18/ \x01(\t\x12\x10\n\x08TimeCost\x18\x30 \x01(\t\x12\x0f\n\x07\x43PUTime\x18\x31 \x01(\t\x12\x14\n\x0c\x43onvergenece\x18\x32 \x01(\t\x12\x11\n\tOtherinfo\x18\x33 \x01(\t\x12\x10\n\x08\x43omments\x18\x34 \x01(\t\x12\r\n\x05NAtom\x18\x35 \x01(\x03\x12\x11\n\x05Homos\x18\x36 \x03(\x01\x42\x02\x10\x01\x12\x17\n\x0bScfEnergies\x18\x37 \x03(\x01\x42\x02\x10\x01\x12\x16\n\nMoEnergies\x18\x38 \x03(\x01\x42\x02\x10\x01\x12\x16\n\nAtomCoords\x18\x39 \x03(\x01\x42\x02\x10\x01\x12\x0b\n\x03Nmo\x18: \x01(\x03\x12\x0e\n\x06NBasis\x18; \x01(\x03\"S\n\x10\x43\x61lcPropsRequest\x12\x16\n\x0e\x63\x61lcPropsQuery\x18\x01 \x01(\t\x12\'\n\x08\x63\x61lcProp\x18\x02 \x01(\x0b\x32\x15.com.smiles.CalcProps2\xb7\x02\n\x10\x43\x61lcPropsService\x12\x45\n\x0cGetCalcProps\x12\x1c.com.smiles.CalcPropsRequest\x1a\x15.com.smiles.CalcProps\"\x00\x12H\n\x0f\x43reateCalcProps\x12\x1c.com.smiles.CalcPropsRequest\x1a\x15.com.smiles.CalcProps\"\x00\x12H\n\x0fUpdateCalcProps\x12\x1c.com.smiles.CalcPropsRequest\x1a\x15.com.smiles.CalcProps\"\x00\x12H\n\x0f\x44\x65leteCalcProps\x12\x1c.com.smiles.CalcPropsRequest\x1a\x15.com.smiles.CalcProps\"\x00\x42\x02P\x01\x62\x06proto3')



_CALCPROPS = DESCRIPTOR.message_types_by_name['CalcProps']
_CALCPROPSREQUEST = DESCRIPTOR.message_types_by_name['CalcPropsRequest']
CalcProps = _reflection.GeneratedProtocolMessageType('CalcProps', (_message.Message,), {
  'DESCRIPTOR' : _CALCPROPS,
  '__module__' : 'calc_properties_pb2'
  # @@protoc_insertion_point(class_scope:com.smiles.CalcProps)
  })
_sym_db.RegisterMessage(CalcProps)

CalcPropsRequest = _reflection.GeneratedProtocolMessageType('CalcPropsRequest', (_message.Message,), {
  'DESCRIPTOR' : _CALCPROPSREQUEST,
  '__module__' : 'calc_properties_pb2'
  # @@protoc_insertion_point(class_scope:com.smiles.CalcPropsRequest)
  })
_sym_db.RegisterMessage(CalcPropsRequest)

_CALCPROPSSERVICE = DESCRIPTOR.services_by_name['CalcPropsService']
if _descriptor._USE_C_DESCRIPTORS == False:

  DESCRIPTOR._options = None
  DESCRIPTOR._serialized_options = b'P\001'
  _CALCPROPS.fields_by_name['Homos']._options = None
  _CALCPROPS.fields_by_name['Homos']._serialized_options = b'\020\001'
  _CALCPROPS.fields_by_name['ScfEnergies']._options = None
  _CALCPROPS.fields_by_name['ScfEnergies']._serialized_options = b'\020\001'
  _CALCPROPS.fields_by_name['MoEnergies']._options = None
  _CALCPROPS.fields_by_name['MoEnergies']._serialized_options = b'\020\001'
  _CALCPROPS.fields_by_name['AtomCoords']._options = None
  _CALCPROPS.fields_by_name['AtomCoords']._serialized_options = b'\020\001'
  _CALCPROPS._serialized_start=38
  _CALCPROPS._serialized_end=1083
  _CALCPROPSREQUEST._serialized_start=1085
  _CALCPROPSREQUEST._serialized_end=1168
  _CALCPROPSSERVICE._serialized_start=1171
  _CALCPROPSSERVICE._serialized_end=1482
# @@protoc_insertion_point(module_scope)
