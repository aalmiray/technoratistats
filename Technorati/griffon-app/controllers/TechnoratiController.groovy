import javax.swing.JOptionPane

class TechnoratiController {
   def model
   def builder
   def http = new HttpHelper()

   static final String API_KEY = "5130a7c7e3cc4ec657132cd7f068f5f4"
   static final String COSMOS_URL = "http://api.technorati.com/cosmos"

   def lookup = { evt = null ->
      if( !model.blogUrl ) return
      builder.lookupAction.enabled = false

      // clear previous info
      model.inboundBlogs = 0
      model.inboundLinks = 0
      model.lastUpdated = ""
      model.rank = 0
      model.linksList.clear()

      doOutside {
         try {
            def cosmos = http.post(COSMOS_URL, [key: API_KEY, url: model.blogUrl, limit: 20])
            if(!cosmos) {
                error("Query", "Couldn't retrieve Technorati stats for\n\n${model.blogUrl}")
            } else if (cosmos.document?.result?.error?.text()?.size() > 0) {
                error("Query", cosmos.document.result.error.text())
            } else {
                def root = cosmos.document.result

                def list = []
                def linkNames = [] // skip repeated names
                cosmos.document.item?.each { link ->
                   def name = link?.weblog?.name?.text()?.trim()
                   if (!linkNames.find {it == name}) {
                      linkNames << name
                      list << [
                         name: name,
                         url: link?.weblog?.url?.text()?.trim() ?: "<nourl>",
                         blogs: link?.weblog?.inboundblogs?.text()?.toInteger() ?: 0,
                         links: link?.weblog?.inboundlinks?.text()?.toInteger() ?: 0
                      ]
                   }
                }

                doLater {
                   // try first inside <weblog>
                   model.inboundBlogs = root.weblog?.inboundblogs?.text() ?: 0
                   model.inboundLinks = root.weblog?.inboundlinks?.text() ?: 0
                   model.lastUpdated = root.weblog?.lastupdate?.text() ?: ""
                   model.rank = root.weblog?.rank?.text() ?: 0

                   // try next outside <weblog>
                   if(model.inboundBlogs == "0") model.inboundBlogs = root?.inboundblogs?.text() ?: 0
                   if(model.inboundLinks == "0") model.inboundLinks = root?.inboundlinks?.text() ?: 0
                   if(model.rank == "0") model.rank = root.weblog?.rankingstart?.text() ?: 0

                   model.linksList.addAll(list)
                }
            }
         } finally {
            edt { builder.lookupAction.enabled = true }
         }
      }
   }

   def about = { evt = null ->
      def pane = builder.optionPane()
      pane.setMessage("Welcome to Technorati Stats,\na sample Griffon application.")
      def dialog = pane.createDialog(app.appFrames[0], 'About Technorati Stats')
      dialog.show()
   }

    def exit = { evt = null ->
      app.shutdown()
   }

   def displayDialog( type, title, message ) {
      doLater {
         JOptionPane.showMessageDialog(app.appFrames[0], message.toString(),
               title, type )
      }
   }
   def info = this.&displayDialog.curry(JOptionPane.INFORMATION_MESSAGE)
   def warning = this.&displayDialog.curry(JOptionPane.WARNING_MESSAGE)
   def error = this.&displayDialog.curry(JOptionPane.ERROR_MESSAGE)
}
