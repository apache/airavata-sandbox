void psptest::interact_pressed()
{
   hide_widgets( menu_widgets );
   menu_button->setPixmap( id_to_icon[ "interact" ] );
   delete_widgets_layouts( panel1_widgets, panel1_layouts );
   delete_widgets_layouts( panel1_sub_widgets, panel1_sub_layouts );
   panel1_widget_map.clear();
   panel1_inputs.clear();
   panel1_outputs.clear();
   panel1_map_input.clear();

   QHBoxLayout *hbl = new QHBoxLayout( 0 );

   {
      mQPushButton * pb = new mQPushButton( this );
      pb->setText( "Interact 1" );
      pb->setMaximumHeight( 22 );
      pb->show();
      // this is supposed to automatically disconnect on deletion of the widget
      connect( pb, SIGNAL( clicked() ), SLOT( module_load_interact_interact_1() ) );
      if ( panel1_widgets.size() )
      {
         hbl->addSpacing( 2 );
      }
      hbl->addWidget( pb );
      panel1_widgets.push_back( pb );
   }
   {
      mQPushButton * pb = new mQPushButton( this );
      pb->setText( "Plot Test" );
      pb->setMaximumHeight( 22 );
      pb->show();
      // this is supposed to automatically disconnect on deletion of the widget
      connect( pb, SIGNAL( clicked() ), SLOT( module_load_interact_plottest() ) );
      if ( panel1_widgets.size() )
      {
         hbl->addSpacing( 2 );
      }
      hbl->addWidget( pb );
      panel1_widgets.push_back( pb );
   }
   {
      QLabel * lbl = new QLabel( "", this );
      gl_panel1->addWidget( lbl, 3, 0 );
      panel1_widgets.push_back( lbl );
   }

   gl_panel1->addLayout( hbl, 0, 1 );
   panel1_layouts.push_back( hbl );
   gl_panel1->setColumnStretch( 0, 1 );
   gl_panel1->setColumnStretch( 1, 0 );
   gl_panel1->setColumnStretch( 2, 1 );
   gl_panel1->setRowStretch( 0, 0 );
   gl_panel1->setRowStretch( 1, 0 );
   gl_panel1->setRowStretch( 2, 0 );
   gl_panel1->setRowStretch( 3, 1 );
}
