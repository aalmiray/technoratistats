import static griffon.util.GriffonApplicationUtils.*

menuBar( id: 'menuBar') {
   menu(text: 'File', mnemonic: 'F') {
       menuItem(lookupAction)
       if( !isMacOSX ) {
           separator()
           menuItem(exitAction)
       }
   }

   if( !isMacOSX ) {
       glue()
       menu(text: 'Help', mnemonic: 'H') {
           menuItem(aboutAction)
       }
   }
}