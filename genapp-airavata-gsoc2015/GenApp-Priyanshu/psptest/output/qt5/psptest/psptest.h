#ifndef DEFINED_psptest
#define DEFINED_psptest

#include <QtCore>
#include <QtWidgets>
#include <qwt_plot.h>
#include "qwt_plot_grid.h"
#include "qwt_plot_curve.h"
// #include "qwt_legend.h"

#include "utility_routines.h"

class mQLabel : public QLabel
{
   Q_OBJECT

   public:

      mQLabel ( QWidget * parent = 0, Qt::WindowFlags f = 0 );
      mQLabel ( const QString & text, QWidget * parent = 0, Qt::WindowFlags f = 0 );
      ~mQLabel();

      mQLabel * mbuddy;
      QPixmap * pixmap_base;
      QPixmap * pixmap_hover;

   signals:
      void pressed();

   protected:
      virtual void mousePressEvent ( QMouseEvent *e );
      virtual void enterEvent      ( QEvent *e );
      virtual void leaveEvent      ( QEvent *e );
};


class mQPushButton : public QPushButton
{
   Q_OBJECT

   public:

      mQPushButton  ( QWidget *parent = 0 );
      ~mQPushButton ();
      QLabel *      mbuddy;
      QVariant      data;

   protected:
      virtual void enterEvent      ( QEvent *e );
      virtual void leaveEvent      ( QEvent *e );
};

class mQRadioButton : public QRadioButton
{
   Q_OBJECT

   public:

      mQRadioButton  ( QWidget *parent = 0 );
      ~mQRadioButton ();

      QVariant      data;
};

class psptest : public QFrame
{
   Q_OBJECT

   public:

      psptest();

   private:

      QMap < QString, QPixmap >     id_to_icon;
      QMap < QString, QString >     id_to_label;
      QVector < QWidget * >         menu_widgets;
      void                          hide_widgets( QVector < QWidget * > &widgets, bool hide = true );
      QVector < QWidget * >         panel1_widgets;
      QVector < QLayout * >         panel1_layouts;
      QVector < QWidget * >         panel1_sub_widgets;
      QVector < QLayout * >         panel1_sub_layouts;
      void                          delete_widgets_layouts( QVector < QWidget * > &widgets,
                                                            QVector < QLayout * > &layouts );

      mQLabel *                     menu_button;
      QGridLayout *                 gl_panel1;
      QGridLayout *                 gl_footer;

      QPalette *                    palette_le;
      QPalette *                    palette_cb;
      QPalette *                    palette_lbl_error;
      QPalette *                    palette_plot;

      QMap < QString, QVariant >    global_data;
      QMap < QString, QString >     global_data_types;

      QVector < QString >           panel1_inputs;
      QVector < QString >           panel1_outputs;
      QMap < QString, bool >        panel1_is_input;
      QMap < QString, QWidget * >   panel1_widget_map;
      QMap < QString, QString >     panel1_map_input;

      void                          save_value          ( const QString & id, const QString & ext );
      void                          save_last_value     ( const QString & id );
      void                          save_default_value  ( const QString & id );
      void                          save_last_values    ();

      void                          reset_value         ( const QString & id, const QString & ext );
      void                          reset_values        ( const QString & ext );
      void                          reset_output_values ( const QString & ext );
      void                          reset_default_values();
      void                          reset_last_values   ();

      QString                       get_last_value      ( const QString & id , bool & skip );
      QString                       input_to_json       ( const QString & mod, const QString & dir = "" );

      QString                       current_module_id;
      QMap < QString, QString >     process_json;

      void                          process_results     ( const QString & mod );
      QVector < QColor >            plot_colors;
      void                          push_back_color_if_ok( QColor bg, QColor set );

      void                          browse_filenames( const QString & label,
                                                      const QString & id,
                                                      bool  multiple_files = false );
      void                          spawn_app( const QString & appname, const QString & filename );

   private slots:

      void                          menu_pressed();

   private slots:
      void                          tools_pressed();

   private slots:
      void                          build_pressed();

   private slots:
      void                          interact_pressed();

   private slots:
      void                          simulate_pressed();

   private slots:
      void                          calculate_pressed();

   private slots:
      void                          analyze_pressed();

   private slots:
      void                          module_load_tools_center();
      void                          module_submit_tools_center();
      void                          module_reset_tools_center();
      void                          error_tools_center( QProcess::ProcessError );
      void                          readyReadStandardOutput_tools_center();
      void                          readyReadStandardError_tools_center();
      void                          finished_tools_center( int, QProcess::ExitStatus );

   private:
      QProcess *                    process_tools_center;

   private slots:
      void                          module_load_tools_align();
      void                          module_submit_tools_align();
      void                          module_reset_tools_align();
      void                          error_tools_align( QProcess::ProcessError );
      void                          readyReadStandardOutput_tools_align();
      void                          readyReadStandardError_tools_align();
      void                          finished_tools_align( int, QProcess::ExitStatus );

   private:
      QProcess *                    process_tools_align;

   private slots:
      void                          module_load_tools_filetest();
      void                          module_submit_tools_filetest();
      void                          module_reset_tools_filetest();
      void                          error_tools_filetest( QProcess::ProcessError );
      void                          readyReadStandardOutput_tools_filetest();
      void                          readyReadStandardError_tools_filetest();
      void                          finished_tools_filetest( int, QProcess::ExitStatus );

   private:
      QProcess *                    process_tools_filetest;

   private slots:
      void                          module_load_tools_data_interpolation();
      void                          module_submit_tools_data_interpolation();
      void                          module_reset_tools_data_interpolation();
      void                          error_tools_data_interpolation( QProcess::ProcessError );
      void                          readyReadStandardOutput_tools_data_interpolation();
      void                          readyReadStandardError_tools_data_interpolation();
      void                          finished_tools_data_interpolation( int, QProcess::ExitStatus );

