import grpc
from django.http import JsonResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt

import calc_info_pb2_grpc
import calc_info_pb2
import molecule_pb2
import molecule_pb2_grpc


# def calcInfoData(channel):
#     stub = calc_info_pb2_grpc.CalcInfoServiceStub(channel)
#     rpc_log = stub.ListCalcInfo(calc_info_pb2.ListInfoRequest())
#     data_log = []
#     for infoData in rpc_log:
#         data = {
#             'calcinfo_id': infoData.calcInfo.calcinfo_id,
#             'smiles': infoData.calcInfo.smiles,
#             'nbasis': infoData.calcInfo.nbasis,
#             'nmo': infoData.calcInfo.nmo,
#             'nalpha': infoData.calcInfo.nalpha,
#             'nbeta': infoData.calcInfo.nbeta,
#             'natom': infoData.calcInfo.natom,
#             'energy': infoData.calcInfo.energy
#         }
#         data_log.append(data)
#     return data_log


# def readCalcInfoData(channel):
#     stub = calc_info_pb2_grpc.CalcInfoServiceStub(channel)
#     rpc_log = stub.ReadCalcInfo(calc_info_pb2.ReadInfoRequest(id='630f26040e217430d0ce8f5d'))
#     return rpc_log

#
# def readMoleculeData(channel):
#     stub = molecule_pb2_grpc.MoleculeServiceStub(channel)
#     rpc_log = stub.ReadMolecule(molecule_pb2.ReadMoleculeRequest(id='631c8b3a263f4bd508d11ef1'))
#     return rpc_log


def ListmoleculeData(channel):
    stub = molecule_pb2_grpc.MoleculeServiceStub(channel)
    rpc_log = stub.ListMolecule(molecule_pb2.ListMoleculeRequest())
    data_log = []

    for molData in rpc_log:
        data = {
            'mol_id': molData.molecule.mol_id,
            'cas_nr': molData.molecule.cas_nr,
            'smiles': molData.molecule.smiles,
            'smiles_stereo': molData.molecule.smiles_stereo,
            'inchi': molData.molecule.inchi,
            'molfile_blob_source': molData.molecule.molfile_blob_source,
            'emp_formula': molData.molecule.emp_formula,
            'emp_formula_sort': molData.molecule.emp_formula_sort,
            'emp_formula_source': molData.molecule.emp_formula_source,
            'mw': molData.molecule.mw,
            'mw_monoiso': molData.molecule.mw_monoiso,
            'rdb': molData.molecule.rdb,
            'mw_source': molData.molecule.mw_source,
            'validated_by': molData.molecule.validated_by,
            'journal': molData.molecule.journal,
            'auth_of_intr': molData.molecule.auth_of_intr,
            'jour_cit': molData.molecule.jour_cit,
            'year_publ': molData.molecule.year_publ,
            'doi_link': molData.molecule.doi_link,
            'comp_class': molData.molecule.comp_class,
            'cuniq': molData.molecule.cuniq,
            'calc_perf': molData.molecule.calc_perf,
            'org_met': molData.molecule.org_met,
            'mol_chrg': molData.molecule.mol_chrg,
            'state_ofmat': molData.molecule.state_ofmat,
            'color_white': molData.molecule.color_white,
            'color_uv': molData.molecule.color_uv,
            'absorb_max': molData.molecule.absorb_max,
            'solvent_ae': molData.molecule.solvent_ae,
            'absorb': molData.molecule.absorb,
            'conc': molData.molecule.conc,
            'extinc': molData.molecule.extinc,
            'emis_max': molData.molecule.emis_max,
            'temp_abs': molData.molecule.temp_abs,
            'emis_qy': molData.molecule.emis_qy,
            'temp_ems': molData.molecule.temp_ems,
            'lifetime': molData.molecule.lifetime,
            'temp_cv': molData.molecule.temp_cv,
            'reduc_pot': molData.molecule.reduc_pot,
            'hw_or_pk_rp': molData.molecule.hw_or_pk_rp,
            'oxid_pot': molData.molecule.oxid_pot,
            'hw_or_pk_op': molData.molecule.hw_or_pk_op,
            'solvent_cv': molData.molecule.solvent_cv,
            'electrolyte': molData.molecule.electrolyte,
            'ref_electrd': molData.molecule.ref_electrd,
            'inter_thngs': molData.molecule.inter_thngs,
            'density_20': molData.molecule.density_20,
            'density_20_source': molData.molecule.density_20_source,
            'default_warn_level': molData.molecule.default_warn_level,
            'n_20': molData.molecule.n_20,
            'n_20_source': molData.molecule.n_20_source,
            'mp_low': molData.molecule.mp_low,
            'mp_high': molData.molecule.mp_high,
            'mp_source': molData.molecule.mp_source,
            'bp_low': molData.molecule.bp_low,
            'bp_high': molData.molecule.bp_high,
            'bp_press': molData.molecule.bp_press,
            'press_unit': molData.molecule.press_unit,
            'bp_source': molData.molecule.bp_source,
            'safety_r': molData.molecule.safety_r,
            'safety_h': molData.molecule.safety_h,
            'safety_s': molData.molecule.safety_s,
            'safety_p': molData.molecule.safety_p,
            'safety_text': molData.molecule.safety_text,
            'safety_sym': molData.molecule.safety_sym,
            'safety_sym_ghs': molData.molecule.safety_sym_ghs,
            'safety_source': molData.molecule.safety_source,
            'comment_mol': molData.molecule.comment_mol,
        }
        data_log.append(data)
    return data_log

@csrf_exempt
def client(request):
    if request.method == 'GET':
        print(request)
        with grpc.insecure_channel('localhost:50051') as channel:
            # return JsonResponse(calcInfoData(channel), safe=False)
            # return HttpResponse(ListmoleculeData(channel))
            return JsonResponse(ListmoleculeData(channel), safe=False)
