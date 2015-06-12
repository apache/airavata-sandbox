/****************************************************************************
** Meta object code from reading C++ file 'psptest.h'
**
** Created by: The Qt Meta Object Compiler version 63 (Qt 4.8.6)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "psptest.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'psptest.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 63
#error "This file was generated using the moc from 4.8.6. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_mQLabel[] = {

 // content:
       6,       // revision
       0,       // classname
       0,    0, // classinfo
       1,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       1,       // signalCount

 // signals: signature, parameters, type, tag, flags
       9,    8,    8,    8, 0x05,

       0        // eod
};

static const char qt_meta_stringdata_mQLabel[] = {
    "mQLabel\0\0pressed()\0"
};

void mQLabel::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    if (_c == QMetaObject::InvokeMetaMethod) {
        Q_ASSERT(staticMetaObject.cast(_o));
        mQLabel *_t = static_cast<mQLabel *>(_o);
        switch (_id) {
        case 0: _t->pressed(); break;
        default: ;
        }
    }
    Q_UNUSED(_a);
}

const QMetaObjectExtraData mQLabel::staticMetaObjectExtraData = {
    0,  qt_static_metacall 
};

const QMetaObject mQLabel::staticMetaObject = {
    { &QLabel::staticMetaObject, qt_meta_stringdata_mQLabel,
      qt_meta_data_mQLabel, &staticMetaObjectExtraData }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &mQLabel::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *mQLabel::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *mQLabel::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_mQLabel))
        return static_cast<void*>(const_cast< mQLabel*>(this));
    return QLabel::qt_metacast(_clname);
}

int mQLabel::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QLabel::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        if (_id < 1)
            qt_static_metacall(this, _c, _id, _a);
        _id -= 1;
    }
    return _id;
}

// SIGNAL 0
void mQLabel::pressed()
{
    QMetaObject::activate(this, &staticMetaObject, 0, 0);
}
static const uint qt_meta_data_mQPushButton[] = {

 // content:
       6,       // revision
       0,       // classname
       0,    0, // classinfo
       0,    0, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

       0        // eod
};

static const char qt_meta_stringdata_mQPushButton[] = {
    "mQPushButton\0"
};

void mQPushButton::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    Q_UNUSED(_o);
    Q_UNUSED(_id);
    Q_UNUSED(_c);
    Q_UNUSED(_a);
}

const QMetaObjectExtraData mQPushButton::staticMetaObjectExtraData = {
    0,  qt_static_metacall 
};

const QMetaObject mQPushButton::staticMetaObject = {
    { &QPushButton::staticMetaObject, qt_meta_stringdata_mQPushButton,
      qt_meta_data_mQPushButton, &staticMetaObjectExtraData }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &mQPushButton::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *mQPushButton::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *mQPushButton::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_mQPushButton))
        return static_cast<void*>(const_cast< mQPushButton*>(this));
    return QPushButton::qt_metacast(_clname);
}

int mQPushButton::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QPushButton::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    return _id;
}
static const uint qt_meta_data_mQRadioButton[] = {

 // content:
       6,       // revision
       0,       // classname
       0,    0, // classinfo
       0,    0, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

       0        // eod
};

static const char qt_meta_stringdata_mQRadioButton[] = {
    "mQRadioButton\0"
};

void mQRadioButton::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    Q_UNUSED(_o);
    Q_UNUSED(_id);
    Q_UNUSED(_c);
    Q_UNUSED(_a);
}

const QMetaObjectExtraData mQRadioButton::staticMetaObjectExtraData = {
    0,  qt_static_metacall 
};

const QMetaObject mQRadioButton::staticMetaObject = {
    { &QRadioButton::staticMetaObject, qt_meta_stringdata_mQRadioButton,
      qt_meta_data_mQRadioButton, &staticMetaObjectExtraData }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &mQRadioButton::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *mQRadioButton::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *mQRadioButton::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_mQRadioButton))
        return static_cast<void*>(const_cast< mQRadioButton*>(this));
    return QRadioButton::qt_metacast(_clname);
}

int mQRadioButton::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QRadioButton::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    return _id;
}
static const uint qt_meta_data_psptest[] = {

 // content:
       6,       // revision
       0,       // classname
       0,    0, // classinfo
     118,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // slots: signature, parameters, type, tag, flags
       9,    8,    8,    8, 0x08,
      24,    8,    8,    8, 0x08,
      40,    8,    8,    8, 0x08,
      56,    8,    8,    8, 0x08,
      75,    8,    8,    8, 0x08,
      94,    8,    8,    8, 0x08,
     114,    8,    8,    8, 0x08,
     132,    8,    8,    8, 0x08,
     159,    8,    8,    8, 0x08,
     188,    8,    8,    8, 0x08,
     216,    8,    8,    8, 0x08,
     259,    8,    8,    8, 0x08,
     298,    8,    8,    8, 0x08,
     338,  336,    8,    8, 0x08,
     386,    8,    8,    8, 0x08,
     412,    8,    8,    8, 0x08,
     440,    8,    8,    8, 0x08,
     467,    8,    8,    8, 0x08,
     509,    8,    8,    8, 0x08,
     547,    8,    8,    8, 0x08,
     584,  336,    8,    8, 0x08,
     631,    8,    8,    8, 0x08,
     660,    8,    8,    8, 0x08,
     691,    8,    8,    8, 0x08,
     721,    8,    8,    8, 0x08,
     766,    8,    8,    8, 0x08,
     807,    8,    8,    8, 0x08,
     847,  336,    8,    8, 0x08,
     897,    8,    8,    8, 0x08,
     936,    8,    8,    8, 0x08,
     977,    8,    8,    8, 0x08,
    1017,    8,    8,    8, 0x08,
    1072,    8,    8,    8, 0x08,
    1123,    8,    8,    8, 0x08,
    1173,  336,    8,    8, 0x08,
    1233,    8,    8,    8, 0x08,
    1261,    8,    8,    8, 0x08,
    1291,    8,    8,    8, 0x08,
    1320,    8,    8,    8, 0x08,
    1364,    8,    8,    8, 0x08,
    1404,    8,    8,    8, 0x08,
    1443,  336,    8,    8, 0x08,
    1492,    8,    8,    8, 0x08,
    1520,    8,    8,    8, 0x08,
    1550,    8,    8,    8, 0x08,
    1579,    8,    8,    8, 0x08,
    1623,    8,    8,    8, 0x08,
    1663,    8,    8,    8, 0x08,
    1702,  336,    8,    8, 0x08,
    1751,    8,    8,    8, 0x08,
    1785,    8,    8,    8, 0x08,
    1821,    8,    8,    8, 0x08,
    1856,    8,    8,    8, 0x08,
    1906,    8,    8,    8, 0x08,
    1952,    8,    8,    8, 0x08,
    1997,  336,    8,    8, 0x08,
    2052,    8,    8,    8, 0x08,
    2084,    8,    8,    8, 0x08,
    2118,    8,    8,    8, 0x08,
    2151,    8,    8,    8, 0x08,
    2199,    8,    8,    8, 0x08,
    2243,    8,    8,    8, 0x08,
    2286,  336,    8,    8, 0x08,
    2339,    8,    8,    8, 0x08,
    2373,    8,    8,    8, 0x08,
    2409,    8,    8,    8, 0x08,
    2444,    8,    8,    8, 0x08,
    2494,    8,    8,    8, 0x08,
    2540,    8,    8,    8, 0x08,
    2585,  336,    8,    8, 0x08,
    2640,    8,    8,    8, 0x08,
    2674,    8,    8,    8, 0x08,
    2710,    8,    8,    8, 0x08,
    2745,    8,    8,    8, 0x08,
    2795,    8,    8,    8, 0x08,
    2841,    8,    8,    8, 0x08,
    2886,  336,    8,    8, 0x08,
    2941,    8,    8,    8, 0x08,
    2977,    8,    8,    8, 0x08,
    3015,    8,    8,    8, 0x08,
    3052,    8,    8,    8, 0x08,
    3104,    8,    8,    8, 0x08,
    3152,    8,    8,    8, 0x08,
    3199,  336,    8,    8, 0x08,
    3256,    8,    8,    8, 0x08,
    3292,    8,    8,    8, 0x08,
    3330,    8,    8,    8, 0x08,
    3367,    8,    8,    8, 0x08,
    3419,    8,    8,    8, 0x08,
    3467,    8,    8,    8, 0x08,
    3514,  336,    8,    8, 0x08,
    3571,    8,    8,    8, 0x08,
    3602,    8,    8,    8, 0x08,
    3635,    8,    8,    8, 0x08,
    3667,    8,    8,    8, 0x08,
    3714,    8,    8,    8, 0x08,
    3757,    8,    8,    8, 0x08,
    3799,  336,    8,    8, 0x08,
    3851,    8,    8,    8, 0x08,
    3889,    8,    8,    8, 0x08,
    3927,    8,    8,    8, 0x08,
    3954,    8,    8,    8, 0x08,
    3981,    8,    8,    8, 0x08,
    4011,    8,    8,    8, 0x08,
    4041,    8,    8,    8, 0x08,
    4069,    8,    8,    8, 0x08,
    4097,    8,    8,    8, 0x08,
    4124,    8,    8,    8, 0x08,
    4151,    8,    8,    8, 0x08,
    4179,    8,    8,    8, 0x08,
    4207,    8,    8,    8, 0x08,
    4234,    8,    8,    8, 0x08,
    4262,    8,    8,    8, 0x08,
    4290,    8,    8,    8, 0x08,
    4318,    8,    8,    8, 0x08,
    4349,    8,    8,    8, 0x08,
    4380,    8,    8,    8, 0x08,
    4411,    8,    8,    8, 0x08,

       0        // eod
};

static const char qt_meta_stringdata_psptest[] = {
    "psptest\0\0menu_pressed()\0tools_pressed()\0"
    "build_pressed()\0interact_pressed()\0"
    "simulate_pressed()\0calculate_pressed()\0"
    "analyze_pressed()\0module_load_tools_center()\0"
    "module_submit_tools_center()\0"
    "module_reset_tools_center()\0"
    "error_tools_center(QProcess::ProcessError)\0"
    "readyReadStandardOutput_tools_center()\0"
    "readyReadStandardError_tools_center()\0"
    ",\0finished_tools_center(int,QProcess::ExitStatus)\0"
    "module_load_tools_align()\0"
    "module_submit_tools_align()\0"
    "module_reset_tools_align()\0"
    "error_tools_align(QProcess::ProcessError)\0"
    "readyReadStandardOutput_tools_align()\0"
    "readyReadStandardError_tools_align()\0"
    "finished_tools_align(int,QProcess::ExitStatus)\0"
    "module_load_tools_filetest()\0"
    "module_submit_tools_filetest()\0"
    "module_reset_tools_filetest()\0"
    "error_tools_filetest(QProcess::ProcessError)\0"
    "readyReadStandardOutput_tools_filetest()\0"
    "readyReadStandardError_tools_filetest()\0"
    "finished_tools_filetest(int,QProcess::ExitStatus)\0"
    "module_load_tools_data_interpolation()\0"
    "module_submit_tools_data_interpolation()\0"
    "module_reset_tools_data_interpolation()\0"
    "error_tools_data_interpolation(QProcess::ProcessError)\0"
    "readyReadStandardOutput_tools_data_interpolation()\0"
    "readyReadStandardError_tools_data_interpolation()\0"
    "finished_tools_data_interpolation(int,QProcess::ExitStatus)\0"
    "module_load_build_build_1()\0"
    "module_submit_build_build_1()\0"
    "module_reset_build_build_1()\0"
    "error_build_build_1(QProcess::ProcessError)\0"
    "readyReadStandardOutput_build_build_1()\0"
    "readyReadStandardError_build_build_1()\0"
    "finished_build_build_1(int,QProcess::ExitStatus)\0"
    "module_load_build_build_2()\0"
    "module_submit_build_build_2()\0"
    "module_reset_build_build_2()\0"
    "error_build_build_2(QProcess::ProcessError)\0"
    "readyReadStandardOutput_build_build_2()\0"
    "readyReadStandardError_build_build_2()\0"
    "finished_build_build_2(int,QProcess::ExitStatus)\0"
    "module_load_interact_interact_1()\0"
    "module_submit_interact_interact_1()\0"
    "module_reset_interact_interact_1()\0"
    "error_interact_interact_1(QProcess::ProcessError)\0"
    "readyReadStandardOutput_interact_interact_1()\0"
    "readyReadStandardError_interact_interact_1()\0"
    "finished_interact_interact_1(int,QProcess::ExitStatus)\0"
    "module_load_interact_plottest()\0"
    "module_submit_interact_plottest()\0"
    "module_reset_interact_plottest()\0"
    "error_interact_plottest(QProcess::ProcessError)\0"
    "readyReadStandardOutput_interact_plottest()\0"
    "readyReadStandardError_interact_plottest()\0"
    "finished_interact_plottest(int,QProcess::ExitStatus)\0"
    "module_load_simulate_simulate_1()\0"
    "module_submit_simulate_simulate_1()\0"
    "module_reset_simulate_simulate_1()\0"
    "error_simulate_simulate_1(QProcess::ProcessError)\0"
    "readyReadStandardOutput_simulate_simulate_1()\0"
    "readyReadStandardError_simulate_simulate_1()\0"
    "finished_simulate_simulate_1(int,QProcess::ExitStatus)\0"
    "module_load_simulate_simulate_2()\0"
    "module_submit_simulate_simulate_2()\0"
    "module_reset_simulate_simulate_2()\0"
    "error_simulate_simulate_2(QProcess::ProcessError)\0"
    "readyReadStandardOutput_simulate_simulate_2()\0"
    "readyReadStandardError_simulate_simulate_2()\0"
    "finished_simulate_simulate_2(int,QProcess::ExitStatus)\0"
    "module_load_calculate_calculate_1()\0"
    "module_submit_calculate_calculate_1()\0"
    "module_reset_calculate_calculate_1()\0"
    "error_calculate_calculate_1(QProcess::ProcessError)\0"
    "readyReadStandardOutput_calculate_calculate_1()\0"
    "readyReadStandardError_calculate_calculate_1()\0"
    "finished_calculate_calculate_1(int,QProcess::ExitStatus)\0"
    "module_load_calculate_calculate_2()\0"
    "module_submit_calculate_calculate_2()\0"
    "module_reset_calculate_calculate_2()\0"
    "error_calculate_calculate_2(QProcess::ProcessError)\0"
    "readyReadStandardOutput_calculate_calculate_2()\0"
    "readyReadStandardError_calculate_calculate_2()\0"
    "finished_calculate_calculate_2(int,QProcess::ExitStatus)\0"
    "module_load_analyze_synctest()\0"
    "module_submit_analyze_synctest()\0"
    "module_reset_analyze_synctest()\0"
    "error_analyze_synctest(QProcess::ProcessError)\0"
    "readyReadStandardOutput_analyze_synctest()\0"
    "readyReadStandardError_analyze_synctest()\0"
    "finished_analyze_synctest(int,QProcess::ExitStatus)\0"
    "browse_data_interpolation_outputref()\0"
    "browse_data_interpolation_outputres()\0"
    "browse_interact_1_input5()\0"
    "browse_interact_1_input6()\0"
    "browse_interact_1_outputref()\0"
    "browse_interact_1_outputres()\0"
    "browse_plottest_outputref()\0"
    "browse_plottest_outputres()\0"
    "browse_simulate_1_input4()\0"
    "browse_simulate_1_input5()\0"
    "browse_simulate_1_output2()\0"
    "browse_simulate_1_output3()\0"
    "browse_simulate_2_input1()\0"
    "browse_simulate_2_output2()\0"
    "browse_simulate_2_output3()\0"
    "browse_simulate_2_output4()\0"
    "browse_calculate_1_outputref()\0"
    "browse_calculate_1_outputres()\0"
    "browse_calculate_2_outputref()\0"
    "browse_calculate_2_outputres()\0"
};

void psptest::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    if (_c == QMetaObject::InvokeMetaMethod) {
        Q_ASSERT(staticMetaObject.cast(_o));
        psptest *_t = static_cast<psptest *>(_o);
        switch (_id) {
        case 0: _t->menu_pressed(); break;
        case 1: _t->tools_pressed(); break;
        case 2: _t->build_pressed(); break;
        case 3: _t->interact_pressed(); break;
        case 4: _t->simulate_pressed(); break;
        case 5: _t->calculate_pressed(); break;
        case 6: _t->analyze_pressed(); break;
        case 7: _t->module_load_tools_center(); break;
        case 8: _t->module_submit_tools_center(); break;
        case 9: _t->module_reset_tools_center(); break;
        case 10: _t->error_tools_center((*reinterpret_cast< QProcess::ProcessError(*)>(_a[1]))); break;
        case 11: _t->readyReadStandardOutput_tools_center(); break;
        case 12: _t->readyReadStandardError_tools_center(); break;
        case 13: _t->finished_tools_center((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QProcess::ExitStatus(*)>(_a[2]))); break;
        case 14: _t->module_load_tools_align(); break;
        case 15: _t->module_submit_tools_align(); break;
        case 16: _t->module_reset_tools_align(); break;
        case 17: _t->error_tools_align((*reinterpret_cast< QProcess::ProcessError(*)>(_a[1]))); break;
        case 18: _t->readyReadStandardOutput_tools_align(); break;
        case 19: _t->readyReadStandardError_tools_align(); break;
        case 20: _t->finished_tools_align((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QProcess::ExitStatus(*)>(_a[2]))); break;
        case 21: _t->module_load_tools_filetest(); break;
        case 22: _t->module_submit_tools_filetest(); break;
        case 23: _t->module_reset_tools_filetest(); break;
        case 24: _t->error_tools_filetest((*reinterpret_cast< QProcess::ProcessError(*)>(_a[1]))); break;
        case 25: _t->readyReadStandardOutput_tools_filetest(); break;
        case 26: _t->readyReadStandardError_tools_filetest(); break;
        case 27: _t->finished_tools_filetest((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QProcess::ExitStatus(*)>(_a[2]))); break;
        case 28: _t->module_load_tools_data_interpolation(); break;
        case 29: _t->module_submit_tools_data_interpolation(); break;
        case 30: _t->module_reset_tools_data_interpolation(); break;
        case 31: _t->error_tools_data_interpolation((*reinterpret_cast< QProcess::ProcessError(*)>(_a[1]))); break;
        case 32: _t->readyReadStandardOutput_tools_data_interpolation(); break;
        case 33: _t->readyReadStandardError_tools_data_interpolation(); break;
        case 34: _t->finished_tools_data_interpolation((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QProcess::ExitStatus(*)>(_a[2]))); break;
        case 35: _t->module_load_build_build_1(); break;
        case 36: _t->module_submit_build_build_1(); break;
        case 37: _t->module_reset_build_build_1(); break;
        case 38: _t->error_build_build_1((*reinterpret_cast< QProcess::ProcessError(*)>(_a[1]))); break;
        case 39: _t->readyReadStandardOutput_build_build_1(); break;
        case 40: _t->readyReadStandardError_build_build_1(); break;
        case 41: _t->finished_build_build_1((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QProcess::ExitStatus(*)>(_a[2]))); break;
        case 42: _t->module_load_build_build_2(); break;
        case 43: _t->module_submit_build_build_2(); break;
        case 44: _t->module_reset_build_build_2(); break;
        case 45: _t->error_build_build_2((*reinterpret_cast< QProcess::ProcessError(*)>(_a[1]))); break;
        case 46: _t->readyReadStandardOutput_build_build_2(); break;
        case 47: _t->readyReadStandardError_build_build_2(); break;
        case 48: _t->finished_build_build_2((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QProcess::ExitStatus(*)>(_a[2]))); break;
        case 49: _t->module_load_interact_interact_1(); break;
        case 50: _t->module_submit_interact_interact_1(); break;
        case 51: _t->module_reset_interact_interact_1(); break;
        case 52: _t->error_interact_interact_1((*reinterpret_cast< QProcess::ProcessError(*)>(_a[1]))); break;
        case 53: _t->readyReadStandardOutput_interact_interact_1(); break;
        case 54: _t->readyReadStandardError_interact_interact_1(); break;
        case 55: _t->finished_interact_interact_1((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QProcess::ExitStatus(*)>(_a[2]))); break;
        case 56: _t->module_load_interact_plottest(); break;
        case 57: _t->module_submit_interact_plottest(); break;
        case 58: _t->module_reset_interact_plottest(); break;
        case 59: _t->error_interact_plottest((*reinterpret_cast< QProcess::ProcessError(*)>(_a[1]))); break;
        case 60: _t->readyReadStandardOutput_interact_plottest(); break;
        case 61: _t->readyReadStandardError_interact_plottest(); break;
        case 62: _t->finished_interact_plottest((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QProcess::ExitStatus(*)>(_a[2]))); break;
        case 63: _t->module_load_simulate_simulate_1(); break;
        case 64: _t->module_submit_simulate_simulate_1(); break;
        case 65: _t->module_reset_simulate_simulate_1(); break;
        case 66: _t->error_simulate_simulate_1((*reinterpret_cast< QProcess::ProcessError(*)>(_a[1]))); break;
        case 67: _t->readyReadStandardOutput_simulate_simulate_1(); break;
        case 68: _t->readyReadStandardError_simulate_simulate_1(); break;
        case 69: _t->finished_simulate_simulate_1((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QProcess::ExitStatus(*)>(_a[2]))); break;
        case 70: _t->module_load_simulate_simulate_2(); break;
        case 71: _t->module_submit_simulate_simulate_2(); break;
        case 72: _t->module_reset_simulate_simulate_2(); break;
        case 73: _t->error_simulate_simulate_2((*reinterpret_cast< QProcess::ProcessError(*)>(_a[1]))); break;
        case 74: _t->readyReadStandardOutput_simulate_simulate_2(); break;
        case 75: _t->readyReadStandardError_simulate_simulate_2(); break;
        case 76: _t->finished_simulate_simulate_2((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QProcess::ExitStatus(*)>(_a[2]))); break;
        case 77: _t->module_load_calculate_calculate_1(); break;
        case 78: _t->module_submit_calculate_calculate_1(); break;
        case 79: _t->module_reset_calculate_calculate_1(); break;
        case 80: _t->error_calculate_calculate_1((*reinterpret_cast< QProcess::ProcessError(*)>(_a[1]))); break;
        case 81: _t->readyReadStandardOutput_calculate_calculate_1(); break;
        case 82: _t->readyReadStandardError_calculate_calculate_1(); break;
        case 83: _t->finished_calculate_calculate_1((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QProcess::ExitStatus(*)>(_a[2]))); break;
        case 84: _t->module_load_calculate_calculate_2(); break;
        case 85: _t->module_submit_calculate_calculate_2(); break;
        case 86: _t->module_reset_calculate_calculate_2(); break;
        case 87: _t->error_calculate_calculate_2((*reinterpret_cast< QProcess::ProcessError(*)>(_a[1]))); break;
        case 88: _t->readyReadStandardOutput_calculate_calculate_2(); break;
        case 89: _t->readyReadStandardError_calculate_calculate_2(); break;
        case 90: _t->finished_calculate_calculate_2((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QProcess::ExitStatus(*)>(_a[2]))); break;
        case 91: _t->module_load_analyze_synctest(); break;
        case 92: _t->module_submit_analyze_synctest(); break;
        case 93: _t->module_reset_analyze_synctest(); break;
        case 94: _t->error_analyze_synctest((*reinterpret_cast< QProcess::ProcessError(*)>(_a[1]))); break;
        case 95: _t->readyReadStandardOutput_analyze_synctest(); break;
        case 96: _t->readyReadStandardError_analyze_synctest(); break;
        case 97: _t->finished_analyze_synctest((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< QProcess::ExitStatus(*)>(_a[2]))); break;
        case 98: _t->browse_data_interpolation_outputref(); break;
        case 99: _t->browse_data_interpolation_outputres(); break;
        case 100: _t->browse_interact_1_input5(); break;
        case 101: _t->browse_interact_1_input6(); break;
        case 102: _t->browse_interact_1_outputref(); break;
        case 103: _t->browse_interact_1_outputres(); break;
        case 104: _t->browse_plottest_outputref(); break;
        case 105: _t->browse_plottest_outputres(); break;
        case 106: _t->browse_simulate_1_input4(); break;
        case 107: _t->browse_simulate_1_input5(); break;
        case 108: _t->browse_simulate_1_output2(); break;
        case 109: _t->browse_simulate_1_output3(); break;
        case 110: _t->browse_simulate_2_input1(); break;
        case 111: _t->browse_simulate_2_output2(); break;
        case 112: _t->browse_simulate_2_output3(); break;
        case 113: _t->browse_simulate_2_output4(); break;
        case 114: _t->browse_calculate_1_outputref(); break;
        case 115: _t->browse_calculate_1_outputres(); break;
        case 116: _t->browse_calculate_2_outputref(); break;
        case 117: _t->browse_calculate_2_outputres(); break;
        default: ;
        }
    }
}

const QMetaObjectExtraData psptest::staticMetaObjectExtraData = {
    0,  qt_static_metacall 
};

const QMetaObject psptest::staticMetaObject = {
    { &QFrame::staticMetaObject, qt_meta_stringdata_psptest,
      qt_meta_data_psptest, &staticMetaObjectExtraData }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &psptest::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *psptest::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *psptest::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_psptest))
        return static_cast<void*>(const_cast< psptest*>(this));
    return QFrame::qt_metacast(_clname);
}

int psptest::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QFrame::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        if (_id < 118)
            qt_static_metacall(this, _c, _id, _a);
        _id -= 118;
    }
    return _id;
}
QT_END_MOC_NAMESPACE
