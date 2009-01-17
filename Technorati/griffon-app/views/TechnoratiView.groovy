import static java.awt.Color.BLACK
import ca.odell.glazedlists.gui.*
import ca.odell.glazedlists.swing.*
import com.jgoodies.forms.layout.*

build(TechnoratiActions)

def blackShadowBorder = {
   compoundBorder(
      inner: emptyBorder(3),
      outer: compoundBorder(
         inner: lineBorder(color: BLACK),
         outer: dropShadowBorder()
      )
   )
}

def createTableModel() {
   def columnNames = ["Name", "Url", "Blogs", "Links"]
   new EventTableModel(model.linksList, [
          getColumnCount: { columnNames.size() },
          getColumnName: { index -> columnNames[index] },
          getColumnValue: {object, index ->
             object."${columnNames[index].toLowerCase()}"
          }] as TableFormat)
}

def formLayout = new FormLayout(
  "l:m, 2dlu, l:p:g, 4dlu, l:m, 2dlu, l:p:g",
  "p, 2dlu, p, 4dlu, t:min(20dlu;p):g"
)
formLayout.columnGroups = [[1,5],[3,7]] as int[][]
formLayout.rowGroups = [[1,3]] as int[][]
def cc = new CellConstraints()

application( title: "Technorati Stats", size: [600,500], locationByPlatform: true,
             iconImage: imageIcon("/groovy/ui/ConsoleIcon.png").image ) {
   menuBar( build(TechnoratiMenuBar) )
   borderLayout()
   jxtitledPanel(title: "Blog URL", border: blackShadowBorder(),
                 constraints: context.NORTH) {
      panel {
      borderLayout()
         textField(columns: 120, constraints: context.CENTER, id: "urltf")
         button(lookupAction, constraints: context.EAST)
         bean(model, blogUrl: bind{ urltf.text })
      }
   }
   jxtitledPanel(title: "Output", border: blackShadowBorder(),
                 constraints: context.CENTER) {
      panel(layout:formLayout) {
         label("Inbound blogs:", constraints: cc.xy(1,1))
         label("Inbound links:", constraints: cc.xy(1,3))
         label("Rank:", constraints: cc.xy(5,1))
         label("Last updated:", constraints: cc.xy(5,3))
         textField(text: bind{ model.inboundBlogs }, constraints: cc.xy(3,1), editable: false, columns: 16 )
         textField(text: bind{ model.inboundLinks }, constraints: cc.xy(3,3), editable: false, columns: 16 )
         textField(text: bind{ model.rank },         constraints: cc.xy(7,1), editable: false, columns: 16 )
         textField(text: bind{ model.lastUpdated },  constraints: cc.xy(7,3), editable: false, columns: 16 )
         scrollPane(constraints: cc.xyw(1,5,7) ) {
            table(id: "linksTable", model: createTableModel())
            def tableSorter = new TableComparatorChooser(linksTable,
                model.linksList, AbstractTableComparatorChooser.SINGLE_COLUMN)
         }
      }
   }
}