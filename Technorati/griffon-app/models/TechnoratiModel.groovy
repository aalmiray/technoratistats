import groovy.beans.Bindable
import ca.odell.glazedlists.*

class TechnoratiModel {
   @Bindable String blogUrl = ""
   @Bindable String inboundBlogs = ""
   @Bindable String inboundLinks = ""
   @Bindable String rank = ""
   @Bindable String lastUpdated = ""

   EventList linksList = new SortedList(
      new BasicEventList(),
      {a, b -> b.name <=> a.name} as Comparator
   )
}