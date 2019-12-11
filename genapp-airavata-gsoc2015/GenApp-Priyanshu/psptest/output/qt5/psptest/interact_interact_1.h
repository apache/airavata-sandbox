void psptest::module_load_interact_interact_1()
{
   delete_widgets_layouts( panel1_sub_widgets, panel1_sub_layouts );
   panel1_widget_map.clear();
   panel1_inputs.clear();
   panel1_outputs.clear();
   panel1_map_input.clear();
   panel1_is_input.clear();

   current_module_id = "interact_1";

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
      QLabel * lbl = new QLabel( "Input value 1  ", this );
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

      QString id = "interact_1:input1";
      global_data_types[ id ] = "le";
      panel1_widget_map[ id ] = le;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      QLabel * lbl = new QLabel( "Input value 2  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      QLineEdit * le = new QLineEdit( this );
      le->setMaximumHeight( 22 );
//      le->setPalette( *palette_le );
      le->setValidator( new QIntValidator( le ) );
      le->setText( QString( "%1" ).arg( 2 ) ); 
      ((QIntValidator*)le->validator())->setBottom( 0 ); 
      ((QIntValidator*)le->validator())->setTop( 10 ); 
      le->show();
      gl->addWidget( le, ppos, 1 );
      panel1_sub_widgets.push_back( le );

      QString id = "interact_1:input2";
      global_data_types[ id ] = "le";
      panel1_widget_map[ id ] = le;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      mQLabel * lbl = new mQLabel( "Input value 3  ", this );
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

      QString id = "interact_1:input3";
      global_data_types[ id ] = "cb";
      panel1_widget_map[ id ] = cb;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      QLabel * lbl = new QLabel( "listbox  ", this );
      // lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      QListWidget * lw = new QListWidget( this );
      //  lb->setMaximumHeight( 22 );
//      lw->setPalette( *palette_le );
      lw->show();
      gl->addWidget( lw, ppos, 1 );
      panel1_sub_widgets.push_back( lw );

      QString id = "interact_1:input8";
      { QString qs = "choice 1"; lw->addItem( qs ); panel1_map_input[ QString( "lw:%1" ).arg( qs ) ] = "c1"; }{ QString qs = "choice 2"; lw->addItem( qs ); panel1_map_input[ QString( "lw:%1" ).arg( qs ) ] = "c2"; }{ QString qs = "choice 3"; lw->addItem( qs ); panel1_map_input[ QString( "lw:%1" ).arg( qs ) ] = "c3"; }
      lw->setCurrentRow( 0 );
      global_data_types[ id ] = "lw";
      panel1_widget_map[ id ] = lw;
      panel1_inputs.push_back( id );
      save_default_value( id );
      panel1_map_input[ id ] = "interact_1:input8";

      ppos++;
   }
   {
      QLabel * lbl = new QLabel( "Input value 4  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      QLineEdit * le = new QLineEdit( this );
      le->setMaximumHeight( 22 );
//      le->setPalette( *palette_le );
      le->setValidator( new QIntValidator( le ) );
      le->setText( QString( "%1" ).arg( 1 ) ); 
      ((QIntValidator*)le->validator())->setBottom( 1 ); 
      ((QIntValidator*)le->validator())->setTop( 10 ); 
      le->show();
      gl->addWidget( le, ppos, 1 );
      panel1_sub_widgets.push_back( le );

      QString id = "interact_1:input4";
      global_data_types[ id ] = "le";
      panel1_widget_map[ id ] = le;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      QLabel * lbl = new QLabel( "Input file  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      mQPushButton * pb = new mQPushButton( this );
      pb->setText( "Browse..." );
      pb->setMaximumHeight( 22 );
      pb->show();
      gl->addWidget( pb, ppos, 1 );
      panel1_sub_widgets.push_back( pb );
      connect( pb, SIGNAL( clicked() ), this, SLOT( browse_interact_1_input5() ) );

      QLabel * lbl2 = new QLabel( "", this );
      pb->mbuddy = lbl2;
      lbl2->setMaximumHeight( 22 );
      lbl2->show();
      gl->addWidget( lbl2, ppos, 2 );
      panel1_sub_widgets.push_back( lbl2 );

      QString id = "interact_1:input5";
      global_data_types[ id ] = "file";
      panel1_widget_map[ id ] = pb;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      QLabel * lbl = new QLabel( "Input files  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      mQPushButton * pb = new mQPushButton( this );
      pb->setText( "Browse..." );
      pb->setMaximumHeight( 22 );
      pb->show();
      gl->addWidget( pb, ppos, 1 );
      panel1_sub_widgets.push_back( pb );
      connect( pb, SIGNAL( clicked() ), this, SLOT( browse_interact_1_input6() ) );

      QLabel * lbl2 = new QLabel( "", this );
      pb->mbuddy = lbl2;
      lbl2->setMaximumHeight( 22 );
      lbl2->show();
      gl->addWidget( lbl2, ppos, 2 );
      panel1_sub_widgets.push_back( lbl2 );

      QString id = "interact_1:input6";
      global_data_types[ id ] = "files";
      panel1_widget_map[ id ] = pb;
      panel1_inputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      mQLabel * lbl = new mQLabel( "Radiobutton a  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      mQRadioButton * rb = new mQRadioButton( this );
      rb->setMaximumHeight( 22 );
//      rb->setPalette( *palette_cb );
     
      rb->show();
      gl->addWidget( rb, ppos, 1 );
      rb->data = QVariant( "input7a" );
      panel1_sub_widgets.push_back( rb );
      connect( lbl, SIGNAL( pressed() ), rb, SLOT( toggle() ) );

      if ( !panel1_widget_map.count( "gb:input7" ) )
      {
         panel1_widget_map[ "gb:input7" ] = new QGroupBox( 0 );
         ((QGroupBox *)panel1_widget_map[ "gb:input7" ])->setFlat( true );
      }

// #warn radio needs work
//      ((QGroupBox *)panel1_widget_map[ "hb:input7" ])->insert( rb );

      QString id = "interact_1:input7a";
      global_data_types[ id ] = "rb";
      panel1_widget_map[ id ] = rb;
      panel1_inputs.push_back( id );
      save_default_value( id );
      panel1_map_input[ id ] = "interact_1:input7";

      ppos++;
   }
   {
      mQLabel * lbl = new mQLabel( "Radiobutton b  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      mQRadioButton * rb = new mQRadioButton( this );
      rb->setMaximumHeight( 22 );
//      rb->setPalette( *palette_cb );
     
      rb->show();
      gl->addWidget( rb, ppos, 1 );
      rb->data = QVariant( "input7b" );
      panel1_sub_widgets.push_back( rb );
      connect( lbl, SIGNAL( pressed() ), rb, SLOT( toggle() ) );

      if ( !panel1_widget_map.count( "gb:input7" ) )
      {
         panel1_widget_map[ "gb:input7" ] = new QGroupBox( 0 );
         ((QGroupBox *)panel1_widget_map[ "gb:input7" ])->setFlat( true );
      }

// #warn radio needs work
//      ((QGroupBox *)panel1_widget_map[ "hb:input7" ])->insert( rb );

      QString id = "interact_1:input7b";
      global_data_types[ id ] = "rb";
      panel1_widget_map[ id ] = rb;
      panel1_inputs.push_back( id );
      save_default_value( id );
      panel1_map_input[ id ] = "interact_1:input7";

      ppos++;
   }
   {
      mQLabel * lbl = new mQLabel( "Radiobutton c  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      mQRadioButton * rb = new mQRadioButton( this );
      rb->setMaximumHeight( 22 );
//      rb->setPalette( *palette_cb );
      rb->setChecked( true ); 
      rb->show();
      gl->addWidget( rb, ppos, 1 );
      rb->data = QVariant( "input7c" );
      panel1_sub_widgets.push_back( rb );
      connect( lbl, SIGNAL( pressed() ), rb, SLOT( toggle() ) );

      if ( !panel1_widget_map.count( "gb:input7" ) )
      {
         panel1_widget_map[ "gb:input7" ] = new QGroupBox( 0 );
         ((QGroupBox *)panel1_widget_map[ "gb:input7" ])->setFlat( true );
      }

// #warn radio needs work
//      ((QGroupBox *)panel1_widget_map[ "hb:input7" ])->insert( rb );

      QString id = "interact_1:input7c";
      global_data_types[ id ] = "rb";
      panel1_widget_map[ id ] = rb;
      panel1_inputs.push_back( id );
      save_default_value( id );
      panel1_map_input[ id ] = "interact_1:input7";

      ppos++;
   }
   QHBoxLayout * hbl = new QHBoxLayout( 0 );
   {
      mQPushButton * pb = new mQPushButton( this );
      pb->setText( "Submit" );
      pb->setMaximumHeight( 22 );
      pb->show();
      connect( pb, SIGNAL( clicked() ), SLOT( module_submit_interact_interact_1() ) );
      hbl->addWidget( pb );
      hbl->addSpacing( 3 );
      panel1_sub_widgets.push_back( pb );
   }
   {
      mQPushButton * pb = new mQPushButton( this );
      pb->setText( "Reset to default values" );
      pb->setMaximumHeight( 22 );
      pb->show();
      connect( pb, SIGNAL( clicked() ), SLOT(module_reset_interact_interact_1() ) );
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

      QString id = "interact_1:_errorMessages";
      global_data_types[ id ] = "lbl";
      panel1_widget_map[ id ] = lbl;
      panel1_inputs.push_back( id );
      save_default_value( id );
   }
   ppos++;
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

      QString id = "interact_1:output1";
      global_data_types[ id ] = "le";
      panel1_widget_map[ id ] = le;
      panel1_outputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      QLabel * lbl = new QLabel( "input json reference  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      mQLabel * lbl2 = new mQLabel( "", this );
      
      lbl2->setMaximumHeight( 22 );
      lbl2->show();
      gl->addWidget( lbl2, ppos, 1 );
      panel1_sub_widgets.push_back( lbl2 );
      connect( lbl2, SIGNAL( pressed() ), this, SLOT( browse_interact_1_outputref() ) );

      QString id = "interact_1:outputref";
      global_data_types[ id ] = "outfile";
      panel1_widget_map[ id ] = lbl2;
      panel1_outputs.push_back( id );
      save_default_value( id );

      ppos++;
   }
   {
      QLabel * lbl = new QLabel( "results json reference  ", this );
      lbl->setMaximumHeight( 22 );
      lbl->show();
      gl->addWidget( lbl, ppos, 0 );
      panel1_sub_widgets.push_back( lbl );

      mQLabel * lbl2 = new mQLabel( "", this );
      
      lbl2->setMaximumHeight( 22 );
      lbl2->show();
      gl->addWidget( lbl2, ppos, 1 );
      panel1_sub_widgets.push_back( lbl2 );
      connect( lbl2, SIGNAL( pressed() ), this, SLOT( browse_interact_1_outputres() ) );

      QString id = "interact_1:outputres";
      global_data_types[ id ] = "outfile";
      panel1_widget_map[ id ] = lbl2;
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

void psptest::module_reset_interact_interact_1()
{
   reset_default_values();
}

void psptest::module_submit_interact_interact_1()
{
   save_last_values();
   reset_output_values( "default_value" );
   // qDebug() << input_to_json( "interact_1" );
   // we should check if process already running

   QString program = "/home/priyanshu-sekhar/airavata-sandbox/genapp-airavata-gsoc2015/GenApp-Priyanshu/psptest/bin/interact_1";

   QFileInfo qfi( program );
   if ( !qfi.exists() || !qfi.isExecutable() || qfi.isDir() )
   {
      QString id = "interact_1";
      QString key = id + ":_errorMessages";    
      global_data[ key + ":last_value" ] = QVariant( "Unexpected results:\n   error => command not found or not executable" );
      if ( current_module_id == id &&
           global_data_types.count( key ) )
      {
         reset_value( key, "last_value" );
      }
      return;
   }
   //   qDebug() << "process exists and isExecutable";
   
   // make a temporary directory:

   process_json[ "interact_1" ] = "";
   qDebug() << "process started";
   string tprojId = createProject("user", "interact_1");
   char *projId = new char[tprojId.length() + 1];
   strcpy(projId, tprojId.c_str());
   QString tmp = input_to_json( "interact_1" );
   string tjson = tmp.toUtf8().constData();
   char *json = new char[tjson.length() + 1];
   strcpy(json, tjson.c_str());
   Register* register_;
   register_ = Register::getInstance();
   string module = "interact_1";
   string experiment = "Experiment";
   char* expName = new char[module.length() + experiment.length() + 1];
   strcpy(expName,module.c_str());
   strcat(expName,experiment.c_str());
   string tappId = register_->getInterfaceId("Interact 1");
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
     process_json[ "interact_1" ] = qoutput;
     qDebug() << "process_interact_interact_1->finished()";
     qDebug() << qoutput;   
     process_results( "interact_1" );
   }
  
   if(getExperimentStatus(expId)==9)
    exit(1);
  // disconnect( process_interact_interact_1, 0, 0, 0 );
   // post process data into output fields, add unexpected data etc
   // delete process_interact_interact_1;
   // process_interact_interact_1 = (QProcess *) 0;


}

void psptest::readyReadStandardOutput_interact_interact_1()
{
   process_json[ "interact_1" ] += QString( process_interact_interact_1->readAllStandardOutput() );
}

void psptest::readyReadStandardError_interact_interact_1()
{
   qDebug() << process_interact_interact_1->readAllStandardError();
}

void psptest::error_interact_interact_1( QProcess::ProcessError e )
{
   qDebug() << "process_interact_interact_1->error()" << e;
}

void psptest::finished_interact_interact_1( int, QProcess::ExitStatus )
{
   qDebug() << "process_interact_interact_1->finished()";
   readyReadStandardOutput_interact_interact_1();
   readyReadStandardError_interact_interact_1();

   disconnect( process_interact_interact_1, 0, 0, 0 );
   // post process data into output fields, add unexpected data etc
   // delete process_interact_interact_1;
   // process_interact_interact_1 = (QProcess *) 0;
   process_results( "interact_1" );
}