   private:
      QProcess *                    process_tools_data_interpolation;

   private slots:
      void                          module_load_build_build_1();
      void                          module_submit_build_build_1();
      void                          module_reset_build_build_1();
      void                          error_build_build_1( QProcess::ProcessError );
      void                          readyReadStandardOutput_build_build_1();
      void                          readyReadStandardError_build_build_1();
      void                          finished_build_build_1( int, QProcess::ExitStatus );

   private:
      QProcess *                    process_build_build_1;

   private slots:
      void                          module_load_build_build_2();
      void                          module_submit_build_build_2();
      void                          module_reset_build_build_2();
      void                          error_build_build_2( QProcess::ProcessError );
      void                          readyReadStandardOutput_build_build_2();
      void                          readyReadStandardError_build_build_2();
      void                          finished_build_build_2( int, QProcess::ExitStatus );

   private:
      QProcess *                    process_build_build_2;

   private slots:
      void                          module_load_interact_interact_1();
      void                          module_submit_interact_interact_1();
      void                          module_reset_interact_interact_1();
      void                          error_interact_interact_1( QProcess::ProcessError );
      void                          readyReadStandardOutput_interact_interact_1();
      void                          readyReadStandardError_interact_interact_1();
      void                          finished_interact_interact_1( int, QProcess::ExitStatus );

   private:
      QProcess *                    process_interact_interact_1;

   private slots:
      void                          module_load_interact_plottest();
      void                          module_submit_interact_plottest();
      void                          module_reset_interact_plottest();
      void                          error_interact_plottest( QProcess::ProcessError );
      void                          readyReadStandardOutput_interact_plottest();
      void                          readyReadStandardError_interact_plottest();
      void                          finished_interact_plottest( int, QProcess::ExitStatus );

   private:
      QProcess *                    process_interact_plottest;

   private slots:
      void                          module_load_simulate_simulate_1();
      void                          module_submit_simulate_simulate_1();
      void                          module_reset_simulate_simulate_1();
      void                          error_simulate_simulate_1( QProcess::ProcessError );
      void                          readyReadStandardOutput_simulate_simulate_1();
      void                          readyReadStandardError_simulate_simulate_1();
      void                          finished_simulate_simulate_1( int, QProcess::ExitStatus );

   private:
      QProcess *                    process_simulate_simulate_1;

   private slots:
      void                          module_load_simulate_simulate_2();
      void                          module_submit_simulate_simulate_2();
      void                          module_reset_simulate_simulate_2();
      void                          error_simulate_simulate_2( QProcess::ProcessError );
      void                          readyReadStandardOutput_simulate_simulate_2();
      void                          readyReadStandardError_simulate_simulate_2();
      void                          finished_simulate_simulate_2( int, QProcess::ExitStatus );

   private:
      QProcess *                    process_simulate_simulate_2;

   private slots:
      void                          module_load_calculate_calculate_1();
      void                          module_submit_calculate_calculate_1();
      void                          module_reset_calculate_calculate_1();
      void                          error_calculate_calculate_1( QProcess::ProcessError );
      void                          readyReadStandardOutput_calculate_calculate_1();
      void                          readyReadStandardError_calculate_calculate_1();
      void                          finished_calculate_calculate_1( int, QProcess::ExitStatus );

   private:
      QProcess *                    process_calculate_calculate_1;

   private slots:
      void                          module_load_calculate_calculate_2();
      void                          module_submit_calculate_calculate_2();
      void                          module_reset_calculate_calculate_2();
      void                          error_calculate_calculate_2( QProcess::ProcessError );
      void                          readyReadStandardOutput_calculate_calculate_2();
      void                          readyReadStandardError_calculate_calculate_2();
      void                          finished_calculate_calculate_2( int, QProcess::ExitStatus );

   private:
      QProcess *                    process_calculate_calculate_2;

   private slots:
      void                          module_load_analyze_synctest();
      void                          module_submit_analyze_synctest();
      void                          module_reset_analyze_synctest();
      void                          error_analyze_synctest( QProcess::ProcessError );
      void                          readyReadStandardOutput_analyze_synctest();
      void                          readyReadStandardError_analyze_synctest();
      void                          finished_analyze_synctest( int, QProcess::ExitStatus );

   private:
      QProcess *                    process_analyze_synctest;

   private slots:
      void                          browse_data_interpolation_outputref();

   private slots:
      void                          browse_data_interpolation_outputres();

   private slots:
      void                          browse_interact_1_input5();

   private slots:
      void                          browse_interact_1_input6();

   private slots:
      void                          browse_interact_1_outputref();

   private slots:
      void                          browse_interact_1_outputres();

   private slots:
      void                          browse_plottest_outputref();

   private slots:
      void                          browse_plottest_outputres();

   private slots:
      void                          browse_simulate_1_input4();

   private slots:
      void                          browse_simulate_1_input5();

   private slots:
      void                          browse_simulate_1_output2();

   private slots:
      void                          browse_simulate_1_output3();

   private slots:
      void                          browse_simulate_2_input1();

   private slots:
      void                          browse_simulate_2_output2();

   private slots:
      void                          browse_simulate_2_output3();

   private slots:
      void                          browse_simulate_2_output4();

   private slots:
      void                          browse_calculate_1_outputref();

   private slots:
      void                          browse_calculate_1_outputres();

   private slots:
      void                          browse_calculate_2_outputref();

   private slots:
      void                          browse_calculate_2_outputres();
};

#endif // DEFINED_psptest
