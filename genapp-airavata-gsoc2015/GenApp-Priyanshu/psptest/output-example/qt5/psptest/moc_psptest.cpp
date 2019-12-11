/****************************************************************************
** Meta object code from reading C++ file 'psptest.h'
**
** Created by: The Qt Meta Object Compiler version 67 (Qt 5.4.2)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "psptest.h"
#include <QtCore/qbytearray.h>
#include <QtCore/qmetatype.h>
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'psptest.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 67
#error "This file was generated using the moc from 5.4.2. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
struct qt_meta_stringdata_mQLabel_t {
    QByteArrayData data[3];
    char stringdata[17];
};
#define QT_MOC_LITERAL(idx, ofs, len) \
    Q_STATIC_BYTE_ARRAY_DATA_HEADER_INITIALIZER_WITH_OFFSET(len, \
    qptrdiff(offsetof(qt_meta_stringdata_mQLabel_t, stringdata) + ofs \
        - idx * sizeof(QByteArrayData)) \
    )
static const qt_meta_stringdata_mQLabel_t qt_meta_stringdata_mQLabel = {
    {
QT_MOC_LITERAL(0, 0, 7), // "mQLabel"
QT_MOC_LITERAL(1, 8, 7), // "pressed"
QT_MOC_LITERAL(2, 16, 0) // ""

    },
    "mQLabel\0pressed\0"
};
#undef QT_MOC_LITERAL

static const uint qt_meta_data_mQLabel[] = {

 // content:
       7,       // revision
       0,       // classname
       0,    0, // classinfo
       1,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       1,       // signalCount

 // signals: name, argc, parameters, tag, flags
       1,    0,   19,    2, 0x06 /* Public */,

 // signals: parameters
    QMetaType::Void,

       0        // eod
};

void mQLabel::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    if (_c == QMetaObject::InvokeMetaMethod) {
        mQLabel *_t = static_cast<mQLabel *>(_o);
        switch (_id) {
        case 0: _t->pressed(); break;
        default: ;
        }
    } else if (_c == QMetaObject::IndexOfMethod) {
        int *result = reinterpret_cast<int *>(_a[0]);
        void **func = reinterpret_cast<void **>(_a[1]);
        {
            typedef void (mQLabel::*_t)();
            if (*reinterpret_cast<_t *>(func) == static_cast<_t>(&mQLabel::pressed)) {
                *result = 0;
            }
        }
    }
    Q_UNUSED(_a);
}

const QMetaObject mQLabel::staticMetaObject = {
    { &QLabel::staticMetaObject, qt_meta_stringdata_mQLabel.data,
      qt_meta_data_mQLabel,  qt_static_metacall, Q_NULLPTR, Q_NULLPTR}
};


const QMetaObject *mQLabel::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->dynamicMetaObject() : &staticMetaObject;
}

void *mQLabel::qt_metacast(const char *_clname)
{
    if (!_clname) return Q_NULLPTR;
    if (!strcmp(_clname, qt_meta_stringdata_mQLabel.stringdata))
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
    } else if (_c == QMetaObject::RegisterMethodArgumentMetaType) {
        if (_id < 1)
            *reinterpret_cast<int*>(_a[0]) = -1;
        _id -= 1;
    }
    return _id;
}

// SIGNAL 0
void mQLabel::pressed()
{
    QMetaObject::activate(this, &staticMetaObject, 0, Q_NULLPTR);
}
struct qt_meta_stringdata_mQPushButton_t {
    QByteArrayData data[1];
    char stringdata[13];
};
#define QT_MOC_LITERAL(idx, ofs, len) \
    Q_STATIC_BYTE_ARRAY_DATA_HEADER_INITIALIZER_WITH_OFFSET(len, \
    qptrdiff(offsetof(qt_meta_stringdata_mQPushButton_t, stringdata) + ofs \
        - idx * sizeof(QByteArrayData)) \
    )
static const qt_meta_stringdata_mQPushButton_t qt_meta_stringdata_mQPushButton = {
    {
QT_MOC_LITERAL(0, 0, 12) // "mQPushButton"

    },
    "mQPushButton"
};
#undef QT_MOC_LITERAL

