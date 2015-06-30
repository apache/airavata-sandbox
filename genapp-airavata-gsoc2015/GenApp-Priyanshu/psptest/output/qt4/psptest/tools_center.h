void psptest::module_load_tools_center()
{
   delete_widgets_layouts( panel1_sub_widgets, panel1_sub_layouts );
   panel1_widget_map.clear();
   panel1_inputs.clear();
   panel1_outputs.clear();
   panel1_map_input.clear();
   panel1_is_input.clear();

   current_module_id = "center";

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
      QLabel * lbl = new QLabel( "Center value 1  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      QLineEdit * le = new QLineEdit( this );
      le->setMaximumHeight( 22 );
//      le->setPalette( *palette_le );
      le->setValidator( new QDoubleValidator( le ) );
      ((QDoubleValidator*)le->validator())->setDecimals( 8 );
      le->setText( QString( "%1" ).arg( 0 ) ); 
      ((QDoubleValidator*)le->validator())->setBottom( 0 ); 
      ((QDoubleValidator*)le->validator())->setTop( 1e+50 ); 
      le->show();
      gl->addWidget( le, ppos, 1 );
      panel1_sub_widgets.push_back( le );

      QString id = "center:input1";
      global_data_types[ id ] = "le";
      panel1_widget_map[ id ] = le;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      QLabel * lbl = new QLabel( "Minutes to run  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      QLineEdit * le = new QLineEdit( this );
      le->setMaximumHeight( 22 );
//      le->setPalette( *palette_le );
      le->setValidator( new QDoubleValidator( le ) );
      ((QDoubleValidator*)le->validator())->setDecimals( 8 );
      le->setText( QString( "%1" ).arg( 1 ) ); 
      ((QDoubleValidator*)le->validator())->setBottom( 0 ); 
      ((QDoubleValidator*)le->validator())->setTop( 10000 ); 
      le->show();
      gl->addWidget( le, ppos, 1 );
      panel1_sub_widgets.push_back( le );

      QString id = "center:runminutes";
      global_data_types[ id ] = "le";
      panel1_widget_map[ id ] = le;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      mQLabel * lbl = new mQLabel( "Message results  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      QCheckBox * cb = new QCheckBox( this );
      cb->setMaximumHeight( 22 );
//      cb->setPalette( *palette_cb );
      cb->setChecked( true ); 
      cb->show();
      gl->addWidget( cb, ppos, 1 );
      panel1_sub_widgets.push_back( cb );
      connect( lbl, SIGNAL( pressed() ), cb, SLOT( toggle() ) );

      QString id = "center:usemessage";
      global_data_types[ id ] = "cb";
      panel1_widget_map[ id ] = cb;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      mQLabel * lbl = new mQLabel( "Message box results  ", this );
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

      QString id = "center:usemessagebox";
      global_data_types[ id ] = "cb";
      panel1_widget_map[ id ] = cb;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      QLabel * lbl = new QLabel( "Results interval in seconds  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      QLineEdit * le = new QLineEdit( this );
      le->setMaximumHeight( 22 );
//      le->setPalette( *palette_le );
      le->setValidator( new QIntValidator( le ) );
      le->setText( QString( "%1" ).arg( 10 ) ); 
      ((QIntValidator*)le->validator())->setBottom( 5 ); 
      ((QIntValidator*)le->validator())->setTop( 1000 ); 
      le->show();
      gl->addWidget( le, ppos, 1 );
      panel1_sub_widgets.push_back( le );

      QString id = "center:messagesecs";
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
      connect( pb, SIGNAL( clicked() ), SLOT( module_submit_tools_center() ) );
      hbl->addWidget( pb );
      hbl->addSpacing( 3 );
      panel1_sub_widgets.push_back( pb );
   }
   {
      mQPushButton * pb = new mQPushButton( this );
      pb->setText( "Reset to default values" );
      pb->setMaximumHeight( 22 );
      pb->show();
      connect( pb, SIGNAL( clicked() ), SLOT(module_reset_tools_center() ) );
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

      QString id = "center:_errorMessages";
      global_data_types[ id ] = "lbl";
      panel1_widget_map[ id ] = lbl;
      panel1_inputs.push_back( id );
      save_default_value( id );
   }
   ppos++;
   {
      QLabel * lbl = new QLabel( "Message text  ", this );
      // lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      QTextEdit * te = new QTextEdit( this );
      //  lb->setMaximumHeight( 22 );
//      te->setPalette( *palette_le );
      te->show();
      te->setReadOnly( true );
      gl->addWidget( te, ppos, 1 );
      panel1_sub_widgets.push_back( te );

      QString id = "center:output2";
      global_data_types[ id ] = "te";
      panel1_widget_map[ id ] = te;
      panel1_outputs.push_back( id );
      save_default_value( id );
      // panel1_map_input[ id ] = "center:__fields:name__";

      ppos++;
   }
   {
      QLabel * lbl = new QLabel( "Output value 1  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      QLineEdit * le = new QLineEdit( this );
      le->setMaximumHeight( 22 );
//      le->setPalette( *palette_le );
      le->setReadOnly( true );
      le->show();
      gl->addWidget( le, ppos, 1 );
      panel1_sub_widgets.push_back( le );

      QString id = "center:output1";
      global_data_types[ id ] = "le";
      panel1_widget_map[ id ] = le;
      panel1_outputs.push_back( id );
      save_default_value( id );

      ppos++;
   }

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

void psptest::module_reset_tools_center()
{
   reset_default_values();
}

void psptest::module_submit_tools_center()
{
   save_last_values();
   reset_output_values( "default_value" );
   
   // we should check if process already running

   QString program = "/home/priyanshu-sekhar/airavata-sandbox/genapp-airavata-gsoc2015/GenApp-Priyanshu/psptest/bin/center";

   QFileInfo qfi( program );
   if ( !qfi.exists() || !qfi.isExecutable() || qfi.isDir() )
   {
      QString id = "center";
      QString key = id + ":_errorMessages";    
      global_data[ key + ":last_value" ] = QVariant( "Unexpected results:\n   error => command not found or not executable" );
      if ( current_module_id == id &&
           global_data_types.count( key ) )
      {
         reset_value( key, "last_value" );
      }
      return;
   }
   

   process_json[ "center" ] = "";
   qDebug() << "process started";
   string tprojId = createProject("user", "center");
   char *projId = new char[tprojId.length() + 1];
   strcpy(projId, tprojId.c_str());
   QString tmp = input_to_json( "center" );
   string tjson = tmp.toUtf8().constData();
   char *json = new char[tjson.length() + 1];
   strcpy(json, tjson.c_str());
   Register* register_;
   register_ = Register::getInstance();
   string module = "center";
   string experiment = "Experiment";
   char* expName = new char[module.length() + experiment.length() + 1];
   strcpy(expName,module.c_str());
   strcat(expName,experiment.c_str());
   string tappId = register_->getInterfaceId("Center");
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
     process_json[ "center" ] = qoutput;
     qDebug() << "process_tools_center->finished()";
     qDebug() << qoutput;   
     process_results( "center" );
   }
  
   if(getExperimentStatus(expId)==9)
    exit(1);

}

void psptest::readyReadStandardOutput_tools_center()
{
   process_json[ "center" ] += QString( process_tools_center->readAllStandardOutput() );
}

void psptest::readyReadStandardError_tools_center()
{
   qDebug() << process_tools_center->readAllStandardError();
}

void psptest::error_tools_center( QProcess::ProcessError e )
{
   qDebug() << "process_tools_center->error()" << e;
}

void psptest::finished_tools_center( int, QProcess::ExitStatus )
{
   qDebug() << "process_tools_center->finished()";
   readyReadStandardOutput_tools_center();
   readyReadStandardError_tools_center();

   disconnect( process_tools_center, 0, 0, 0 );
   // post process data into output fields, add unexpected data etc
   // delete process_tools_center;
   // process_tools_center = (QProcess *) 0;
   process_results( "center" );
}
