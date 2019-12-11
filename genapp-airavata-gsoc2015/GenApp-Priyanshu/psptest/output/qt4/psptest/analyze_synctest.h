void psptest::module_load_analyze_synctest()
{
   delete_widgets_layouts( panel1_sub_widgets, panel1_sub_layouts );
   panel1_widget_map.clear();
   panel1_inputs.clear();
   panel1_outputs.clear();
   panel1_map_input.clear();
   panel1_is_input.clear();

   current_module_id = "synctest";

   {
      QLabel * lbl = new QLabel( "", this );
      lbl->setMaximumHeight( 12 );
      lbl->show();
      gl_panel1->addWidget( lbl, 1, 0 );
      panel1_sub_widgets.push_back( lbl );

   }

   QGridLayout * gl = new QGridLayout( 0 ); //, 1, 1, 3, 3 );
   {
      QLabel * lbl = new QLabel( "", this );
      lbl->setMaximumHeight( 12 );
      lbl->show();
      gl->addWidget( lbl, 0, 2 );
      panel1_sub_widgets.push_back( lbl );
   }

   int ppos = 0; // the base position in the gl_sub_panel

   {
      QLabel * lbl = new QLabel( "Base repeater  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      QLineEdit * le = new QLineEdit( this );
      le->setMaximumHeight( 22 );
//      le->setPalette( *palette_le );
      le->setValidator( new QIntValidator( le ) );
      le->setText( QString( "%1" ).arg( 0 ) ); 
      ((QIntValidator*)le->validator())->setBottom( 0 ); 
      ((QIntValidator*)le->validator())->setTop( 5 ); 
      le->show();
      gl->addWidget( le, ppos, 1 );
      panel1_sub_widgets.push_back( le );

      QString id = "synctest:integerrepeater0";
      global_data_types[ id ] = "le";
      panel1_widget_map[ id ] = le;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      mQLabel * lbl = new mQLabel( "a synced sub set  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      QCheckBox * cb = new QCheckBox( this );
      cb->setMaximumHeight( 22 );
//      cb->setPalette( *palette_cb );
     
      cb->show();
      gl->addWidget( cb, ppos, 1 );
      panel1_sub_widgets.push_back( cb );
      connect( lbl, SIGNAL( pressed() ), cb, SLOT( toggle() ) );

      QString id = "synctest:checkboxrepeater1";
      global_data_types[ id ] = "cb";
      panel1_widget_map[ id ] = cb;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      QLabel * lbl = new QLabel( "How about a number of repeatings  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      QLineEdit * le = new QLineEdit( this );
      le->setMaximumHeight( 22 );
//      le->setPalette( *palette_le );
      le->setValidator( new QIntValidator( le ) );
      le->setText( QString( "%1" ).arg( 0 ) ); 
      ((QIntValidator*)le->validator())->setBottom( 0 ); 
      ((QIntValidator*)le->validator())->setTop( 5 ); 
      le->show();
      gl->addWidget( le, ppos, 1 );
      panel1_sub_widgets.push_back( le );

      QString id = "synctest:integerrepeater1";
      global_data_types[ id ] = "le";
      panel1_widget_map[ id ] = le;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      mQLabel * lbl = new mQLabel( "another synced sub set  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      QCheckBox * cb = new QCheckBox( this );
      cb->setMaximumHeight( 22 );
//      cb->setPalette( *palette_cb );
     
      cb->show();
      gl->addWidget( cb, ppos, 1 );
      panel1_sub_widgets.push_back( cb );
      connect( lbl, SIGNAL( pressed() ), cb, SLOT( toggle() ) );

      QString id = "synctest:checkboxrepeater2";
      global_data_types[ id ] = "cb";
      panel1_widget_map[ id ] = cb;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      QLabel * lbl = new QLabel( "Again how about a number of repeatings  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      QLineEdit * le = new QLineEdit( this );
      le->setMaximumHeight( 22 );
//      le->setPalette( *palette_le );
      le->setValidator( new QIntValidator( le ) );
      le->setText( QString( "%1" ).arg( 0 ) ); 
      ((QIntValidator*)le->validator())->setBottom( 0 ); 
      ((QIntValidator*)le->validator())->setTop( 5 ); 
      le->show();
      gl->addWidget( le, ppos, 1 );
      panel1_sub_widgets.push_back( le );

      QString id = "synctest:integerrepeater2";
      global_data_types[ id ] = "le";
      panel1_widget_map[ id ] = le;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   QHBoxLayout * hbl = new QHBoxLayout( 0 );
   {
      mQPushButton * pb = new mQPushButton( this );
      pb->setText( "Submit" );
      pb->setMaximumHeight( 22 );
      pb->show();
      connect( pb, SIGNAL( clicked() ), SLOT( module_submit_analyze_synctest() ) );
      hbl->addWidget( pb );
      hbl->addSpacing( 3 );
      panel1_sub_widgets.push_back( pb );
   }
   {
      mQPushButton * pb = new mQPushButton( this );
      pb->setText( "Reset to default values" );
      pb->setMaximumHeight( 22 );
      pb->show();
      connect( pb, SIGNAL( clicked() ), SLOT(module_reset_analyze_synctest() ) );
      hbl->addWidget( pb );
      panel1_sub_widgets.push_back( pb );
   }
   gl->addLayout( hbl, ppos, 0, 1, 2 );
   panel1_sub_layouts.push_back( hbl );
   ppos++;
   {
      QLabel * lbl = new QLabel( "", this );
//      lbl->setPalette( *palette_lbl_error );
      lbl->show();
      gl->addWidget( lbl, ppos, 0, 1, 2 );
      panel1_sub_widgets.push_back( lbl );

      QString id = "synctest:_errorMessages";
      global_data_types[ id ] = "lbl";
      panel1_widget_map[ id ] = lbl;
      panel1_inputs.push_back( id );
      save_default_value( id );
   }
   ppos++;

   reset_last_values();

   gl->setColumnStretch( 0, 0 );
   gl->setColumnStretch( 1, 0 );
   gl->setColumnStretch( 2, 1 );
   gl_panel1->addLayout( gl, 2, 0, 1, 3 );
   panel1_sub_layouts.push_back( gl );
   for ( int i = 0; i < (int) panel1_inputs.size(); ++i )
   {
      panel1_is_input[ panel1_inputs[ i ] ] = true;
   }
}

void psptest::module_reset_analyze_synctest()
{
   reset_default_values();
}

void psptest::module_submit_analyze_synctest()
{
   save_last_values();
   reset_output_values( "default_value" );
   
   // we should check if process already running

   QString program = "/home/priyanshu-sekhar/airavata-sandbox/genapp-airavata-gsoc2015/GenApp-Priyanshu/psptest/bin/synctest";

   QFileInfo qfi( program );
   if ( !qfi.exists() || !qfi.isExecutable() || qfi.isDir() )
   {
      QString id = "synctest";
      QString key = id + ":_errorMessages";    
      global_data[ key + ":last_value" ] = QVariant( "Unexpected results:\n   error => command not found or not executable" );
      if ( current_module_id == id &&
           global_data_types.count( key ) )
      {
         reset_value( key, "last_value" );
      }
      return;
   }
   

   process_json[ "synctest" ] = "";
   qDebug() << "process started";
   string tprojId = createProject("user", "synctest");
   char *projId = new char[tprojId.length() + 1];
   strcpy(projId, tprojId.c_str());
   QString tmp = input_to_json( "synctest" );
   string tjson = tmp.toUtf8().constData();
   char *json = new char[tjson.length() + 1];
   strcpy(json, tjson.c_str());
   Register* register_;
   register_ = Register::getInstance();
   string module = "synctest";
   string experiment = "Experiment";
   char* expName = new char[module.length() + experiment.length() + 1];
   strcpy(expName,module.c_str());
   strcat(expName,experiment.c_str());
   string tappId = register_->getInterfaceId("Sync Test");
   char* appId = new char[tappId.length()+1];
   strcpy(appId,tappId.c_str());
   string texpId = createExperiment("user", expName , projId, appId, json);  
   char *expId = new char[texpId.length() + 1];
   strcpy(expId, texpId.c_str());
   launchExperiment(expId);
   while((getExperimentStatus(expId))!=8){   
   cout << "status" << getExperimentStatus(expId) << endl;
   if(getExperimentStatus(expId)==9)
   {
     cout << "Experiment Failed" << endl;
     break;
   }
   unsigned int microseconds = 30000;
   usleep(microseconds);
   }

   if(getExperimentStatus(expId)==8)
   {
     unsigned int microseconds = 500000;
     usleep(microseconds);
     string output = getExperimentOutput(expId);  
     QString qoutput = QString::fromStdString(output);
     process_json[ "synctest" ] = qoutput;
     qDebug() << "process_analyze_synctest->finished()";
     qDebug() << qoutput;   
     process_results( "synctest" );
   }
  
   if(getExperimentStatus(expId)==9)
    exit(1);

}

void psptest::readyReadStandardOutput_analyze_synctest()
{
   process_json[ "synctest" ] += QString( process_analyze_synctest->readAllStandardOutput() );
}

void psptest::readyReadStandardError_analyze_synctest()
{
   qDebug() << process_analyze_synctest->readAllStandardError();
}

void psptest::error_analyze_synctest( QProcess::ProcessError e )
{
   qDebug() << "process_analyze_synctest->error()" << e;
}

void psptest::finished_analyze_synctest( int, QProcess::ExitStatus )
{
   qDebug() << "process_analyze_synctest->finished()";
   readyReadStandardOutput_analyze_synctest();
   readyReadStandardError_analyze_synctest();

   disconnect( process_analyze_synctest, 0, 0, 0 );
   // post process data into output fields, add unexpected data etc
   // delete process_analyze_synctest;
   // process_analyze_synctest = (QProcess *) 0;
   process_results( "synctest" );
}