static const uint qt_meta_data_mQPushButton[] = {

 // content:
       7,       // revision
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

void mQPushButton::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    Q_UNUSED(_o);
    Q_UNUSED(_id);
    Q_UNUSED(_c);
    Q_UNUSED(_a);
}

const QMetaObject mQPushButton::staticMetaObject = {
    { &QPushButton::staticMetaObject, qt_meta_stringdata_mQPushButton.data,
      qt_meta_data_mQPushButton,  qt_static_metacall, Q_NULLPTR, Q_NULLPTR}
};


const QMetaObject *mQPushButton::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->dynamicMetaObject() : &staticMetaObject;
}

void *mQPushButton::qt_metacast(const char *_clname)
{
    if (!_clname) return Q_NULLPTR;
    if (!strcmp(_clname, qt_meta_stringdata_mQPushButton.stringdata))
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
struct qt_meta_stringdata_mQRadioButton_t {
    QByteArrayData data[1];
    char stringdata[14];
};
#define QT_MOC_LITERAL(idx, ofs, len) \
    Q_STATIC_BYTE_ARRAY_DATA_HEADER_INITIALIZER_WITH_OFFSET(len, \
    qptrdiff(offsetof(qt_meta_stringdata_mQRadioButton_t, stringdata) + ofs \
        - idx * sizeof(QByteArrayData)) \
    )
static const qt_meta_stringdata_mQRadioButton_t qt_meta_stringdata_mQRadioButton = {
    {
QT_MOC_LITERAL(0, 0, 13) // "mQRadioButton"

    },
    "mQRadioButton"
};
#undef QT_MOC_LITERAL

static const uint qt_meta_data_mQRadioButton[] = {

 // content:
       7,       // revision
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

void mQRadioButton::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    Q_UNUSED(_o);
    Q_UNUSED(_id);
    Q_UNUSED(_c);
    Q_UNUSED(_a);
}

const QMetaObject mQRadioButton::staticMetaObject = {
    { &QRadioButton::staticMetaObject, qt_meta_stringdata_mQRadioButton.data,
      qt_meta_data_mQRadioButton,  qt_static_metacall, Q_NULLPTR, Q_NULLPTR}
};


const QMetaObject *mQRadioButton::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->dynamicMetaObject() : &staticMetaObject;
}

