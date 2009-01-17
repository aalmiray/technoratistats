actions {
   action( id: 'exitAction',
      name: 'Quit',
      closure: controller.exit,
      mnemonic: 'Q',
      accelerator: shortcut('Q'),
   )

   action( id: 'aboutAction',
      name: 'About',
      closure: controller.about,
      mnemonic: 'B',
      accelerator: shortcut('B')
   )

   action( id: 'lookupAction',
      name: 'Lookup',
      enabled: bind { model.blogUrl && model.blogUrl.size() > 0 },
      closure: controller.lookup,
      mnemonic: 'L',
      accelerator: shortcut('L'),
   )
}