void *mQRadioButton::qt_metacast(const char *_clname)
{
    if (!_clname) return Q_NULLPTR;
    if (!strcmp(_clname, qt_meta_stringdata_mQRadioButton.stringdata))
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
struct qt_meta_stringdata_psptest_t {
    QByteArrayData data[122];
    char stringdata[3650];
};
#define QT_MOC_LITERAL(idx, ofs, len) \
    Q_STATIC_BYTE_ARRAY_DATA_HEADER_INITIALIZER_WITH_OFFSET(len, \
    qptrdiff(offsetof(qt_meta_stringdata_psptest_t, stringdata) + ofs \
        - idx * sizeof(QByteArrayData)) \
    )
static const qt_meta_stringdata_psptest_t qt_meta_stringdata_psptest = {
    {
QT_MOC_LITERAL(0, 0, 7), // "psptest"
QT_MOC_LITERAL(1, 8, 12), // "menu_pressed"
QT_MOC_LITERAL(2, 21, 0), // ""
QT_MOC_LITERAL(3, 22, 13), // "tools_pressed"
QT_MOC_LITERAL(4, 36, 13), // "build_pressed"
QT_MOC_LITERAL(5, 50, 16), // "interact_pressed"
QT_MOC_LITERAL(6, 67, 16), // "simulate_pressed"
QT_MOC_LITERAL(7, 84, 17), // "calculate_pressed"
QT_MOC_LITERAL(8, 102, 15), // "analyze_pressed"
QT_MOC_LITERAL(9, 118, 24), // "module_load_tools_center"
QT_MOC_LITERAL(10, 143, 26), // "module_submit_tools_center"
QT_MOC_LITERAL(11, 170, 25), // "module_reset_tools_center"
QT_MOC_LITERAL(12, 196, 18), // "error_tools_center"
QT_MOC_LITERAL(13, 215, 22), // "QProcess::ProcessError"
QT_MOC_LITERAL(14, 238, 36), // "readyReadStandardOutput_tools..."
QT_MOC_LITERAL(15, 275, 35), // "readyReadStandardError_tools_..."
QT_MOC_LITERAL(16, 311, 21), // "finished_tools_center"
QT_MOC_LITERAL(17, 333, 20), // "QProcess::ExitStatus"
QT_MOC_LITERAL(18, 354, 23), // "module_load_tools_align"
QT_MOC_LITERAL(19, 378, 25), // "module_submit_tools_align"
QT_MOC_LITERAL(20, 404, 24), // "module_reset_tools_align"
QT_MOC_LITERAL(21, 429, 17), // "error_tools_align"
QT_MOC_LITERAL(22, 447, 35), // "readyReadStandardOutput_tools..."
QT_MOC_LITERAL(23, 483, 34), // "readyReadStandardError_tools_..."
QT_MOC_LITERAL(24, 518, 20), // "finished_tools_align"
QT_MOC_LITERAL(25, 539, 26), // "module_load_tools_filetest"
QT_MOC_LITERAL(26, 566, 28), // "module_submit_tools_filetest"
QT_MOC_LITERAL(27, 595, 27), // "module_reset_tools_filetest"
QT_MOC_LITERAL(28, 623, 20), // "error_tools_filetest"
QT_MOC_LITERAL(29, 644, 38), // "readyReadStandardOutput_tools..."
QT_MOC_LITERAL(30, 683, 37), // "readyReadStandardError_tools_..."
QT_MOC_LITERAL(31, 721, 23), // "finished_tools_filetest"
QT_MOC_LITERAL(32, 745, 36), // "module_load_tools_data_interp..."
QT_MOC_LITERAL(33, 782, 38), // "module_submit_tools_data_inte..."
QT_MOC_LITERAL(34, 821, 37), // "module_reset_tools_data_inter..."
QT_MOC_LITERAL(35, 859, 30), // "error_tools_data_interpolation"
QT_MOC_LITERAL(36, 890, 48), // "readyReadStandardOutput_tools..."
QT_MOC_LITERAL(37, 939, 47), // "readyReadStandardError_tools_..."
QT_MOC_LITERAL(38, 987, 33), // "finished_tools_data_interpola..."
QT_MOC_LITERAL(39, 1021, 25), // "module_load_build_build_1"
QT_MOC_LITERAL(40, 1047, 27), // "module_submit_build_build_1"
QT_MOC_LITERAL(41, 1075, 26), // "module_reset_build_build_1"
QT_MOC_LITERAL(42, 1102, 19), // "error_build_build_1"
QT_MOC_LITERAL(43, 1122, 37), // "readyReadStandardOutput_build..."
QT_MOC_LITERAL(44, 1160, 36), // "readyReadStandardError_build_..."
QT_MOC_LITERAL(45, 1197, 22), // "finished_build_build_1"
QT_MOC_LITERAL(46, 1220, 25), // "module_load_build_build_2"
QT_MOC_LITERAL(47, 1246, 27), // "module_submit_build_build_2"
QT_MOC_LITERAL(48, 1274, 26), // "module_reset_build_build_2"
QT_MOC_LITERAL(49, 1301, 19), // "error_build_build_2"
QT_MOC_LITERAL(50, 1321, 37), // "readyReadStandardOutput_build..."
QT_MOC_LITERAL(51, 1359, 36), // "readyReadStandardError_build_..."
QT_MOC_LITERAL(52, 1396, 22), // "finished_build_build_2"
QT_MOC_LITERAL(53, 1419, 31), // "module_load_interact_interact_1"
QT_MOC_LITERAL(54, 1451, 33), // "module_submit_interact_intera..."
QT_MOC_LITERAL(55, 1485, 32), // "module_reset_interact_interact_1"
QT_MOC_LITERAL(56, 1518, 25), // "error_interact_interact_1"
QT_MOC_LITERAL(57, 1544, 43), // "readyReadStandardOutput_inter..."
QT_MOC_LITERAL(58, 1588, 42), // "readyReadStandardError_intera..."
QT_MOC_LITERAL(59, 1631, 28), // "finished_interact_interact_1"
QT_MOC_LITERAL(60, 1660, 29), // "module_load_interact_plottest"
QT_MOC_LITERAL(61, 1690, 31), // "module_submit_interact_plottest"
QT_MOC_LITERAL(62, 1722, 30), // "module_reset_interact_plottest"
QT_MOC_LITERAL(63, 1753, 23), // "error_interact_plottest"
QT_MOC_LITERAL(64, 1777, 41), // "readyReadStandardOutput_inter..."
QT_MOC_LITERAL(65, 1819, 40), // "readyReadStandardError_intera..."
QT_MOC_LITERAL(66, 1860, 26), // "finished_interact_plottest"
QT_MOC_LITERAL(67, 1887, 31), // "module_load_simulate_simulate_1"
QT_MOC_LITERAL(68, 1919, 33), // "module_submit_simulate_simula..."
QT_MOC_LITERAL(69, 1953, 32), // "module_reset_simulate_simulate_1"
QT_MOC_LITERAL(70, 1986, 25), // "error_simulate_simulate_1"
QT_MOC_LITERAL(71, 2012, 43), // "readyReadStandardOutput_simul..."
QT_MOC_LITERAL(72, 2056, 42), // "readyReadStandardError_simula..."
QT_MOC_LITERAL(73, 2099, 28), // "finished_simulate_simulate_1"
QT_MOC_LITERAL(74, 2128, 31), // "module_load_simulate_simulate_2"
QT_MOC_LITERAL(75, 2160, 33), // "module_submit_simulate_simula..."
QT_MOC_LITERAL(76, 2194, 32), // "module_reset_simulate_simulate_2"
QT_MOC_LITERAL(77, 2227, 25), // "error_simulate_simulate_2"
QT_MOC_LITERAL(78, 2253, 43), // "readyReadStandardOutput_simul..."
QT_MOC_LITERAL(79, 2297, 42), // "readyReadStandardError_simula..."
QT_MOC_LITERAL(80, 2340, 28), // "finished_simulate_simulate_2"
QT_MOC_LITERAL(81, 2369, 33), // "module_load_calculate_calcula..."
QT_MOC_LITERAL(82, 2403, 35), // "module_submit_calculate_calcu..."
QT_MOC_LITERAL(83, 2439, 34), // "module_reset_calculate_calcul..."
QT_MOC_LITERAL(84, 2474, 27), // "error_calculate_calculate_1"
QT_MOC_LITERAL(85, 2502, 45), // "readyReadStandardOutput_calcu..."
QT_MOC_LITERAL(86, 2548, 44), // "readyReadStandardError_calcul..."
QT_MOC_LITERAL(87, 2593, 30), // "finished_calculate_calculate_1"
QT_MOC_LITERAL(88, 2624, 33), // "module_load_calculate_calcula..."
QT_MOC_LITERAL(89, 2658, 35), // "module_submit_calculate_calcu..."
QT_MOC_LITERAL(90, 2694, 34), // "module_reset_calculate_calcul..."
QT_MOC_LITERAL(91, 2729, 27), // "error_calculate_calculate_2"
QT_MOC_LITERAL(92, 2757, 45), // "readyReadStandardOutput_calcu..."
QT_MOC_LITERAL(93, 2803, 44), // "readyReadStandardError_calcul..."
QT_MOC_LITERAL(94, 2848, 30), // "finished_calculate_calculate_2"
QT_MOC_LITERAL(95, 2879, 28), // "module_load_analyze_synctest"
QT_MOC_LITERAL(96, 2908, 30), // "module_submit_analyze_synctest"
QT_MOC_LITERAL(97, 2939, 29), // "module_reset_analyze_synctest"
QT_MOC_LITERAL(98, 2969, 22), // "error_analyze_synctest"
QT_MOC_LITERAL(99, 2992, 40), // "readyReadStandardOutput_analy..."
QT_MOC_LITERAL(100, 3033, 39), // "readyReadStandardError_analyz..."
QT_MOC_LITERAL(101, 3073, 25), // "finished_analyze_synctest"
QT_MOC_LITERAL(102, 3099, 35), // "browse_data_interpolation_out..."
QT_MOC_LITERAL(103, 3135, 35), // "browse_data_interpolation_out..."
QT_MOC_LITERAL(104, 3171, 24), // "browse_interact_1_input5"
QT_MOC_LITERAL(105, 3196, 24), // "browse_interact_1_input6"
QT_MOC_LITERAL(106, 3221, 27), // "browse_interact_1_outputref"
QT_MOC_LITERAL(107, 3249, 27), // "browse_interact_1_outputres"
QT_MOC_LITERAL(108, 3277, 25), // "browse_plottest_outputref"
QT_MOC_LITERAL(109, 3303, 25), // "browse_plottest_outputres"
QT_MOC_LITERAL(110, 3329, 24), // "browse_simulate_1_input4"
QT_MOC_LITERAL(111, 3354, 24), // "browse_simulate_1_input5"
QT_MOC_LITERAL(112, 3379, 25), // "browse_simulate_1_output2"
QT_MOC_LITERAL(113, 3405, 25), // "browse_simulate_1_output3"
QT_MOC_LITERAL(114, 3431, 24), // "browse_simulate_2_input1"
QT_MOC_LITERAL(115, 3456, 25), // "browse_simulate_2_output2"
QT_MOC_LITERAL(116, 3482, 25), // "browse_simulate_2_output3"
QT_MOC_LITERAL(117, 3508, 25), // "browse_simulate_2_output4"
QT_MOC_LITERAL(118, 3534, 28), // "browse_calculate_1_outputref"
QT_MOC_LITERAL(119, 3563, 28), // "browse_calculate_1_outputres"
QT_MOC_LITERAL(120, 3592, 28), // "browse_calculate_2_outputref"
QT_MOC_LITERAL(121, 3621, 28) // "browse_calculate_2_outputres"

    },
    "psptest\0menu_pressed\0\0tools_pressed\0"
    "build_pressed\0interact_pressed\0"
    "simulate_pressed\0calculate_pressed\0"
    "analyze_pressed\0module_load_tools_center\0"
    "module_submit_tools_center\0"
    "module_reset_tools_center\0error_tools_center\0"
    "QProcess::ProcessError\0"
    "readyReadStandardOutput_tools_center\0"
    "readyReadStandardError_tools_center\0"
    "finished_tools_center\0QProcess::ExitStatus\0"
    "module_load_tools_align\0"
    "module_submit_tools_align\0"
    "module_reset_tools_align\0error_tools_align\0"
    "readyReadStandardOutput_tools_align\0"
    "readyReadStandardError_tools_align\0"
    "finished_tools_align\0module_load_tools_filetest\0"
    "module_submit_tools_filetest\0"
    "module_reset_tools_filetest\0"
    "error_tools_filetest\0"
    "readyReadStandardOutput_tools_filetest\0"
    "readyReadStandardError_tools_filetest\0"
    "finished_tools_filetest\0"
    "module_load_tools_data_interpolation\0"
    "module_submit_tools_data_interpolation\0"
    "module_reset_tools_data_interpolation\0"
    "error_tools_data_interpolation\0"
    "readyReadStandardOutput_tools_data_interpolation\0"
    "readyReadStandardError_tools_data_interpolation\0"
    "finished_tools_data_interpolation\0"
    "module_load_build_build_1\0"
    "module_submit_build_build_1\0"
    "module_reset_build_build_1\0"
    "error_build_build_1\0"
    "readyReadStandardOutput_build_build_1\0"
    "readyReadStandardError_build_build_1\0"
    "finished_build_build_1\0module_load_build_build_2\0"
    "module_submit_build_build_2\0"
    "module_reset_build_build_2\0"
    "error_build_build_2\0"
    "readyReadStandardOutput_build_build_2\0"
    "readyReadStandardError_build_build_2\0"
    "finished_build_build_2\0"
    "module_load_interact_interact_1\0"
    "module_submit_interact_interact_1\0"
    "module_reset_interact_interact_1\0"
    "error_interact_interact_1\0"
    "readyReadStandardOutput_interact_interact_1\0"
    "readyReadStandardError_interact_interact_1\0"
    "finished_interact_interact_1\0"
    "module_load_interact_plottest\0"
    "module_submit_interact_plottest\0"
    "module_reset_interact_plottest\0"
    "error_interact_plottest\0"
    "readyReadStandardOutput_interact_plottest\0"
    "readyReadStandardError_interact_plottest\0"
    "finished_interact_plottest\0"
    "module_load_simulate_simulate_1\0"
    "module_submit_simulate_simulate_1\0"
    "module_reset_simulate_simulate_1\0"
    "error_simulate_simulate_1\0"
    "readyReadStandardOutput_simulate_simulate_1\0"
    "readyReadStandardError_simulate_simulate_1\0"
    "finished_simulate_simulate_1\0"
    "module_load_simulate_simulate_2\0"
    "module_submit_simulate_simulate_2\0"
    "module_reset_simulate_simulate_2\0"
    "error_simulate_simulate_2\0"
    "readyReadStandardOutput_simulate_simulate_2\0"
    "readyReadStandardError_simulate_simulate_2\0"
    "finished_simulate_simulate_2\0"
    "module_load_calculate_calculate_1\0"
    "module_submit_calculate_calculate_1\0"
    "module_reset_calculate_calculate_1\0"
    "error_calculate_calculate_1\0"
    "readyReadStandardOutput_calculate_calculate_1\0"
    "readyReadStandardError_calculate_calculate_1\0"
    "finished_calculate_calculate_1\0"
    "module_load_calculate_calculate_2\0"
    "module_submit_calculate_calculate_2\0"
    "module_reset_calculate_calculate_2\0"
    "error_calculate_calculate_2\0"
    "readyReadStandardOutput_calculate_calculate_2\0"
    "readyReadStandardError_calculate_calculate_2\0"
    "finished_calculate_calculate_2\0"
    "module_load_analyze_synctest\0"
    "module_submit_analyze_synctest\0"
    "module_reset_analyze_synctest\0"
    "error_analyze_synctest\0"
    "readyReadStandardOutput_analyze_synctest\0"
    "readyReadStandardError_analyze_synctest\0"
    "finished_analyze_synctest\0"
    "browse_data_interpolation_outputref\0"
    "browse_data_interpolation_outputres\0"
    "browse_interact_1_input5\0"
    "browse_interact_1_input6\0"
    "browse_interact_1_outputref\0"
    "browse_interact_1_outputres\0"
    "browse_plottest_outputref\0"
    "browse_plottest_outputres\0"
    "browse_simulate_1_input4\0"
    "browse_simulate_1_input5\0"
    "browse_simulate_1_output2\0"
    "browse_simulate_1_output3\0"
    "browse_simulate_2_input1\0"
    "browse_simulate_2_output2\0"
    "browse_simulate_2_output3\0"
    "browse_simulate_2_output4\0"
    "browse_calculate_1_outputref\0"
    "browse_calculate_1_outputres\0"
    "browse_calculate_2_outputref\0"
    "browse_calculate_2_outputres"
};
#undef QT_MOC_LITERAL

static const uint qt_meta_data_psptest[] = {

 // content:
       7,       // revision
       0,       // classname
       0,    0, // classinfo
     118,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // slots: name, argc, parameters, tag, flags
       1,    0,  604,    2, 0x08 /* Private */,
       3,    0,  605,    2, 0x08 /* Private */,
       4,    0,  606,    2, 0x08 /* Private */,
       5,    0,  607,    2, 0x08 /* Private */,
       6,    0,  608,    2, 0x08 /* Private */,
       7,    0,  609,    2, 0x08 /* Private */,
       8,    0,  610,    2, 0x08 /* Private */,
       9,    0,  611,    2, 0x08 /* Private */,
      10,    0,  612,    2, 0x08 /* Private */,
      11,    0,  613,    2, 0x08 /* Private */,
      12,    1,  614,    2, 0x08 /* Private */,
      14,    0,  617,    2, 0x08 /* Private */,
      15,    0,  618,    2, 0x08 /* Private */,
      16,    2,  619,    2, 0x08 /* Private */,
      18,    0,  624,    2, 0x08 /* Private */,
      19,    0,  625,    2, 0x08 /* Private */,
      20,    0,  626,    2, 0x08 /* Private */,
      21,    1,  627,    2, 0x08 /* Private */,
      22,    0,  630,    2, 0x08 /* Private */,
      23,    0,  631,    2, 0x08 /* Private */,
      24,    2,  632,    2, 0x08 /* Private */,
      25,    0,  637,    2, 0x08 /* Private */,
      26,    0,  638,    2, 0x08 /* Private */,
      27,    0,  639,    2, 0x08 /* Private */,
      28,    1,  640,    2, 0x08 /* Private */,
      29,    0,  643,    2, 0x08 /* Private */,
      30,    0,  644,    2, 0x08 /* Private */,
      31,    2,  645,    2, 0x08 /* Private */,
      32,    0,  650,    2, 0x08 /* Private */,
      33,    0,  651,    2, 0x08 /* Private */,
      34,    0,  652,    2, 0x08 /* Private */,
      35,    1,  653,    2, 0x08 /* Private */,
      36,    0,  656,    2, 0x08 /* Private */,
      37,    0,  657,    2, 0x08 /* Private */,
      38,    2,  658,    2, 0x08 /* Private */,
      39,    0,  663,    2, 0x08 /* Private */,
      40,    0,  664,    2, 0x08 /* Private */,
      41,    0,  665,    2, 0x08 /* Private */,
      42,    1,  666,    2, 0x08 /* Private */,
      43,    0,  669,    2, 0x08 /* Private */,
      44,    0,  670,    2, 0x08 /* Private */,
      45,    2,  671,    2, 0x08 /* Private */,
      46,    0,  676,    2, 0x08 /* Private */,
      47,    0,  677,    2, 0x08 /* Private */,
      48,    0,  678,    2, 0x08 /* Private */,
      49,    1,  679,    2, 0x08 /* Private */,
      50,    0,  682,    2, 0x08 /* Private */,
      51,    0,  683,    2, 0x08 /* Private */,
      52,    2,  684,    2, 0x08 /* Private */,
      53,    0,  689,    2, 0x08 /* Private */,
      54,    0,  690,    2, 0x08 /* Private */,
      55,    0,  691,    2, 0x08 /* Private */,
      56,    1,  692,    2, 0x08 /* Private */,
      57,    0,  695,    2, 0x08 /* Private */,
      58,    0,  696,    2, 0x08 /* Private */,
      59,    2,  697,    2, 0x08 /* Private */,
      60,    0,  702,    2, 0x08 /* Private */,
      61,    0,  703,    2, 0x08 /* Private */,
      62,    0,  704,    2, 0x08 /* Private */,
      63,    1,  705,    2, 0x08 /* Private */,
      64,    0,  708,    2, 0x08 /* Private */,
      65,    0,  709,    2, 0x08 /* Private */,
      66,    2,  710,    2, 0x08 /* Private */,
      67,    0,  715,    2, 0x08 /* Private */,
      68,    0,  716,    2, 0x08 /* Private */,
      69,    0,  717,    2, 0x08 /* Private */,
      70,    1,  718,    2, 0x08 /* Private */,
      71,    0,  721,    2, 0x08 /* Private */,
      72,    0,  722,    2, 0x08 /* Private */,
      73,    2,  723,    2, 0x08 /* Private */,
      74,    0,  728,    2, 0x08 /* Private */,
      75,    0,  729,    2, 0x08 /* Private */,
      76,    0,  730,    2, 0x08 /* Private */,
      77,    1,  731,    2, 0x08 /* Private */,
      78,    0,  734,    2, 0x08 /* Private */,
      79,    0,  735,    2, 0x08 /* Private */,
      80,    2,  736,    2, 0x08 /* Private */,
      81,    0,  741,    2, 0x08 /* Private */,
      82,    0,  742,    2, 0x08 /* Private */,
      83,    0,  743,    2, 0x08 /* Private */,
      84,    1,  744,    2, 0x08 /* Private */,
      85,    0,  747,    2, 0x08 /* Private */,
      86,    0,  748,    2, 0x08 /* Private */,
      87,    2,  749,    2, 0x08 /* Private */,
      88,    0,  754,    2, 0x08 /* Private */,
      89,    0,  755,    2, 0x08 /* Private */,
      90,    0,  756,    2, 0x08 /* Private */,
      91,    1,  757,    2, 0x08 /* Private */,
      92,    0,  760,    2, 0x08 /* Private */,
      93,    0,  761,    2, 0x08 /* Private */,
      94,    2,  762,    2, 0x08 /* Private */,
      95,    0,  767,    2, 0x08 /* Private */,
      96,    0,  768,    2, 0x08 /* Private */,
      97,    0,  769,    2, 0x08 /* Private */,
      98,    1,  770,    2, 0x08 /* Private */,
      99,    0,  773,    2, 0x08 /* Private */,
     100,    0,  774,    2, 0x08 /* Private */,
     101,    2,  775,    2, 0x08 /* Private */,
     102,    0,  780,    2, 0x08 /* Private */,
     103,    0,  781,    2, 0x08 /* Private */,
     104,    0,  782,    2, 0x08 /* Private */,
     105,    0,  783,    2, 0x08 /* Private */,
     106,    0,  784,    2, 0x08 /* Private */,
     107,    0,  785,    2, 0x08 /* Private */,
     108,    0,  786,    2, 0x08 /* Private */,
     109,    0,  787,    2, 0x08 /* Private */,
     110,    0,  788,    2, 0x08 /* Private */,
     111,    0,  789,    2, 0x08 /* Private */,
     112,    0,  790,    2, 0x08 /* Private */,
     113,    0,  791,    2, 0x08 /* Private */,
     114,    0,  792,    2, 0x08 /* Private */,
     115,    0,  793,    2, 0x08 /* Private */,
     116,    0,  794,    2, 0x08 /* Private */,
     117,    0,  795,    2, 0x08 /* Private */,
     118,    0,  796,    2, 0x08 /* Private */,
     119,    0,  797,    2, 0x08 /* Private */,
     120,    0,  798,    2, 0x08 /* Private */,
     121,    0,  799,    2, 0x08 /* Private */,

 // slots: parameters
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, 0x80000000 | 13,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, QMetaType::Int, 0x80000000 | 17,    2,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, 0x80000000 | 13,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, QMetaType::Int, 0x80000000 | 17,    2,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, 0x80000000 | 13,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, QMetaType::Int, 0x80000000 | 17,    2,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, 0x80000000 | 13,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, QMetaType::Int, 0x80000000 | 17,    2,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, 0x80000000 | 13,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, QMetaType::Int, 0x80000000 | 17,    2,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, 0x80000000 | 13,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, QMetaType::Int, 0x80000000 | 17,    2,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, 0x80000000 | 13,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, QMetaType::Int, 0x80000000 | 17,    2,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, 0x80000000 | 13,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, QMetaType::Int, 0x80000000 | 17,    2,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, 0x80000000 | 13,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, QMetaType::Int, 0x80000000 | 17,    2,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, 0x80000000 | 13,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, QMetaType::Int, 0x80000000 | 17,    2,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, 0x80000000 | 13,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, QMetaType::Int, 0x80000000 | 17,    2,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, 0x80000000 | 13,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, QMetaType::Int, 0x80000000 | 17,    2,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, 0x80000000 | 13,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, QMetaType::Int, 0x80000000 | 17,    2,    2,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,

       0        // eod
};

void psptest::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    if (_c == QMetaObject::InvokeMetaMethod) {
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

const QMetaObject psptest::staticMetaObject = {
    { &QFrame::staticMetaObject, qt_meta_stringdata_psptest.data,
      qt_meta_data_psptest,  qt_static_metacall, Q_NULLPTR, Q_NULLPTR}
};


const QMetaObject *psptest::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->dynamicMetaObject() : &staticMetaObject;
}

void *psptest::qt_metacast(const char *_clname)
{
    if (!_clname) return Q_NULLPTR;
    if (!strcmp(_clname, qt_meta_stringdata_psptest.stringdata))
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
    } else if (_c == QMetaObject::RegisterMethodArgumentMetaType) {
        if (_id < 118)
            *reinterpret_cast<int*>(_a[0]) = -1;
        _id -= 118;
    }
    return _id;
}
QT_END_MOC_NAMESPACE
